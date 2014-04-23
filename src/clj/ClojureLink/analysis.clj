(ns ClojureLink.analysis
  (:require
   [clojure.jvm.tools.analyzer :as azr]
   [clojure.jvm.tools.analyzer.emit-form :as ae]
   [instaparse.core :as insta]))

;; ---------------


;; [[file:~/code/source-graph/source-graph.org::*ns%20shit][core]]

;; (ns source-graph.core
;;   (:refer-clojure :exclude [==])
;;   (:use clojure.repl
;;         clojure.pprint
;;         tsg.kits.introspection
;;         clojure.core.logic
;;         [clojure.core.match :only [match]])
;;   (:require [clj-schema.schema :as schema]
;;             [clj-schema.validation :as validation]

;;             [clojure.java.io :as io]

;;             [clojure.tools.analyzer :as azr]
;;             [clojure.tools.analyzer.emit-form :as ae]

;;             [tsg.kits.files :as tf]
;;             ;; [tsg.kits.maps :as tm]
;;             ;; [tsg.kits.macros :as tmc]

;;             [loom.graph :as lg]
;;             [loom.attr :as lat]
;;             [loom.gen :as lgen]
;;             [loom.io :as lio]
;;             [loom.label :as ll]))


;; (schema/def-map-schema :loose NsOpts
;;   [[:path] string?
;;    [:ns] symbol?])

(defn ensure-set [x]
  (if (set? x) x (set x)))

(defn ast-seq [ast]
  (tree-seq coll? :children ast))

(defn ns-ast-seq [{:keys [path ns] :as nsopts}]
  (->>
   ;; (if path
   ;;  (azr/analyze-ns (azr/pb-reader-for-ns ns) path ns {:children true})
   ;;  (azr/analyze-ns ns {:children true}))
   (azr/analyze-ns ns :reader (azr/pb-reader-for-ns ns) :opt {:children true})
   (map ast-seq)
   (tree-seq coll? seq)))

(defn definiendum [x]
  (case (:op x)
    :def (:var x)
    :deftype* (:name x)
    nil))

(defn declaring-form? [x]
  (boolean (definiendum x)))

(defn declaring-forms [nsopts]
  (->> (ns-ast-seq nsopts)
       (filter declaring-form?)))

(defn extract-var [x]
  (:var x))

(defn var-node? [x]
  (= :var (:op x)))

(defn vars-in-form [x]
  (->> x
       (tree-seq coll? seq)
       (filter var-node?)
       (map extract-var)))

(defn reference-edges [form vars]
  (when-let [d (definiendum form)]
    (let [d' (second (re-find #"/(.*)$" (str d)))
          vars' (ensure-set vars)]
      (->> form
           vars-in-form
           (filter vars')
           frequencies
           (map (fn [[v freq]]
                  [d'
                   (second (re-find #"/(.*)$" (str v)))
                   freq]))))))

;; could stand some prettification:

(defn reference-graph [nsopts]
  (let [dfs (declaring-forms nsopts)
        decvars (set (map definiendum dfs))]
    (mapv
     (fn [df]
       (into
        [(second (re-find #"/(.*)$" (str (definiendum df))))]
        (reference-edges df decvars)))
      dfs)))

;; (defn reference-graph [nsopts]
;;   (let [dfs (declaring-forms nsopts)
;;         decvars (set (map definiendum dfs))]
;;     (reduce
;;       (fn [g df]
;;         (let [d (definiendum df)]
;;           (reduce #(lg/add-edges %1 %2)
;;             g
;;             (reference-edges df decvars))))
;;       (lg/digraph)
;;       dfs)))

;; (schema/def-simple-schema GraphvizAlgorithm
;;   #{:dot :neato :fdp :sfdp :twopi :circo})

;; (defn show-graph
;;   ([g] (show-graph g nil))
;;   ([g opts]
;;      (apply lio/view g (apply concat opts))))  

;; (defn graph-ns-refs
;;   ([{:keys [ns path]
;;      :or {path (.getAbsolutePath (tf/project-dir))}
;;      :as nsopts}]
;;      {:pre [(symbol? ns)
;;             (string? path)]}
;;      (show-graph (reference-graph nsopts) nsopts)))

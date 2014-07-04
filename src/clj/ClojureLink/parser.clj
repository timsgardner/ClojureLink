(ns ClojureLink.parser
  (:use clojure.repl
        clojure.pprint
        clojure.data)
  (:require [instaparse.core :as insta]
            [clojure.java.io :as io]))

(defn to-line-vec [f]
  (let [f' (io/file
             ;;(io/resource f)
             f)]
    (with-open [r (io/reader f')]
      (vec (line-seq r)))))

(defn lines-to-string [lv]
  (clojure.string/join "\n" lv))

(defn make-grammar [path]
  (insta/parser
    (->> path
         to-line-vec
         (remove #(re-matches #"^\s*#.*" %))
         lines-to-string)))

; because everything's still broken:
(def awful-hardcoded-path-to-grammar
  "/Users/timothygardner/code/ClojureLink/resources/instaparse/clojure.txt")

(def clojure-grammar
  (make-grammar awful-hardcoded-path-to-grammar))

;;(def clojure-file-1 (to-line-vec "test-files/clojure-test-1.clj"))

(defn parse-file-dispatch [f]
  (type f))

(defn parse-string [s]
  (insta/parse clojure-grammar s))

(defmulti parse-file #'parse-file-dispatch)

(defmethod parse-file java.lang.String [f]
  (->> f slurp parse-string))

(defmethod parse-file java.io.File [f]
  (->> f slurp parse-string))

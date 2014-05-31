(ns ClojureLink.parser-test
  (:use clojure.test
        clojure.pprint
        clojure.repl)
  (:require [ClojureLink.parser :as cp]
            [instaparse.core :as insta]
            [clojure.java.io :as io]
            [ClojureLink.parser :as cp]))

(def quil-src-path "/Users/timothygardner/code/quil/src")

(def quil-calc-path (str quil-src-path "/quil/helpers/calc.clj"))

(deftest quil-calc
  (cp/parse-file quil-calc-path))

(defn clojure-files [dir]
  (->> (clojure.java.io/file dir)
       (file-seq)
       (map (memfn getAbsolutePath))
       (filter #(re-matches #".*\.clj" %))))

(def quil-src-files (clojure-files quil-src-path))

(deftest quil-src
  (is (doall (map #(cp/parse-file %) quil-src-files))))

(def clojurelink-path "/Users/timothygardner/code/ClojureLink")

(def clojurelink-src-files (clojure-files clojurelink-path))

(deftest clojurelink-src
  (is (doall (map #(cp/parse-file %) clojurelink-src-files))))


(ns clj.ClojureLink.parser
  (:use clojure.repl
        clojure.pprint
        clojure.data)
  (:require [instaparse.core :as insta]
            [clojure.java.io :as io]))

(defn to-line-vec [f]
  (let [f' (io/file (io/resource f))]
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

(def clojure-grammar-1
  (make-grammar "instaparse/clojure.txt"))

(def clojure-file-1 (to-line-vec "test-files/clojure-test-1.clj"))


(comment
  (pprint
    (->> (clojure.string/split-lines
           (slurp "src/clj/ClojureLink/parser.clj"))
         (take 14)
         lines-to-string
         (#(insta/parses clojure-grammar-1 % :total true))))

  (pprint
    (->> "(clojure.string/join \"\n\")"
         (#(insta/parses clojure-grammar-1 % :total true))))

  "\"bla\\\\\\\"\""

  (pprint
    (->>
      "\"\\\"bla\\\\\\\\\\\\\\\"\\\"\""
      (#(insta/parses clojure-grammar-1 % :total true))))

  (pprint
    (->>
      "(defn lines-to-string [lv]\n(clojure.string/join \"\\n\" lv))"
      (#(insta/parses clojure-grammar-1 % :total true)))))

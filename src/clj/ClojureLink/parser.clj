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

(def clojure-grammar-1
  (make-grammar awful-hardcoded-path-to-grammar))

;;(def clojure-file-1 (to-line-vec "test-files/clojure-test-1.clj"))

(defn parse-file-dispatch [f]
  (type f))

(defn parse-string [s]
  (insta/parse clojure-grammar-1 s))

(defmulti parse-file #'parse-file-dispatch)

(defmethod parse-file java.lang.String [f]
           (->> f slurp parse-string))

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
      "@house"
      (#(insta/parses clojure-grammar-1 % :total true))))

  (->>
    "/Users/timothygardner/code/immutable-stack/contacts/src/clj/contacts/core.clj"
    slurp
    clojure.string/split-lines
    (take 23)
    lines-to-string
    (#(insta/parses clojure-grammar-1 % :total true))
    pprint))


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
  (->> path
    to-line-vec
    (remove #(re-matches #"^\s*#.*" %))
    lines-to-string
    insta/parser))

; because everything's still broken:
(def awful-hardcoded-path-to-grammar
  "/Users/timothygardner/code/ClojureLink/resources/instaparse/clojure.txt")

(def ^:dynamic *clojure-grammar*
  (make-grammar awful-hardcoded-path-to-grammar))

(defmacro with-grammar [g & body]
  `(let [g# ~g]
     (binding [*clojure-grammar* (if (string? g#)
                                   (make-grammar g#)
                                   g#)]
       ~@body)))

(defn parse-string [s]
  (insta/parse *clojure-grammar* s))

(defn parse-file
  ([f]
     (->> f slurp parse-string))
  ([f strtln]
     (parse-string
       (lines-to-string
         (subvec (to-line-vec f)
           strtln))))
  ([f strtln endln]
     (parse-string
       (lines-to-string
         (subvec (to-line-vec f)
           strtln
           endln)))))

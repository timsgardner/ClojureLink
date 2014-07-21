(ns ClojureLink.parser
  (:use clojure.repl
        clojure.pprint
        clojure.data)
  (:require [instaparse.core :as insta]
            [clojure.java.io :as io]
            [clojure.core.match :as match]
            [clojure.walk :as walk]))

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

; hardcoded because everything's still broken:

(def clojure-sexp-grammar  "/Users/timothygardner/code/ClojureLink/resources/instaparse/clojure-sexp-grammar.txt")
(def clojure-word-grammar  "/Users/timothygardner/code/ClojureLink/resources/instaparse/clojure-word-grammar.txt")


(def ^:dynamic *clojure-grammar*
  clojure-sexp-grammar)

(defmacro with-grammar [g & body]
  `(let [g# ~g]
     (binding [*clojure-grammar* (if (string? g#)
                                   (make-grammar g#)
                                   g#)]
       ~@body)))

(defn parse-string [s]
  (insta/parse *clojure-grammar* s))

(defn parses-string [s]
  (insta/parses *clojure-grammar* s))

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

(defn parses-file
  ([f]
     (->> f slurp parses-string))
  ([f strtln]
     (parses-string
       (lines-to-string
         (subvec (to-line-vec f)
           strtln))))
  ([f strtln endln]
     (parses-string
       (lines-to-string
         (subvec (to-line-vec f)
           strtln
           endln)))))

;; other part ------------------------------------

(defn tag-type? [x t]
  (match/match [x]
    [[t & _]] true
    :else false))

(defn word? [x]
  (tag-type? x :word))

(defn metadata? [x]
  (tag-type? x :metadata))

(defn file-section [{:keys [file] :as argm}]
  (match/match [argm]
    [{:start s :end e}] (lines-to-string
                          (subvec (to-line-vec file) s e))
    [{:start s}]        (lines-to-string
                          (subvec (to-line-vec file) s))
    :else               (slurp file)))

 ;; this will reload grammar each time by default. maybe change for
 ;; release, can do so by redefining the grammars to parsers rather
 ;; than strings.

(defn default-clojure-parser [s]
  (let [g1 (make-grammar clojure-sexp-grammar)
        g2 (make-grammar clojure-word-grammar)]
    (->> s
      (insta/parse g1)
      (insta/transform
        {:word #(first (insta/parse g2 %))}))))

(defn parse-file* [{:keys [parser]
                    :or {parser default-clojure-parser}
                    :as argm}]
  (parser (file-section argm)))

(defn parse-clojure-string [s]
  (default-clojure-parser s))

(defn parse-clojure-file 
  ([f]
     (parse-file*
       {:file f
        :parser default-clojure-parser}))
  ([f strtln]
     (parse-file*
       {:file f
        :parser default-clojure-parser
        :start strtln}))
  ([f strtln endln]
     (parse-file*
       {:file f
        :parser default-clojure-parser
        :start strtln
        :end endln})))

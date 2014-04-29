(ns clojure-test-1
  (:use clojure.repl))

; This is an example file
; for testing my instaparse thingum

(defn fun-1 [& xs] #(vector {%, (into #{"bla"} xs)}))

#_(who knows what
      #_(evil lurks
             "in the \"heart of man?\""
             #rabbit (not-me!)))

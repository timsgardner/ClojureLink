(defproject ClojureLink "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :source-path "src/clj"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.clojure/core.logic "0.8.7"]
                 
                 ;;                 [clj-http "0.1.3"]
                 ;;               [swank-clojure "1.3.0-SNAPSHOT"]
                 ;;[org.clojure/jvm.tools.analyzer "0.4.3"]
                 [org.clojure/jvm.tools.analyzer "0.6.1"]
                 [instaparse "1.3.2"]
                 ]
  :jvm-opts ["-Xmx1g"])

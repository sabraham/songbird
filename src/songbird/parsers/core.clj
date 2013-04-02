(ns songbird.parsers.core)

(defn tokenize-on-whitespace
  [s]
  (clojure.string/split s #"\s+"))
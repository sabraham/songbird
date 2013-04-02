(ns songbird.parsers.core-test
  (:use [clojure.test])
  (:require [songbird.parsers.core :as p]))

(deftest tokenize-on-whitespace-test
  (is (= ["hello" "world"]
         (p/tokenize-on-whitespace "hello    world"))))
(ns songbird.parsers.core-test
  (:use [clojure.test])
  (:require [songbird.parsers.core :as p]))

(deftest tokenize-on-whitespace-test
  (is (= ["hello" "world"]
         (p/tokenize-on-whitespace "hello    world"))))

(deftest not-url?-test
  (is (p/not-url? "this."))
  (is (not (p/not-url? "http://www.google.com"))))

(deftest remove-punctuation-test
  (is (= "this" (p/remove-punctuation "!this,")))
  (is (= "this-is" (p/remove-punctuation "!this-is"))))

(deftest split-mashed-words-test
  (is (= ["Virgin" "Atlantic"] (p/split-mashed-words "VirginAtlantic")))
  (is (= ["Virgin"] (p/split-mashed-words "Virgin")))
  (is (= ["virgin"] (p/split-mashed-words "virgin"))))

(deftest split-hyphens-test
  (is (= ["hi"] (p/split-hyphens "hi")))
  (is (= ["hi" "there"] (p/split-hyphens "hi-there"))))

;; https://twitter.com/richardbranson/status/318593943526854656

(deftest raw-text->clean-tokens-test
  (is (= ["thrilled" "to" "announce" "that" "virgin" "atlantic" "will" "launch" "the" "worlds" "first" "glass" "bottomed" "plane" "virgin" "glass" "plane"]
         (p/raw-text->clean-tokens
          "thrilled to announce that @VirginAtlantic will launch the world’s first glass-bottomed plane http://virg.in/gbp  #VirginGlassPlane"))))

(deftest tokens->meter-test
  (is (= [1 1 0 1 0]
         (p/tokens->meter ["thrilled" "to" "announce"]
                          {"thrilled" {:meter [1] :rhyme ["IH" "L" "D"]}
                           "to" {:meter [1] :rhyme ["T" "UW"]}
                           "announce" {:meter [0 1 0] :rhyme ["AW" "N" "S"]}}))))

(deftest tokens->rhyme-test
  (is (= ["AW" "N" "S"]
         (p/tokens->rhyme ["thrilled" "to" "announce"]
                          {"thrilled" {:meter [1] :rhyme ["IH" "L" "D"]}
                           "to" {:meter [1] :rhyme ["T" "UW"]}
                           "announce" {:meter [0 1 0] :rhyme ["AW" "N" "S"]}}))))
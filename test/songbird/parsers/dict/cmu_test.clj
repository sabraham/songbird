(ns songbird.parsers.dict.cmu-test
  (:require [songbird.parsers.dict.cmu :as c])
  (:use [clojure.test]))

(deftest phoneme->stress?-test
  (is (nil? (c/phoneme->stress? "P")))
  (is (= 1 (c/phoneme->stress? "P1"))))

(deftest phonemes->meter-test
  (is (= [0 1]
       (c/phonemes->meter ["P" "ER0" "EH1" "N"]))))

(deftest phonemes->rhyme-test
  (is (= ["EH" "N"]
       (c/phonemes->rhyme ["P" "ER0" "EH1" "N"]))))

(deftest parse-line-test
  (is (= {:token ")paren"
          :meter [0 1]
          :rhyme ["EH" "N"]} (c/parse-line ")PAREN  P ER0 EH1 N\n"))))

(deftest parse-lines-test
  (is (= {"(begin-parens" {:meter [0 1 0 1] :rhyme ["EH" "N" "Z"]}
          "(in-parentheses" {:meter [1 0 1 0 2] :rhyme ["IY" "Z"]}}
       (c/parse-lines [";;; header comment"
                       ";;; blah blah yoyoo"
                       "(BEGIN-PARENS  B IH0 G IH1 N P ER0 EH1 N Z"
                       "(IN-PARENTHESES  IH1 N P ER0 EH1 N TH AH0 S IY2 Z"
                       ";;; footer comment"]))))

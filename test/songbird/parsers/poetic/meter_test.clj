(ns songbird.parsers.poetic.meter-test
  (:use [clojure.test])
  (:require [songbird.parsers.poetic.meter :as m]))

(deftest unstressed?-test
  (is (= true (m/unstressed? 0)))
  (is (= false (m/unstressed? 1)))
  (is (= false (m/unstressed? 2))))

(deftest iamb?-test
  (is (m/iamb? [0 1]))
  (is (m/iamb? [0 2]))
  (is (not (m/iamb? [0])))
  (is (not (m/iamb? [1 0])))
  (is (not (m/iamb? [])))
  (is (not (m/iamb? [0 1 0])))
  (is (not (m/iamb? [0 1 1]))))

(deftest anapaest?-test
  (is (m/anapaest? [0 0 1]))
  (is (m/anapaest? [0 0 2]))
  (is (not (m/anapaest? [0 0 0])))
  (is (not (m/anapaest? [0 1 0])))
  (is (not (m/anapaest? [0 0 1 0])))
  (is (not (m/anapaest? [0 0 1 2])))
  (is (not (m/anapaest? [0])))
  (is (not (m/anapaest? [1 0])))
  (is (not (m/anapaest? []))))
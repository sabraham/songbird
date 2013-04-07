(ns songbird.parsers.poetic.meter-test
  (:use [clojure.test])
  (:require [songbird.parsers.poetic.meter :as m])
  (:import [songbird.parsers.poetic.meter Foot]))

(deftest unstressed?-test
  (is (= true (m/unstressed? 0)))
  (is (= false (m/unstressed? 1)))
  (is (= false (m/unstressed? 2))))

(deftest Foot-test
  (is (m/count-syllables (new Foot [1 0])) 2)
  (is (m/count-syllables (new Foot [1 0 1])) 3)
  (is (m/=foot? (new Foot [1 0 1]) [1 0 1]))
  (is (m/=foot? (new Foot [1 0 1]) [1 0 2]))
  (is (not (m/=foot? (new Foot [1 0 1]) [1 0 2 0]))))

(deftest meter?-factory-test
  (is (m/meter?-factory 2 [m/iamb] [0 1 0 1]))
  (is (m/meter?-factory 2 [m/iamb m/trochee] [0 1 1 0]))
  (is (m/meter?-factory 3 [m/iamb m/trochee] [0 1 1 0 0 1]))
  (is (not (m/meter?-factory 3 [m/iamb m/trochee] [0 1 1 0 1 0]))))
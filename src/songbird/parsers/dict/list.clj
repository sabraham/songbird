(ns songbird.parsers.dict.cmu
  (:require [songbird.parsers.dict.cmu :as cmu]))

(def CMU-DICT
  (cmu/read-dict cmu/CMU-DICT-PATH))
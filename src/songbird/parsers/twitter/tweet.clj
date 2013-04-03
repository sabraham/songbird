(ns songbird.parsers.twitter.tweet
  (:require [cheshire.core :as json]
            [plumbing.core :as plumbing]))

(def MINIMAL-MAP-STRUCTURE
  [["text"]
   ["user" "screen_name"]
   ["id"]])

(defn payload->json
  [payload]
  (json/decode payload))

(defn json->minimal-map
  [json map-structure]
  (loop [ks map-structure
         state {}]
    (if-let [k (first ks)]
      (recur (rest ks)
             (assoc state (last k) (get-in json k)))
    state)))

(defn payload->keyworded-minimal-map
  [payload map-structure]
  (-> payload
      payload->json
      (json->minimal-map map-structure)
      plumbing/keywordize-map))

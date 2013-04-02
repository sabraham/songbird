(ns songbird.processor
  (:require [songbird.parsers.core :as parsers])
  (:import [org.apache.commons.validator.routines UrlValidator]))

;; deal with urls
(def url-validator
  (UrlValidator.))

(defn not-url?
  [token]
  (not (.isValid url-validator token)))

;; punctuation
(defn remove-punctuation
  "Remove all non alphanumeric characters from word. Dropping apostrophe is
   cool, as we only care about pronunciation. Dropping hyphens is not ok"
  [token]
  (clojure.string/replace token #"[^\w\-]+" ""))

(defn mashed-delimiter?
  [char]
  (Character/isUpperCase char))

(defn split-mashed-words
  "(split-mashed-words \"VirginAtlantic\") => (\"Virgin\" \"Atlantic\")"
  [word]
  (loop [prefix nil
         suffix (seq word)
         words []]
    (if-let [letter (first suffix)]
      (if (mashed-delimiter? letter)
        (recur (str letter)
               (rest suffix)
               (conj words prefix))
        (recur (str prefix letter)
               (rest suffix)
               words))
      (filter identity (conj words prefix)))))

(defn split-hyphens
  [word]
  (clojure.string/split word #"-" ))

(defn raw-text->clean-tokens
  "pretty inefficient :("
  [raw-text]
  (->> raw-text
      parsers/tokenize-on-whitespace
      (filter not-url?)
      (map remove-punctuation)
      (map split-mashed-words)
      flatten
      (map split-hyphens)
      flatten
      (map clojure.string/lower-case)))

(defn tokens->meter
  [tokens dict]
  (->> tokens
       (map #(get-in dict [% :meter]))
       flatten))

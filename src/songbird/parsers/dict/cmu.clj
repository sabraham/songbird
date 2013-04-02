(ns songbird.parsers.dict.cmu
  (require [songbird.parsers.core :as parsers]))

(def CMU-DICT-PATH
  (.getFile (clojure.java.io/resource "dicts/cmu.txt")))

(defn phoneme->stress?
  [phoneme]
  (when-let [stress (re-find #"[012]" phoneme)]
    (Integer/parseInt stress)))

(defn phonemes->meter
  [phonemes]
  (filter identity (map phoneme->stress? phonemes)))

(defn phoneme->phoneme-wo-stress
  [phoneme]
  (let [len (.length phoneme)]
    (.substring phoneme 0 (- len 1))))

(defn phonemes->rhyme
  ;; TODO better alg is to reverse phonemes, and greadily consume -- then can
  ;; more easily get things like "Alexander" which now get rhyme er rather than
  ;; d er.
  [phonemes]
  (loop [rhyme []
         phonemes phonemes]
    (let [phoneme (first phonemes)]
      (if phoneme
        (if (phoneme->stress? phoneme)
          (recur [(phoneme->phoneme-wo-stress phoneme)]
                 (rest phonemes))
          (recur (conj rhyme phoneme)
                 (rest phonemes)))
        rhyme))))

(defn commented-line?
  [line]
  (= (.substring line 0 3) ";;;"))

(defn parse-line
  [line]
  (when (not (commented-line? line))
    (let [tokens (parsers/tokenize-on-whitespace line)
          [token phonemes] [(clojure.string/lower-case (first tokens))
                            (rest tokens)]
          meter (phonemes->meter phonemes)
          rhyme (phonemes->rhyme phonemes)]
      {:token token :meter meter :rhyme rhyme})))

(defn parse-lines
  [lines]
  (loop [lines lines
         state {}]
    (if-let [line (first lines)]
      (if-let [parsed-line (parse-line line)]
        (let [key (:token parsed-line)
              value (dissoc parsed-line :token)]
          (recur (rest lines)
                 (assoc state key value)))
        ;; skip commented line
        (recur (rest lines)
               state))
      state)))

(defn read-dict
  [path]
  (let [lines (clojure.string/split (slurp path) #"\n")]
    (parse-lines lines)))

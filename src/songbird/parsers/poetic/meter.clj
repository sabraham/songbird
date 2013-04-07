(ns songbird.parsers.poetic.meter)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; units
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn unstressed?
  [syllable]
  (when syllable
    (= 0 syllable)))

(defn stressed?
  [syllable]
  (when syllable
    (not (unstressed? syllable))))

(defprotocol FootProtocol
  (count-syllables [this])
  (=foot? [this test-meter]))

(extend-type nil
  FootProtocol
  (count-syllables [this] 0)
  (=foot? [this test-meter] false))

(defrecord Foot [meter]
  FootProtocol
  (count-syllables [this] (count (:meter this)))
  (=foot? [this test-meter]
    (if (= (count-syllables this) (count test-meter))
      (loop [this this
             test-meter test-meter
             state true]
        (let [stress (first (:meter this))]
          (if (and stress state)
            (if (or (and (unstressed? stress) (unstressed? (first test-meter)))
                    (and (stressed?   stress) (stressed?   (first test-meter))))
              (recur (Foot. (rest (:meter this))) (rest test-meter) true)
              (recur (Foot. (rest (:meter this))) (rest test-meter) false))
            state)))
      false)))

(defn- symbols->foot
  [symbols]
  (let [symbol-map {\* 0 \- 1}]
    (map symbol-map (seq symbols))))

(defn symbols->Foot
  [symbols]
  (->> symbols symbols->foot (new Foot)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; disyllables
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def dibrach (symbols->Foot "**"))
(def iamb    (symbols->Foot "*-"))
(def trochee (symbols->Foot "-*"))
(def spondee (symbols->Foot "--"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; trisyllables
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def tribach      (symbols->Foot "***"))
(def dactyl       (symbols->Foot "-**"))
(def amphibrach   (symbols->Foot "*-*"))
(def anapaest     (symbols->Foot "**-"))
(def bacchius     (symbols->Foot "*--"))
(def cretic       (symbols->Foot "-*-"))
(def antibacchius (symbols->Foot "--*"))
(def molossus     (symbols->Foot "---"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; meter
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn meter?-factory
  [length feet meter]
  (let [feet (take length (cycle feet))]
    (loop [feet feet
           meter meter
           state true]
      (let [foot (first feet)
            num-syllables (count-syllables foot)]
        (if (and foot state)
          (if (=foot? foot (take num-syllables meter))
            (recur (rest feet) (drop num-syllables meter) true)
            (recur (rest feet) (drop num-syllables meter) false))
          state)))))

(defn dimeter?
  [feet meter]
  (meter?-factory 2 feet meter))

(defn trimeter?
  [feet meter]
  (meter?-factory 3 feet meter))

(defn tetrameter?
  [feet meter]
  (meter?-factory 4 feet meter))

(defn pentameter?
  [feet meter]
  (meter?-factory 5 feet meter))

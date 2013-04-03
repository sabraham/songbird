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

(defn- factory
  [pattern meter]
  (if (= (.length pattern) (count meter))
    (loop [pattern pattern
           meter meter
           state true]
      (let [p (first pattern)]
        (if (and p state)
          (if ((if (= p \*) unstressed? stressed?) (first meter))
            (recur (rest pattern) (rest meter) true)
            (recur (rest pattern) (rest meter) false))
          state)))
    false))

(factory "**" [0 0 1])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; disyllables
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn dibrach? [foot] (factory "**" foot))

(defn iamb?    [foot] (factory "*-" foot))

(defn trochee? [foot] (factory "-*" foot))

(defn spondee? [foot] (factory "--" foot))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ;; trisyllables
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn tribrach?     [syllables] (factory "***" syllables))

(defn dactyl?       [syllables] (factory "-**" syllables))

(defn amphibrach?   [syllables] (factory "*-*" syllables))

(defn anapaest?     [syllables] (factory "**-" syllables))

(defn bacchius?     [syllables] (factory "*--" syllables))

(defn cretic?       [syllables] (factory "-*-" syllables))

(defn antibacchius? [syllables] (factory "--*" syllables))

(defn molossus?     [syllables] (factory "---" syllables))


(ns octant.views.styles
  (:require [cljss.reagent :refer-macros [defstyled]]
            [cljss.core :refer-macros [defstyles inject-global]]
            [clojure.string :as string]))

(def base-font-px 16)
(def root-font-pct 62.5)
(def root-font-px (* base-font-px (/ root-font-pct 100)))
(def px-rm-ratio (/ 1 root-font-px))

(defn *rm
  "given a `px` value, will convert it to rem
  with the proper base font conversion"
  [px]
  (str (* px-rm-ratio px) "rem"))

(defn class-names [& names]
  (string/join " " names))

;; TODO: add pixel/rm conversion here
(defn style-val [& props]
  (string/join " " props))

(defn fn-wrapper [name body]
  (str name "(" body ")"))

(defn rgba [r g b a]
  (let [rgb?a (concat [r g b] (when a [a] []))]
    (fn-wrapper "rgba" (string/join ", " rgb?a))))

(defn calc [l op r] (fn-wrapper "calc" (string/join " " [l op r])))

;;Not currently needed for using bulma
;;TODO: investigate if this is needed in an actual bulma build
(defn add-global! []
  (inject-global
   {:body
    {:font-size (str root-font-pct "%")}}))

(defstyles todo-list [] {:margin-top (*rm 100)})

(defstyles banner []
  {:position "fixed"
   :top 0
   :left 0
   :right 0
   :box-shadow (style-val 0 (*rm 10) (*rm 10) (*rm -5) (rgba 146 209 198 0.6))})

(let [width (*rm 250)
      height (*rm 70)]
  (defstyled popup :div
    {:position "absolute"
     :float "left"
     :z-index 100
     :width width
     :height height
     :left (with-meta #(calc (*rm %) "-" width) :x)
     :top (with-meta #(*rm %) :y)}))

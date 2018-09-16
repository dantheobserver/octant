(ns octant.views.styles
  (:require [cljss.reagent :refer-macros [defstyled]]
            [cljss.core :refer-macros [defstyles inject-global]]))

(def base-font-px 16)
(def root-font-pct 62.5)
(def root-font-px (* base-font-px (/ root-font-pct 100)))
(def px-rm-ratio (/ 1 root-font-px))

(defn class-names [& names]
  (clojure.string/join " " names))

;; TODO: add pixel/rm conversion here
(defn style-val [& props]
  (clojure.string/join " " props))

(defn *rm
  "given a `px` value, will convert it to rem
  with the proper base font conversion"
  [px]
  (str (* px-rm-ratio px) "rem"))

;;Not currently needed for using bulma
;;TODO: investigate if this is needed in an actual bulma build
(defn add-global! []
  (inject-global
   {:body
    {:font-size (str root-font-pct "%")}}))

(defstyled popup :div
  {:position "relative"
   :width (*rm 450)
   :height (*rm 25)
   :left (with-meta #(*rm %) :x)
   :top (with-meta #(*rm %) :y)})

(defstyles banner []
  {:position "fixed"
   :top 0
   :left 0
   :right 0
   :box-shadow (style-val 0 (*rm 10) (*rm 10) (*rm -5) "rgba(146, 209, 198, 0.6)")})

(defstyles todo-list [] {:margin-top (*rm 100)})

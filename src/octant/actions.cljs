(ns octant.actions
  (:require [reagent.core :as r]
            [octant.state.core :refer [app-state]]
            [octant.state.selectors :as sel]))

;; Actions
(defn add-task! [{:keys [id] :as task}]
  (when-not (sel/task id)
    (swap! app-state assoc :todos task)))

(defn set-active-task! [id]
  (let [active-task (sel/active-task)
        active-id (get active-task :id)]
    (when-not (= active-id id)
      (do
        (swap! app-state update-in [:todos active-id] dissoc :active?)
        (swap! app-state assoc-in [:todos id :active?] true)))))

;; Popup
(defn- set-popup-visibility [visible]
  (swap! app-state assoc-in [:hover-popup :visible?] visible))

(defn show-popup [] (set-popup-visibility true))
(defn hide-popup [] (set-popup-visibility false))
(defn- coord-set-fn [x y a]
  (-> a
      (assoc-in [:hover-popup :x] x)
      (assoc-in [:hover-popup :y] y)))

(defn set-popup-loc [x y]
  (swap! app-state (partial coord-set-fn x y)))

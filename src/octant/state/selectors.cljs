(ns octant.state.selectors
  (:require [reagent.core :as r]
            [octant.state.core :refer [app-state]]))

;; State selectors
(defn todos [] (:todos @app-state))

(defn task-ids []
  (-> @(r/track todos)
      keys
      sort))

(defn task [id]
  (-> @(r/track todos)
      (get id)))

(defn todos-seq [] (-> @(r/track todos)
                       vals))

(defn active-task []
  (->> @(r/track todos-seq)
      (filter #(:active? %))
      first))

;; Popup
(defn hover-popup [] (:hover-popup @app-state))

(defn hover-visible []
  (-> @(r/track hover-popup)
      (get :visible)))

(defn hover-coordinates []
  (-> @(r/track hover-popup)
      (get :coordinates)))

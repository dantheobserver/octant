(ns octant.state.selectors
  (:require [octant.state.store :refer [app-state]]))

;; State selectors
(defn todos [] (:todos @app-state))

(defn todos-seq [] (vals (todos)))

(defn task-by-id [id]
  (first (filter #(= (:id %) id) (todos))))

(defn active-task []
  (first (filter #(:active %) (todos-seq))))

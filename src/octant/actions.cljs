(ns octant.actions
  (:require [octant.state.store :refer [app-state]]
            [octant.state.selectors :as sel]))

;; Actions
(defn add-task! [{:keys [id] :as task}]
  (when-not (sel/task-by-id id)
    (swap! app-state assoc :todos task)))

(defn set-active-task! [id]
  (let [active-id (get (sel/active-task) :id)]
    (when-not (= active-id id)
      (swap! app-state update-in [:todos active-id] dissoc :active)
      (swap! app-state assoc-in [:todos id :active] true))))

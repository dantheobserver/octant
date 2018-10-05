(ns octant.state.update-handler
  (:require [octant.state.selectors :as sel]
            [octant.state.core :refer [app-state]]))

(defn process-updates
  [{:keys [type data]}]
  ;; Do some type validation here to prevent malformed data
  (condp = type
    :task/add (when-not (sel/task data)
                (swap! app-state assoc-in [:todos] data))
    :task/delete (swap! app-state update-in [:todos] dissoc data)
    :task/set-active (swap! app-state assoc-in [:todos data :active?] true)
    :task/unset-active (swap! app-state assoc-in [:todos data :active?] false)

    :popup/show (swap! app-state assoc-in [:hover-popup :visible? true])
    :popup/hide (swap! app-state assoc-in [:hover-popup :visible? false])
    :popup/move (let [[x y] data]
                  (swap! app-state update :hover-popup merge {:x x :y y}))))

(ns octant.state.actions
  (:require [reagent.core :as r]
            [octant.state.core :refer [app-state]]
            [octant.state.selectors :as sel]
            [octant.state.update-handler :as handler]))

(defn emit-action [type-key data]
  (handler/process-updates {:type type-key :data data}))

;; TODO: Validate incoming objects with schema/spec/whatever
;; Tasks
(defn delete-task [id] (emit-action :task/delete id))
(defn unset-active-task [id] (emit-action :task/unset-active id))
(defn set-active-task [id]
  (when-let [active-task (sel/active-task)]
    (unset-active-task (:id active-task)))
  (emit-action :task/set-active id))

;; Popup
(defn show-popup [] (emit-action :popup/show nil))
(defn hide-popup [] (emit-action :popup/hide nil))
(defn move-popup [x y] (emit-action :popup/move [x y]))

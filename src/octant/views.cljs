(ns octant.views
  (:require [reagent.core :as r :refer [atom]]))

(def app-state (atom {:todos {0 {:id 0 :task "Take out the trash in the morning" :active true}
                              1 {:id 1 :task "visit the Museum of Modern art"}
                              2 {:id 2 :task "Take the train to post office"}
                              3 {:id 3 :task "Research lessons on Reagent"}
                              4 {:id 4 :task "get food at River market for dinner"}}}))

;; State selectors
(defn todos [] (:todos @app-state))

(defn todos-seq [] (vals (todos)))

(defn task-by-id [id]
  (first (filter #(= (:id %) id) (todos))))

(defn active-task []
  (first (filter #(:active %) (todos-seq))))

;; Actions
(defn add-task! [{:keys [id] :as task}]
  (when-not (task-by-id id)
    (swap! app-state assoc :todos task)))

(defn set-active-task! [id]
  (let [active-id (get (active-task) :id)]
    (when-not (= active-id id)
      (swap! app-state update-in [:todos active-id] dissoc :active)
      (swap! app-state assoc-in [:todos id :active] true)))
  (js/console.log (clj->js (todos-seq))))

;; Views
(defn icon [name props]
  [:ion-icon (merge props {:name name})])

(defn banner []
  [:section.hero.is-primary
   [:div.hero-body
    (if-let [task (active-task)]
      [:div
       [:h1.title "Current Task"]
       [:h2.subtitle.is-centered (:task task)]]
      [:h1.title "No current tasks"])]])

(defn app []
  [:div
   [banner]
   [:section.section
    (for [{:keys [task id active]} (todos-seq)]
      ^{:key id}
      [:div.box (merge
                 {:on-click #(set-active-task! id)}
                 (when active {:class "has-background-light"}))
       [:section.section
        [:span task]]])]])

(ns octant.views
  (:require [reagent.core :as r :refer [atom]]
            [clojure.string :as string]))

(def app-state (atom {:todos {0 {:id 0 :task "Take out the trash in the morning" :active true}
                              1 {:id 1 :task "visit the Museum of Modern art"}
                              2 {:id 2 :task "Take the train to post office"}
                              3 {:id 3 :task "Research lessons on Reagent"}
                              4 {:id 4 :task "get food at River market for   dinner"}}}))

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

(defn hover-text [text]
  (let [hover-idx (atom nil)
        sel-idxs (atom #{})
        set-hover (fn [i]
                    (when-not (get @sel-idxs i)
                      (reset! hover-idx i)))
        toggle-select (fn [i]
                        (if (get @sel-idxs i)
                          (do
                            (swap! sel-idxs disj i)
                            (set-hover i))
                          (do
                            (swap! sel-idxs conj i)
                            (set-hover nil))))]
    (fn [text]
      [:span
       (doall
        (map-indexed (fn [i word]
                       ^{:key i}
                       [:span {:on-mouse-over #(set-hover i)
                               :on-click #(do
                                            (toggle-select i)
                                            (.stopPropagation %))
                               :class (when (get @sel-idxs i) "tag is-info")}
                        [:span
                         (when (= @hover-idx i) {:class "tag is-primary"})
                         word]
                        [:span " "]])
                     (string/split text #"\s+")))])))

(defn list-item [{:keys [task id active]}]
  [:div.box (merge
             {:on-click #(set-active-task! id)}
             (when active {:class "has-background-light"}))
   [:section.section
    [hover-text task]]])

(defn app []
  [:div
   [banner]
   [:section.section
    (for [task (todos-seq)]
      ^{:key (:id task)}
      [list-item task])]])

(ns octant.views.core
  (:require [reagent.core :as r]
            [clojure.string :as string]
            [octant.state.selectors :as sel]
            [octant.actions :as actions]
            [octant.state.store :refer [app-state]]
            [octant.views.styles :as styles :refer [class-names]]))

;; Views
(defn icon [name props]
  [:ion-icon (merge props {:name name})])

(defn banner []
  [:section {:class (class-names (styles/banner) "hero" "is-primary" )}
   [:div.hero-body
    (if-let [task (sel/active-task)]
      [:div
       [:h1.title "Current Task"]
        [:h2.subtitle.is-centered (:task task)]]
      [:h1.title "No current tasks"])]])

(defn hover-text
  "Creates a string made up of elements from `text`
  by word.  On hover it will decorate the element as a tag.
  Clicking will make the item a tagged item."
  [text]
  (let [state (r/atom {:hover-idx nil
                     :sel-idxs #{}})
        set-hover (fn [i]
                    (when-not (get-in @state [:sel-idxs i])
                      (swap! state assoc :hover-idx i)))
        toggle-select (fn [i]
                        (if (get-in @state [:sel-idxs i])
                          (do ;deselect current word
                            (swap! state update :sel-idxs disj i)
                            (set-hover i))
                          (do ;select current word
                            (swap! state update :sel-idxs conj i)
                            (set-hover nil))))]
    (fn [text]
      [:span
       (doall
        (map-indexed (fn [i word]
                       ^{:key i}
                       [:span {:on-mouse-enter #(set-hover i)
                               :on-mouse-leave #(set-hover nil)
                               :on-click #(do
                                            (toggle-select i)
                                            (.stopPropagation %))
                               :class (when (get-in @state [:sel-idxs i]) "tag is-info")}
                        [:span
                         (when (= (:hover-idx @state) i) {:class "tag is-primary"})
                         word]
                        [:span " "]])
                     (string/split text #"\s+")))])))

(defn list-item [{:keys [task id active]}]
  [:div.box (merge
             {:on-click #(actions/set-active-task! id)
              :class (when active "has-background-light")})
   [:section.section
    [hover-text task]]])

(defn todo-list []
  [:div {:class (styles/todo-list)}
   [:section.section
    (for [task (sel/todos-seq)]
      ^{:key (:id task)}
      [list-item task])]])

(defn popup-selector []
  (styles/popup {:x 20 :y 20 }
                [:div
                 :span "Testing"]))

(ns octant.views.todos
  (:require [reagent.core :as r]
            [clojure.string :as str]
            [octant.state.selectors :as sel]
            [octant.state.actions :as actions]
            [octant.views.styles :as styles]))

(def classes #(str/join " " %&))

;; Views
(defn icon [name props]
  [:ion-icon (merge props {:name name})])

(defn banner []
  [:section {:class (classes (styles/banner) "hero" "is-primary")}
   [:div.hero-body
    (if-let [task (sel/active-task)]
      [:div
       [:h1.title "Current Task"]
        [:h2.subtitle.is-centered (:task task)]]
      [:h1.title "No current tasks"])]])

(defn get-target-coords [evt]
  (let [target (aget evt "target")
        y (.-offsetTop target)
        rect (.getBoundingClientRect target)]
    [(.-left rect) y]))

(def elem-scroll-height (memoize (fn [sel]
                                   (if-let [elem (js/document.querySelector sel)]
                                     (.-scrollHeight elem)))))

(defn hover-text
  "Creates a string made up of elements from `text`
  by word.  On hover it will decorate the element as a tag.
  Clicking will make the item a tagged item."
  [text]
  (let [state (r/atom {:hover-idx nil
                       :sel-idxs #{}})
        {:keys [hover-idx sel-idxs]} @state
        set-hover #(when-not (get sel-idxs %)
                     (swap! state assoc :hover-idx %))
        toggle-select #(if (get-in @state [:sel-idxs %])
                        (do ;deselect current word
                          (swap! state update :sel-idxs disj %)
                          (set-hover %))
                        (do ;select current word
                          (swap! state update :sel-idxs conj %)
                          (set-hover nil)))]
    (fn [text]
      [:span
       (doall
        (map-indexed (fn [i word]
                       ^{:key i}
                       [:span {:on-mouse-enter (fn [e]
                                                 (let [[x y] (get-target-coords e)]
                                                   (actions/move-popup x y)
                                                   #(set-hover i)))
                               :on-mouse-leave #(set-hover nil)
                               :on-click #(do
                                            (toggle-select i)
                                            (.stopPropagation %))
                               :class (when (get-in @state [:sel-idxs i]) "tag is-info")}
                        [:span
                         (when (= (:hover-idx @state) i) {:class "tag is-primary"})
                         word]
                        [:span " "]])
                     (str/split text #"\s+")))])))

(defn list-item [id]
  (let [{:keys [active? task]} @(r/track sel/task id)]
    [:div.box (merge
               {:on-click #(actions/set-active-task id)
                :class (classes "todo-list__list-item"
                                (when active? "has-background-light"))})
     [:section.section
      [hover-text task]]]))

(defn todo-list []
  (let [task-ids @(r/track sel/task-ids)]
    [:div {:class (classes "todo-list" (styles/todo-list))}
     [:section.section
      (for [id task-ids]
        ^{:key id}
        [list-item id])]]))

(defn hover-popup []
  (let [{:keys [x y visible?]} @(r/track sel/hover-popup)]
    (styles/popup {:x x :y y :class (classes "hover-popup" "box")}
                  [:div.columns
                   [:div.column "test"]
                   [:div.column "test"]
                   [:div.column "test"]])))

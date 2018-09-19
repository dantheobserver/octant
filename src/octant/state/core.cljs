(ns octant.state.core
  (:require [reagent.core :as r]
            [schema.core :as s]))

(def tag-list [s/Str])

(def task-schema
  {:id (s/cond-pre s/Str s/Int)
   :task s/Str
   :active? (s/maybe s/Bool)
   :tagged (s/maybe tag-list)})

(defonce app-state (r/atom {:todos {0 {:id 0 :task "Take out the trash in the morning!" :active? true}
                                    1 {:id 1 :task "visit the Museum of Modern art"}
                                    2 {:id 2 :task "Take the train to post office"}
                                    3 {:id 3 :task "Research lessons on Reagent"}
                                    4 {:id 4 :task "get food at River market for   dinner"}}
                            :hover-popup {:visible? false :x 0 :y 0}}))

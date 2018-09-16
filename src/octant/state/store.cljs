(ns octant.state.store
  (:require [reagent.core :as r]))

(defonce app-state (r/atom {:todos {0 {:id 0 :task "Take out the trash in the morning" :active true}
                                    1 {:id 1 :task "visit the Museum of Modern art"}
                                    2 {:id 2 :task "Take the train to post office"}
                                    3 {:id 3 :task "Research lessons on Reagent"}
                                    4 {:id 4 :task "get food at River market for   dinner"}}}))

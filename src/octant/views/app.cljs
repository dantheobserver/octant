(ns octant.views.app
  (:require [reagent.core :as reagent]
            [octant.views.core :as views]))

(defn app []
  [:div.app
   [views/banner]
   [views/todo-list]])

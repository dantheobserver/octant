(ns octant.views.core
  (:require [reagent.core :as reagent]
            [octant.views.todos :as todos]))

(defn app []
  [:div.app
   [todos/hover-popup]
   [todos/banner]
   [todos/todo-list]])

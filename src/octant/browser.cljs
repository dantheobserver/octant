(ns octant.browser
  (:require ["react" :as react]
            [reagent.core :as r]
            [goog.object :as obj]
            [octant.views.app :refer [app]]
            [cljss.core :as css]))

;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (css/remove-styles!)
  (r/render [app] (.querySelector js/document "#app"))
  (js/console.log "start"))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (obj/set js/window "React" react)
  (js/console.log "init")
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))

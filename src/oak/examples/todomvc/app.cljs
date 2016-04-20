(ns oak.examples.todomvc.app
  (:require
    [mount.core :as mount :include-macros true]
    [oak.render :as oak-render]
    [oak.examples.todomvc.cs.TodoApp :as TodoApp]))

; -----------------------------------------------------------------------------
; Initialization

(def initial-state {:memory {} :order []})
(def initial-cache nil)

; -----------------------------------------------------------------------------
; State variables

(defonce state (atom initial-state))
(defonce cache (atom initial-cache))

; -----------------------------------------------------------------------------
; Runtime state

(declare app)
(mount/defstate app

  :start
  (oak-render/render
    TodoApp/root
    :target (.getElementById js/document "app")
    :state-atom state
    :cache-atom cache)

  :stop
  (let [stop! (:stop! @app)]
    (stop!)))

; -----------------------------------------------------------------------------
; Interface

(defn ^:export reset-state [] (reset! state initial-state))
(defn ^:export reset-oracle [] (reset! cache initial-cache))

(defn ^:export get-state [] (clj->js @state))
(defn ^:export get-oracle [] (clj->js @cache))

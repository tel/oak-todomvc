(ns oak.examples.todomvc.app
  (:require
    [mount.core :as mount :include-macros true]
    [oak.render :as oak-render]
    [oak.examples.todomvc.cs.TodoApp :as TodoApp]))

; -----------------------------------------------------------------------------
; Initialization

(def initial-state {:todos {:memory {} :order []}})
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

(defn ^:export resetState []
  (reset! state initial-state)
  ((:request-render! @app)))

(defn ^:export resetOracle []
  (reset! cache initial-cache)
  ((:request-render! @app)))

(defn ^:export getState [] (clj->js @state))
(defn ^:export getOracle [] (clj->js @cache))

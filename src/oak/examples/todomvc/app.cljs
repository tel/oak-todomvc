(ns oak.examples.todomvc.app
  (:require
    [mount.core :as mount :include-macros true]
    [oak.render :as oak-render]
    [oak.examples.todomvc.cs.TodoApp :as TodoApp]
    [cljs.core.async :as async]))

; -----------------------------------------------------------------------------
; Initialization

(def initial-model {:todos {:memory {} :order []}})
(def initial-cache nil)

; -----------------------------------------------------------------------------
; State atoms

(defonce model (atom initial-model))
(defonce cache (atom initial-cache))

; -----------------------------------------------------------------------------
; Intention

(defonce intent (async/chan))

; -----------------------------------------------------------------------------
; Runtime model

(declare app)
(mount/defstate app

  :start
  (oak-render/render
    TodoApp/root
    :target (.getElementById js/document "app")
    :model-atom model
    :cache-atom cache
    :intent intent)

  :stop
  (let [stop! (:stop! @app)]
    (stop!)))

; -----------------------------------------------------------------------------
; Interface

(defn ^:export submitOracle [action]
  (async/put! intent [:oracle action]))

(defn ^:export resetModel []
  (reset! model initial-model)
  ((:request-render! @app)))

(defn ^:export resetOracle []
  (reset! cache initial-cache)
  ((:request-render! @app)))

(defn ^:export getModel [] (clj->js @model))
(defn ^:export getOracle [] (clj->js @cache))

(ns oak.examples.todomvc.app
  (:require
    [mount.core :as mount :include-macros true]
    [oak.render :as oak-render]
    [oak.examples.todomvc.cs.TodoApp :as TodoApp]))

; -----------------------------------------------------------------------------
; Initialization

(def initial-model {:todos {:memory {} :order []}})
(def initial-cache nil)

; -----------------------------------------------------------------------------
; State atoms

(defonce model (atom initial-model))
(defonce cache (atom initial-cache))

; -----------------------------------------------------------------------------
; Runtime model

(declare app)
(mount/defstate app

  :start
  (oak-render/render
    TodoApp/root
    :target (.getElementById js/document "app")
    :model-atom model
    :cache-atom cache)

  :stop
  (let [stop! (:stop! @app)]
    (stop!)))

; -----------------------------------------------------------------------------
; Interface

(defn ^:export resetModel []
  (reset! model initial-model)
  ((:request-render! @app)))

(defn ^:export resetOracle []
  (reset! cache initial-cache)
  ((:request-render! @app)))

(defn ^:export getModel [] (clj->js @model))
(defn ^:export getOracle [] (clj->js @cache))

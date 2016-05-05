(ns oak.examples.todomvc.app
  (:require
    [mount.core :as mount :include-macros true]
    [oak.render :as oak-render]
    [oak.examples.todomvc.cs.TodoApp :as TodoApp]
    [oak.examples.todomvc.nav :as nav]
    [oak.oracle.atom-listener :as atom-listener]
    [cljs.core.async :as async]
    [oak.examples.todomvc.routes :as routes]
    [cljs.core.match :refer-macros [match]]
    [oak.oracle :as oracle]
    [oak.oracle.higher-order :as oracle-ho]))

; -----------------------------------------------------------------------------
; Initialization

(def initial-model {:todos {:memory {} :order []}})
(def initial-cache {:navigation {:handler :show-all}})

; -----------------------------------------------------------------------------
; State atoms

(defonce model (atom initial-model))
(defonce cache (atom initial-cache))

; -----------------------------------------------------------------------------
; Intention

(defonce intent (async/chan))

; -----------------------------------------------------------------------------
; Oracle

;(def NavSchema
;  {:handler s/Keyword
;   (s/optional-key :route-params) {s/Keyword s/Any}})
;
;(def o-navigation
;  (oracle/make
;    :model NavSchema
;    :action (os/cond-pair [:landing NavSchema])
;    :query (s/enum :get)
;    :step (fn [action _model]
;            (match action
;              [:landing new-nav] new-nav))
;    :respond (fn [model q]
;               (match q
;                 :get (:handler model)))))

(def oracle
  (oracle-ho/parallel
    {:navigation (atom-listener/oracle nav/locations)}))

; -----------------------------------------------------------------------------
; Runtime model

(declare app)
(mount/defstate app

  :start 10
  #_(oak-render/render
    TodoApp/root
    :oracle oracle
    :target (.getElementById js/document "app")
    :model-atom model
    :cache-atom cache
    :intent intent)

  :stop (+ 2 2)
  #_(let [stop! (:stop! @app)]
    (stop!)))
(oak-render/render
    TodoApp/root
    :oracle oracle
    :target (.getElementById js/document "app")
    :model-atom model
    :cache-atom cache
    :intent intent)
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

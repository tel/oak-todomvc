(ns oak.examples.todomvc.nav
  (:require
    [goog.events :as events]
    [goog.history.EventType :as EventType]
    [mount.core :as mount :include-macros true]
    [oak.examples.todomvc.routes :as routes])
  (:import
    goog.history.Event
    goog.history.Html5History
    goog.Uri))

(defn start
  "Starts a navigation listener."
  [on-token-change]
  (let [on-navigate-event (fn on-navigate-event [ev]
                            (on-token-change (.-token ev)))
        history (doto (Html5History.)
                  (.setUseFragment true)
                  (.setPathPrefix "")
                  (events/listen EventType/NAVIGATE on-navigate-event)
                  (.setEnabled true))]
    {:history history
     :alive?  (fn [] (not (.isDisposed history)))
     :stop!   (fn []
                (.setEnabled history false)
                (.dispose history))}))

(defonce locations (atom {:current nil}))

(declare nav)
(mount/defstate nav
  :start (start (fn [token]
                  (swap! locations
                         (fn [past-state]
                           (-> past-state
                               (assoc :current (routes/match token)))))))
  :stop ((:stop! @nav)))


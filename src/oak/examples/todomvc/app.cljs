(ns oak.examples.todomvc.app
  (:require
    [mount.core :as mount :include-macros true]
    [oak.render :as oak-render]
    [oak.examples.todomvc.cs.TodoApp :as TodoApp]))

(declare app)
(mount/defstate app
  :start
  (oak-render/render
    TodoApp/root
    :target (.getElementById js/document "app")
    :on-event (fn on-event [target ev]
                (println target "<---" ev))
    :initial-state {:Header      nil
                    :MainSection {:memory {"a" {:completed false
                                                :editing   false
                                                :text      "Hi"}}
                                  :order  ["a"]}
                    :Footer      nil})
  :stop
  (let [stop! (:stop! @app)]
    (stop!)))


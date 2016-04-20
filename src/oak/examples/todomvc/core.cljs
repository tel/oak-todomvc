(ns oak.examples.todomvc.core
  (:require
    [oak.examples.todomvc.app]
    [mount.core :as mount]))

(enable-console-print!)

(defn main []
  (mount/start))

(defn reload []
  (mount/stop)
  (main))


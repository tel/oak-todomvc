(ns oak.examples.todomvc.core
  (:require
    [oak.examples.todomvc.app]
    [mount.core :as mount]))

(enable-console-print!)

(defn ^:export start [] (mount/start))
(defn ^:export stop [] (mount/stop))
(defn ^:export reload [] (stop) (start))

(defn ^:export main [] (start))



(ns oak.examples.todomvc.routes
  (:require
    [bidi.bidi :as bidi]))

(def routes
  ["/" {""          :show-all
        "active"    :show-active
        "completed" :show-completed}])

(defn match [str]
  (bidi/match-route routes str))

(defn path-for [location & args]
  (when-let [route (apply bidi/path-for routes location args)]
    (str "#" route)))

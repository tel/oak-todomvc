(ns oak.examples.todomvc.cs.Header
  (:require [oak.dom :as d]
            [oak.core :as oak]))

(defn view [_ _]
  (d/header {:className :header}
    (d/h1 {} "todos")
    (d/input {:className   :new-todo
              :placeholder "What needs to be done?"
              :autofocus   true})))

(def root
  (oak/make
    :name "Header"
    :view view))

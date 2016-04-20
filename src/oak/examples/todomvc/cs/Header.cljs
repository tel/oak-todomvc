(ns oak.examples.todomvc.cs.Header
  (:require
    [oak.dom :as d]
    [oak.component :as oak]
    [schema.core :as s]))

(def event
  (s/pair (s/eq :new-todo) :new-todo
          s/Str :todo-text))

; This is perhaps better done as a controlled element?
(defn view [_ submit]
  (d/header {:className :header}
    (d/h1 {} "todos")
    (d/uinput {:className   :new-todo
               :placeholder "What needs to be done?"
               :onKeyPress  (fn [ev]
                              (when (= "Enter" (.-key ev))
                                (let [target (.-target ev)
                                      value (.-value target)]
                                  (set! (.-value target) "")
                                  (submit [:new-todo value]))))
               :autofocus   true})))

(def root
  (oak/make
    :name "Header"
    :event event
    :view view))

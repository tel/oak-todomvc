(ns oak.examples.todomvc.cs.MainSection
  (:require
    [oak.examples.todomvc.model :as model]
    [oak.component :as oak]
    [oak.dom :as d]
    [oak.examples.todomvc.cs.TodoItem :as TodoItem]
    [schema.core :as s]
    [cljs.core.match :refer-macros [match]]))

(def state
  {:memory {s/Str (oak/state TodoItem/root)}
   :order [s/Str]})

(def event
  (s/cond-pre
    :toggle-all
    :clear-completed
    [:new-todo s/Str]
    [s/Str (oak/event TodoItem/root)]))

(defn step [event state]
  (match event
    :toggle-all
    (if (model/every-todo :completed state)
      (model/map-todos #(assoc % :completed false) state)
      (model/map-todos #(assoc % :completed true) state))

    :clear-completed
    (model/filter-todos (complement :completed) state)

    [:new-todo text]
    (let [{:keys [id] :as new-todo-state} (TodoItem/fresh text)]
      (-> state
          (update :memory assoc id new-todo-state)
          (update :order conj id)))

    [name subevent]
    (match subevent
      :destroy (model/destroy-todo name state)

      [:end-editing (text :guard empty?)] (model/destroy-todo name state)

      :else (update-in
              state [:memory name]
              (oak/step TodoItem/root subevent)))))

(defn view [{:keys [memory order]} submit]
  (d/section {:className :main}
    (d/input {:className :toggle-all
              :type :checkbox
              :onClick (fn [_] (submit :toggle-all))})
    (d/label {:htmlFor :toggle-all} "Mark all as complete")
    (let [children (map (fn [name]
                          (TodoItem/root
                            (get memory name)
                            (fn [ev] (submit [name ev]))))
                        order)]
      (apply d/ul {:className :todo-list} children))))

(def root
  (oak/make
    :name "MainSection"
    :state state
    :event event
    :step step
    :view view))

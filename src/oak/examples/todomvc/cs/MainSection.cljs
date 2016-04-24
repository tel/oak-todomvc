(ns oak.examples.todomvc.cs.MainSection
  (:require
    [oak.examples.todomvc.model :as model]
    [oak.component :as oak]
    [oak.dom :as d]
    [oak.examples.todomvc.cs.TodoItem :as TodoItem]
    [schema.core :as s]
    [cljs.core.match :refer-macros [match]]
    [oak.schema :as os]))

(def model
  {:memory {s/Str (oak/model TodoItem/root)}
   :order [s/Str]})

(def action
  (s/conditional
    keyword?
    (s/enum
      :toggle-all
      :clear-completed)

    (os/cmdp :new-todo)
    (os/cmd :new-todo s/Str)

    :else
    (os/targeted s/Str (oak/action TodoItem/root))))

(defn step [action model]
  (match action
    :toggle-all
    (if (model/every-todo :completed model)
      (model/map-todos #(assoc % :completed false) model)
      (model/map-todos #(assoc % :completed true) model))

    :clear-completed
    (model/filter-todos (complement :completed) model)

    [:new-todo text]
    (if (empty? text)
      model
      (let [{:keys [id] :as new-todo-model} (TodoItem/fresh text)]
        (-> model
            (update :memory assoc id new-todo-model)
            (update :order conj id))))

    [name subaction]
    (match subaction
      :destroy (model/destroy-todo name model)

      [:end-editing (text :guard empty?)] (model/destroy-todo name model)

      :else (update-in
              model [:memory name]
              (oak/step TodoItem/root subaction)))))

(defn view [{{:keys [memory order]} :model} submit]
  (d/section {:className :main}
    (d/input {:className :toggle-all
              :type :checkbox
              :onClick (fn [_] (submit :toggle-all))})
    (d/label {:htmlFor :toggle-all} "Mark all as complete")
    (let [children (map (fn [name]
                          (TodoItem/root
                            (get memory name)
                            (fn [ev] (submit [name ev]))))
                        (reverse order))]
      (apply d/ul {:className :todo-list} children))))

(def root
  (oak/make
    :name "MainSection"
    :model model
    :action action
    :step step
    :view view))

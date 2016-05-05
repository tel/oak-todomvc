(ns oak.examples.todomvc.cs.TodoApp
  (:require
    [oak.component :as oak]
    [oak.dom :as d]
    [oak.schema :as os]
    [cljs.core.match :refer-macros [match]]
    [oak.examples.todomvc.cs.Header :as Header]
    [oak.examples.todomvc.cs.MainSection :as MainSection]
    [oak.examples.todomvc.cs.Footer :as Footer]
    [schema.core :as s]
    [oak.examples.todomvc.model :as model]))

(def model
  {:todos (oak/model MainSection/root)})

(def action
  (os/cond-pair
    [:Header (oak/action Header/root)]
    [:MainSection (oak/action MainSection/root)]
    [:Footer (oak/action Footer/root)]))

(defn query [_model q]
  {:location (q [:navigation [:current]])})

(defn step [[target action] model]
  (match [target action]
    [:Header [:new-todo text]]
    (update model :todos (oak/step MainSection/root [:new-todo text]))

    [:Footer :clear-completed]
    (update model :todos (oak/step MainSection/root :clear-completed))

    [:MainSection subaction]
    (update model :todos (oak/step MainSection/root subaction))))

(defn view [{{:keys [todos]} :model {:keys [location]} :result} submit]
  (d/section {:className :todoapp}
    (Header/root nil (fn [e] (submit [:Header e])))
    (MainSection/root todos (fn [e] (submit [:MainSection e])))

    (let [memory (:memory todos)]
      (when-not (empty? memory)
        (Footer/root
          {:todo-count     (count memory)
           :location       (:handler location)
           :show-completed (model/some-todo :completed todos)}
          (fn [e] (submit [:Footer e])))))))

(def root
  (oak/make
    :name "TodoApp"
    :model model
    :action action
    :query query
    :step step
    :view view))

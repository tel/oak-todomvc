(ns oak.examples.todomvc.cs.TodoApp
  (:require
    [oak.component :as oak]
    [oak.dom :as d]
    [cljs.core.match :refer-macros [match]]
    [oak.examples.todomvc.cs.Header :as Header]
    [oak.examples.todomvc.cs.MainSection :as MainSection]
    [oak.examples.todomvc.cs.Footer :as Footer]
    [oak.examples.todomvc.model :as model]))

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

(defn view [{:keys [todos]} {:keys [location]} submit]
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
    :query query
    :step step
    :view view))

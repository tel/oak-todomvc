(ns oak.examples.todomvc.cs.TodoApp
  (:require
    [oak.component :as oak]
    [oak.dom :as d]
    [cljs.core.match :refer-macros [match]]
    [oak.examples.todomvc.cs.Header :as Header]
    [oak.examples.todomvc.cs.MainSection :as MainSection]
    [oak.examples.todomvc.cs.Footer :as Footer]
    [schema.core :as s]
    [oak.examples.todomvc.model :as model]))

(def state
  {:todos (oak/state MainSection/root)})

(def event
  (s/cond-pre
    (s/pair (s/eq :Header) :target (oak/event Header/root) :subevent)
    (s/pair (s/eq :MainSection) :target (oak/event MainSection/root) :subevent)
    (s/pair (s/eq :Footer) :target (oak/event Footer/root) :subevent)))

(defn query [_state q]
  {:location (q [:location :current])})

(defn step [[target event] state]
  (match [target event]
    [:Header [:new-todo text]]
    (update state :todos (oak/step MainSection/root [:new-todo text]))

    [:Footer :clear-completed]
    (update state :todos (oak/step MainSection/root :clear-completed))

    [:MainSection subevent]
    (update state :todos (oak/step MainSection/root subevent))))

(defn view [[{:keys [todos]} {:keys [location]}] submit]
  (d/section {:className :todoapp}
    (Header/root nil (fn [e] (submit [:Header e])))
    (MainSection/root todos (fn [e] (submit [:MainSection e])))

    (let [memory (:memory todos)]
      (when-not (empty? memory)
        (Footer/root
          {:todo-count     (count memory)
           :location       location
           :show-completed (model/some-todo :completed todos)}
          (fn [e] (submit [:Footer e])))))))

(def root
  (oak/make
    :name "TodoApp"
    :state state
    :query query
    :event event
    :step step
    :view view))

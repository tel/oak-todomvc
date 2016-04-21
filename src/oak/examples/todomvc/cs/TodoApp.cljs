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

(def state
  {:todos (oak/state MainSection/root)})

(def event
  (s/conditional
    (os/cmdp :Header)
    (os/cmd :Header (oak/event Header/root))

    (os/cmdp :MainSection)
    (os/cmd :MainSection (oak/event MainSection/root))

    (os/cmdp :Footer)
    (os/cmd :Footer (oak/event Footer/root))))

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

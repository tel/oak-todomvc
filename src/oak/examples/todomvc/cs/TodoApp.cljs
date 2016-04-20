(ns oak.examples.todomvc.cs.TodoApp
  (:require
    [oak.core :as oak]
    [oak.dom :as d]
    [cljs.core.match :refer-macros [match]]
    [oak.examples.todomvc.cs.Header :as Header]
    [oak.examples.todomvc.cs.MainSection :as MainSection]
    [oak.examples.todomvc.cs.Footer :as Footer]
    [schema.core :as s]))

(def state (oak/state MainSection/root))

(def event
  (s/cond-pre
    (s/pair (s/eq :Header) :target (oak/event Header/root) :subevent)
    (s/pair (s/eq :MainSection) :target (oak/event MainSection/root) :subevent)
    (s/pair (s/eq :Footer) :target (oak/event Footer/root) :subevent)))

(defn view [{:keys [memory] :as state} submit]
  (d/section {:className :todoapp}
    (Header/root nil (fn [e] (submit [:Header e])))
    (MainSection/root state (fn [e] (submit [:MainSection e])))

    (when-not (empty? memory)
      (Footer/root
        {:todo-count (count memory)}
        (fn [e] (submit [:Footer e]))))))

(defn step [[target event] state]
  (match [target event]
    [:Header [:new-todo text]]
    (oak/step MainSection/root [:new-todo text] state)

    [:Footer :clear-completed]
    (oak/step MainSection/root :clear-completed state)

    [:MainSection subevent]
    (oak/step MainSection/root subevent state)))

(def root
  (oak/make
    :name "TodoApp"
    :state state
    :event event
    :step step
    :view view))

(ns oak.examples.todomvc.cs.TodoApp
  (:require [oak.core :as oak]
            [oak.dom :as d]
            [oak.examples.todomvc.cs.Header :as Header]
            [oak.examples.todomvc.cs.MainSection :as MainSection]
            [oak.examples.todomvc.cs.Footer :as Footer]))

(defn view-compositor [{:keys [Header MainSection Footer]}]
  (d/section {:className :todoapp}
    Header MainSection

    ; Hidden and shown when there are todos
    Footer))

(def root
  (oak/static
    {:Header Header/root
     :MainSection MainSection/root
     :Footer Footer/root}
    :name "TodoApp"
    :view-compositor view-compositor))

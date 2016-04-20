(ns oak.examples.todomvc.core
  (:require
    [oak.examples.todomvc.cs.TodoApp :as TodoApp]
    [oak.component :as oak]))

(enable-console-print!)

(defn main []
  (oak/render
    TodoApp/root
    {:Header      nil
     :MainSection {:memory {"a" {:completed false
                                 :editing   false
                                 :text      "Hi"}}
                   :order ["a"]}
     :Footer      nil}
    (.getElementById js/document "app")))


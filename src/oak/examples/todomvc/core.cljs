(ns oak.examples.todomvc.core
  (:require
    [oak.examples.todomvc.cs.TodoApp :as TodoApp]
    [oak.core :as oak]))

(enable-console-print!)

(oak/render-once
  TodoApp/root
  {:Header      nil
   :MainSection {:memory {"a" {:completed false
                               :editing   false
                               :text      "Hi"}}
                 :order ["a"]}
   :Footer      nil}
  nil
  (.getElementById js/document "app"))


(ns oak.examples.todomvc.cs.Footer
  (:require [oak.core :as oak]
            [oak.dom :as d]))

(defn view [_ _]
  (d/footer {:className :footer}
    ; Should be "0 items left" by default
    (d/span {:className :todo-count}
      (d/strong {} "0") " " "items left")
    (d/ul {:className :filters}
      (d/li {}
        (d/a {:className :selected :href "#"}
          "All"))
      (d/li {}
        (d/a {:href "#/active"}
          "Active"))
      (d/li {}
        (d/a {:href "#/completed"}
          "Completed")))
    ; Hidden if no completed items left
    (d/button {:className :clear-completed}
      "Clear completed")))

(def root
  (oak/make
    :name "Footer"
    :view view))

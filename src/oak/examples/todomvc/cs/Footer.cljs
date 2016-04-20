(ns oak.examples.todomvc.cs.Footer
  (:require
    [oak.component :as oak]
    [oak.dom :as d]
    [schema.core :as s]))

(def state
  {:todo-count s/Int})

(defn view [{:keys [todo-count]} submit]
  (d/footer {:className :footer}
    ; Should be "0 items left" by default
    (d/span {:className :todo-count}
      (d/strong {} (str todo-count))
      " "
      (if (= 1 todo-count)
        "item left"
        "items left"))
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
    (d/button {:className :clear-completed
               :onClick (fn [_] (submit :clear-completed))}
      "Clear completed")))

(def root
  (oak/make
    :name "Footer"
    :view view))

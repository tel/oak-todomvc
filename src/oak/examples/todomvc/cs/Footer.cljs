(ns oak.examples.todomvc.cs.Footer
  (:require
    [oak.examples.todomvc.routes :as routes]
    [oak.component :as oak]
    [oak.dom :as d]))

(defn view [{:keys [todo-count show-completed location]} submit]
  (d/footer {:className :footer}
    ; Should be "0 items left" by default
    (d/span {:className :todo-count}
      (d/strong {} (str todo-count))
      " "
      (if (= 1 todo-count) "item left" "items left"))
    (letfn [(link [this-location name]
              (d/a {:className (d/class-names
                                 {:selected (= this-location location)})
                    :href      (str "#" (routes/path-for this-location))}
                name))]
      (d/ul {:className :filters}
        (d/li {} (link :show-all "All"))
        (d/li {} (link :show-active "Active"))
        (d/li {} (link :show-completed "Completed"))))

    (when show-completed
      (d/button {:className :clear-completed
                 :onClick   (fn [_] (submit :clear-completed))}
        "Clear completed"))))

(def root
  (oak/make
    :name "Footer"
    :view view))

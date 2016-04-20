(ns oak.examples.todomvc.cs.Footer
  (:require
    [oak.examples.todomvc.routes :as routes]
    [oak.component :as oak]
    [oak.dom :as d]
    [schema.core :as s]))

(def state
  {:todo-count s/Int
   :location s/Keyword
   :show-completed s/Bool})

(def event
  (s/enum :clear-completed))

(defn view [{:keys [todo-count show-completed location]} submit]
  (d/footer {:className :footer}
    ; Should be "0 items left" by default
    (d/span {:className :todo-count}
      (d/strong {} (str todo-count))
      " "
      (if (= 1 todo-count) "item left" "items left"))
    (letfn [(link [this-location name]
              (d/a {:className (d/class-names
                                 {:select (= this-location location)})
                    :href (routes/path-for this-location)}
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
    :state state
    :event event
    :view view))

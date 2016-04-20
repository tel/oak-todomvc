(ns oak.examples.todomvc.cs.MainSection
  (:require
    [oak.core :as oak]
    [oak.dom :as d]
    [oak.examples.todomvc.cs.TodoItem :as TodoItem]
    [schema.core :as s]
    [cljs.core.match :refer-macros [match]]))

(def state
  {:memory {s/Str (oak/state TodoItem/root)}
   :order [s/Str]})

(def event
  (s/cond-pre
    :toggle-all
    [:new-todo s/Str]
    [s/Str (oak/event TodoItem/root)]))

(defn ^:private every-todo [p state]
  (every? (comp p val) (:memory state)))

(defn ^:private map-vals [f hashmap]
  (into {} (map (fn [p] [(key p) (f (val p))])) hashmap))

(defn ^:private map-todos [f state]
  (update state :memory #(map-vals f %)))

(defn step [event state]
  (match event
    :toggle-all
    (if (every-todo :completed state)
      (map-todos #(assoc % :completed false) state)
      (map-todos #(assoc % :completed true) state))

    [:new-todo text]
    (let [{:keys [id] :as new-todo-state} (TodoItem/fresh text)]
      (-> state
          (update :memory assoc id new-todo-state)
          (update :order conj id)))

    [name subevent]
    (case subevent
      :destroy (-> state
                   (update :memory dissoc name)
                   (update :order (partial remove (partial = name))))

      (update-in
        state [:memory name]
        (oak/step TodoItem/root subevent)))))

(defn view [{:keys [memory order]} submit]
  (d/section {:className :main}
    (d/input {:className :toggle-all
              :type :checkbox
              :onClick (fn [_] (submit :toggle-all))})
    (d/label {:htmlFor :toggle-all} "Mark all as complete")
    (let [children (map (fn [name]
                          (TodoItem/root
                            (get memory name)
                            (fn [ev] (submit [name ev]))))
                        (reverse order))]
      (apply d/ul {:className :todo-list} children))))

(def root
  (oak/make
    :name "MainSection"
    :state state
    :event event
    :step step
    :view view))

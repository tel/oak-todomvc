(ns oak.examples.todomvc.cs.TodoItem
  (:require
    [oak.core :as oak]
    [schema.core :as s]
    [oak.dom :as d]
    [cljs.core.match :refer-macros [match]]
    [oak.examples.todomvc.cs.TodoItem.util :as util])
  (:import (goog.string)))

(def state
  {:completed s/Bool
   :editing   s/Bool
   :text      s/Str})

(def event
  (s/enum
    :toggle
    :begin-editing
    [:end-editing s/Str]
    :destroy))

(defn step [event state]
  (match event
    :toggle (update state :completed not)
    :begin-editing (assoc state :editing true)
    [:end-editing text] (assoc state
                          :editing false
                          :text text)
    :destroy state))



(defn view [{:keys [completed editing text]} submit]
  (d/li {:className (d/class-names
                      {:checked completed
                       :editing editing})}
    (d/div {:className :view}
      (d/input {:className :toggle
                :type      :checkbox
                :checked   completed
                :onClick   (fn [_] (submit :toggle))})
      (d/label {:onDoubleClick (fn [_] (submit :begin-editing))} text)
      (d/button {:className :destroy
                 :onClick (fn [_] (submit :destroy))}))
    (when editing
      (util/editing-uinput text submit))))

(def root
  (oak/make
    :name "TodoItem"
    :keyfn :id
    :state state
    :event event
    :step step
    :view view))

(defn fresh [text]
  {:completed false
   :editing false
   :id (goog.string/getRandomString)
   :text text})

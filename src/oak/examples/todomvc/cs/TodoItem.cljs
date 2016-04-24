(ns oak.examples.todomvc.cs.TodoItem
  (:require
    [oak.component :as oak]
    [schema.core :as s]
    [oak.dom :as d]
    [cljs.core.match :refer-macros [match]]
    [oak.examples.todomvc.cs.TodoItem.util :as util]
    [oak.schema :as os])
  (:import (goog.string)))

(def model
  {:id s/Str
   :completed s/Bool
   :editing   s/Bool
   :text      s/Str})

(def action
  (s/cond-pre
    (s/enum
      :toggle
      :begin-editing
      :destroy)
    (os/cmd :end-editing s/Str)))

(defn step [action model]
  (match action
    :toggle (update model :completed not)
    :begin-editing (assoc model :editing true)
    [:end-editing text] (assoc model
                          :editing false
                          :text text)
    :destroy model))

(defn view [{{:keys [completed editing text]} :model} submit]
  (d/li {:className (d/class-names
                      {:checked completed
                       :editing editing})}
    (d/div {:className :view}
      (d/input {:className :toggle
                :type      :checkbox
                :checked   completed
                :onChange  (fn [_] (submit :toggle))})
      (d/label {:onDoubleClick (fn [_] (submit :begin-editing))} text)
      (d/button {:className :destroy
                 :onClick (fn [_] (submit :destroy))}))
    (when editing
      (util/editing-uinput text submit))))

(def root
  (oak/make
    :name "TodoItem"
    :keyfn (comp :id :model)
    :model model
    :action action
    :step step
    :view view))

(defn fresh [text]
  {:completed false
   :editing false
   :id (goog.string/getRandomString)
   :text text})

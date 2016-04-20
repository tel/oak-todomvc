(ns oak.examples.todomvc.cs.TodoItem
  (:require [oak.core :as oak]
            [schema.core :as s]
            [oak.dom :as d])
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
  (case event
    :toggle (update state :completed not)))

(def editing-uinput
  "Absorbs all clicks that aren't in this element and triggers :end-editing
  with the current value. Also submits :end-editing when enter is hit."
  (oak/make
    :state s/Str
    :event [:end-editing s/Str]
    :view
    (fn [text submit]
      (d/uinput {:onClick    (fn [ev] (.preventPropagation ev))
                 :onKeyPress (fn [ev]
                               (when (= "Enter" (.-key ev))
                                 (submit [:end-editing (.. ev -target -value)])))
                 :className  :edit
                 :value      text}))
    :on-mount
    (fn [el _ submit]
      (let [end-edit (fn [ev]
                       (submit [:end-editing (.. ev -target -value)]))]
        (set! (.-end-edit el) end-edit)
        (.. js/document -body
            (addEventListener "click" end-edit))))
    :on-unmount
    (fn [el _ _]
      (let [end-edit (.-end-edit el)]
        (.. js/document -body
            (removeEventListener "click" end-edit))))))

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
      (editing-uinput text submit))))

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

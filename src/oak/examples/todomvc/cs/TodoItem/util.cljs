(ns oak.examples.todomvc.cs.TodoItem.util
  (:require
    [oak.component :as oak]
    [schema.core :as s]
    [oak.dom :as d]))

(def editing-uinput
  "Absorbs all clicks that aren't in this element and triggers :end-editing
  with the current value. Also submits :end-editing when enter is hit."

  ; NOTE: This is a *bit* tricky and deserves explanation.
  ;
  ; The short story ought to be that we detect "outside of element" events by
  ; placing a listener on the document which only fires when an event didn't
  ; originate in the element.
  ;
  ; The basics of that story are fine, but React's synthetic event system
  ; makes this a little tricky. We'd like to prevent the document handler
  ; from firing when the event arises in the element handler by simply
  ; calling ev.stopPropagation(), but (a) since ev is synthetic that won't
  ; apply to handlers outside of React and, moreover, (b) since React's
  ; synthetic event handler is bound on document itself anyway... it is really
  ; too late!
  ;
  ; So, instead we use ev.nativeEvent.stopImmediatePropagation() which keeps
  ; the event from triggering *further handlers on the current element* (e.g.
  ; document). This works since we know that the React synthetic event
  ; handler is always bound *before* the ones we bind in the mounting cycle
  ; of this element.
  ;
  ; So this depends upon (a) stopImmediatePropagation existing (e.g. not
  ; IE < 9) and that "If several listeners are attached to the same element for
  ; the same event type, they are called in order in which they have been
  ; added." (MDN on stopImmediatePropagation) holds properly.

  (oak/make
    :state s/Str
    :event [:end-editing s/Str]
    :view
    (fn [text submit]
      (d/uinput {:onClick    (fn [ev]
                               (.. ev -nativeEvent stopImmediatePropagation)
                               (.stopPropagation ev))
                 :onKeyPress (fn [ev]
                               (when (= "Enter" (.-key ev))
                                 (submit [:end-editing (.. ev -target -value)])))
                 :className  :edit
                 :value      text}))
    :on-mount
    (fn [el _ submit]
      (let [end-edit (fn [ev]
                       (submit [:end-editing (.. el -value)]))]
        (set! (.-end-edit el) end-edit)
        (.addEventListener  js/document "click" end-edit))
      (.focus el))
    :on-unmount
    (fn [el _ _]
      (let [end-edit (.-end-edit el)]
        (.removeEventListener js/document "click" end-edit)))))

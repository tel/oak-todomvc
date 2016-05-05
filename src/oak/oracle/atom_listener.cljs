(ns oak.oracle.atom-listener
  "An atom-listener oracle is built around an atom external to the component
  system. Every time the atom changes the cache is updated with the new value
  of the atom. Messages are, by default, paths into the atom."
  (:require [oak.oracle :as oracle]
            [oak.schema :as os]
            [schema.core :as s]))

(defn oracle
  "Build an Oracle which watches an atom and updates its own cache with the
  atom's state. There is no refresh step.

  Normally, the queries this oracle responds to are simple paths into the
  atom state (so that [] returns the whole thing) but you can customize this
  by providing a :handle-query function. You will want to also provide a
  :query-schema in this case. For further validation you can also pass an
  :atom-schema key to validate the shape of the atom on updates."
  [the-atom & {:keys [handle-query query-schema atom-schema]
               :or   {handle-query (fn [model query] (get-in model query))
                      query-schema (s/maybe [s/Any])
                      atom-schema  s/Any}}]
  (oracle/make
    :model atom-schema
    :action (os/cmd :set atom-schema)
    :query query-schema

    :step
    (fn step [[_set new-state] _model] new-state)

    :respond handle-query

    :start
    (fn start [submit]
      (submit [:set @the-atom])
      (let [uniq-name (gensym)]
        (add-watch
          the-atom uniq-name
          (fn watch-atom [_uniq-key _the-atom _old-state new-state]
            (submit [:set new-state])))
        uniq-name))

    :stop
    (fn stop [uniq-name]
      (remove-watch the-atom uniq-name))))

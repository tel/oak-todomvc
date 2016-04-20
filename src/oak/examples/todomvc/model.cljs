(ns oak.examples.todomvc.model)

(defn filter-todos [p {:keys [memory order]}]
  (let [store (transient memory)
        new-order (doall
                    (filter
                      (fn [name]
                        (let [item (get memory name)
                              ok (p item)]
                          (when-not (p item) (dissoc! store name))
                          ok))
                      order))]
    {:memory (persistent! store)
     :order new-order}))

(defn destroy-todo [name state]
  (-> state
      (update :memory dissoc name)
      (update :order (partial remove (partial = name)))))

(defn every-todo [p state]
  (every? (comp p val) (:memory state)))

(defn some-todo [p state]
  (boolean (some (comp p val) (:memory state))))

(defn map-vals [f hashmap]
  (into {} (map (fn [p] [(key p) (f (val p))])) hashmap))

(defn map-todos [f state]
  (update state :memory #(map-vals f %)))

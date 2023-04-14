(ns plait-talk.core)

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn add-to-database
  [db k elm]
  (swap! db (fn [db] (update db k #(assoc % (:id elm) elm))))
  elm)

(defn create-unicorn-impl
  [db currency balance]
  (add-to-database
   db
   :unicorns
   {:id (uuid)
    :currency currency
    :balance balance}))

(defn create-rainbow-impl
  [db unicorn price]
  (add-to-database
   db
   :rainbows
   {:id (uuid)
    :unicorn-id (:id unicorn)
    :price price}))

(defn book-impl
  [db rainbow unicorn]
  (swap!
   db
   (fn [snapshot]
     (let [rainbow-owner (get-in snapshot [:unicorns (:unicorn-id rainbow)])]
       (if (= (:currency unicorn) (:currency rainbow-owner))
         (-> snapshot
             (update-in [:unicorns (:id unicorn) :balance] #(- % (:price rainbow)))
             (update-in [:unicorns (:id rainbow-owner) :balance] #(+ % (:price rainbow))))
         snapshot)))))

(defn balance-impl
  [db unicorn]
  (get-in @db [:unicorns (:id unicorn) :balance]))

(let [db (atom {:unicorns {}
                :rainbows {}})]
  (defn create-unicorn
    [& params]
    (apply create-unicorn-impl db params))
  (defn create-rainbow
    [& params]
    (apply create-rainbow-impl db params))
  (defn book
    [& params]
    (apply book-impl db params))
  (defn balance
    [& params]
    (apply balance-impl db params)))

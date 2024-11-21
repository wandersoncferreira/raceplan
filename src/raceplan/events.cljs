(ns raceplan.events
  (:require
   [re-frame.core :as re-frame]
   [raceplan.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-fx
  ::navigate
  (fn [_ [_ handler]]
   {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 (fn [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel)}))

(re-frame/reg-event-db
 ::inc-row-number
 (fn [db _]
   (update db :row-number inc)))

(re-frame/reg-event-db
 ::add-split
 (fn [db [_ val]]
   (let [{:keys [splits]} db
         [row split] val
         row-split (get splits (first row) {})
         new-row-split (assoc row-split row split)
         maybe-next-index (when (= "1" (second row))
                            (str (inc (js/parseInt (first row)))))
         maybe-next-value (when maybe-next-index split)
         new-db (assoc-in db [:splits (first row)] new-row-split)]
     (if maybe-next-index
       (assoc-in new-db [:splits maybe-next-index]
                 {(str maybe-next-index "0") maybe-next-value})
       new-db))))

(re-frame/reg-event-db
 ::del-row-number
 (fn [db [_ val]]
   (let [{:keys [splits]} db
         new-splits (dissoc splits (str val))]
     (-> db
         (update :row-number dec)
         (assoc :splits new-splits)))))

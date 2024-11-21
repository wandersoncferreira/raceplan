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
         new-row-split (assoc row-split row split)]
     (assoc-in db [:splits (first row)] new-row-split))))

(re-frame/reg-event-db
 ::del-row-number
 (fn [db [_ val]]
   (let [{:keys [splits]} db
         new-splits (dissoc splits (str val))]
     (-> db
         (update :row-number dec)
         (assoc :splits new-splits)))))

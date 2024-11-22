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
 ::toggle-pace-segmento
 (fn [db _]
   (update db :toggle-pace-segmento not)))

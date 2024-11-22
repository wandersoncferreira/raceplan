(ns raceplan.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 ::row-number
 (fn [db _]
   (:row-number db)))

(re-frame/reg-sub
 ::splits
 (fn [db _]
   (:splits db)))

(re-frame/reg-sub
 ::toggle-pace-segmento
 (fn [db _]
   (:toggle-pace-segmento db)))

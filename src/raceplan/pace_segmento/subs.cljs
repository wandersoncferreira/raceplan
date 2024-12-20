(ns raceplan.pace-segmento.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::row-number
 (fn [db _]
   (:row-number db)))

(re-frame/reg-sub
 ::splits
 (fn [db _]
   (:splits db)))

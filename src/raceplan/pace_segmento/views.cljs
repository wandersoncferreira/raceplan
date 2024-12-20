(ns raceplan.pace-segmento.views
  (:require
   [re-frame.core :as re-frame]
   [goog.string :as gstring]
   [goog.string.format]
   ["dayjs" :as dayjs]
   ["dayjs/plugin/duration" :as duration]
   [raceplan.pace-segmento.events :as events]
   [raceplan.pace-segmento.subs :as subs]))

(dayjs/extend duration)

(defn to-seconds
  [minutes seconds]
  (+ seconds (* 60 minutes)))

(defn fmt-splits [splits]
  (for [k (keys splits)]
    (let [split (get splits k)
          default-split (int-array [0 0 0 0])]
      (doseq [entry split]
        (let [index-full (first entry)
              index (-> index-full second js/parseInt)
              value (js/parseInt (second entry))]
          (aset default-split index value)))
      [(first default-split)
       (second default-split)
       (to-seconds (nth default-split 2)
                   (nth default-split 3))])))

(defn total-distance
  [splits]
  (->> splits
       (map (juxt second first))
       (map (partial apply -))
       (filter #(> % 0))
       (apply +)))

(defn dist-diff
  [split]
  (- (second split) (first split)))

(defn total-time
  [splits]
  (->> splits
       (map (fn [s] [(dist-diff s) (last s)]))
       (map (fn [s]
              (repeat (first s) (dayjs/duration (second s) "seconds"))))
       (apply concat)
       (reduce
        (fn [total-dur dur]
          (.add total-dur dur))
        (dayjs/duration 0 "seconds"))))

(defn pace-medio
  [total-seconds total-distance]
  (let [med-secs (/ total-seconds total-distance)
        med-dur (dayjs/duration med-secs "seconds")]
    med-dur))

(defn view []
  (let [splits (re-frame/subscribe [::subs/splits])
        row-number (re-frame/subscribe [::subs/row-number])
        splits-fmtted (fmt-splits @splits)
        tdistance (total-distance splits-fmtted)
        ttime (total-time splits-fmtted)]
    [:div
     [:table {:cellPadding "4"}
      [:tbody
       [:tr {:style {:font-weight "bold"
                     :white-space "pre-wrap"}}
        [:td "KM\nInicial"]
        [:td "KM\nFinal"]
        [:td "Pace Médio\n(Minutos)"]
        [:td "Pace Médio\n(Segundos)"]
        [:td {:style {:padding-left "2em"}} "Novo\nSplit"]
        [:td {:style {:padding-left "2em"}} "Remover\nSplit"]]
       (doall
        (for [r (range @row-number)]
          [:tr {:key r}
           (doall
            (for [i (range 4)]
              (let [index (str r i)
                    td-val (get-in @splits [(str r) index])]
                [:td {:key index}
                 [:input {:type "text"
                          :value td-val
                          :style {:width "5em"}
                          :on-change #(let [val (-> % .-target .-value)]
                                        (re-frame/dispatch [::events/add-split [index val]]))}]])))
           [:td [:a.btn.info {:style {:height "1.7em"
                                      :margin-left "2em"}
                              :on-click #(re-frame/dispatch [::events/inc-row-number])}]]
           [:td [:a.btn.warn {:style {:height "1.7em"
                                      :margin-left "2em"}
                              :on-click #(re-frame/dispatch [::events/del-row-number r])}]]]))]]
     [:div
      [:br]
      [:br]
      [:br]
      [:span {:style {:font-weight "bold"}} (gstring/format "Tempo Total (%d KM): " tdistance)]
      [:br]
      [:span (gstring/format "%d Horas %d Minutos e %s Segundos"
                             (.get ttime "hours")
                             (.get ttime "minutes")
                             (.get ttime "seconds"))]
      [:br]
      (let [mpace (pace-medio (.asSeconds ttime) tdistance)]
        [:span (gstring/format "Pace Médio de %d Minutos e %s Segundos"
                               (.get mpace "minutes")
                               (.get mpace "seconds"))])]]))

(ns raceplan.views
  (:require
   [re-frame.core :as re-frame]
   [raceplan.events :as events]
   [raceplan.pace-segmento.views :as pace-segmento]
   [raceplan.routes :as routes]
   [raceplan.subs :as subs]))

(enable-console-print!)

(defn home-panel []
  (let [toggle-pace-segmento (re-frame/subscribe [::subs/toggle-pace-segmento])]
    [:div.container
     [:h1.big "Raceplan"]
     [:span "Calculadoras para ajudar no planejamento da prova de corrida."]
     [:br]
     [:br]
     [:br]
     [:div.row
      [:div
       [:a.btn {:on-click #(re-frame/dispatch [::events/toggle-pace-segmento])}
        "Pace Médio por Segmento"]]]
     [:br]
     [:br]
     [:div.row
      (when @toggle-pace-segmento
        [pace-segmento/view])]]))

(defmethod routes/panels :home-panel [] [home-panel])

;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (js/console.log "main panel active panel")
    (js/console.log (identity @active-panel))
    (routes/panels @active-panel)))

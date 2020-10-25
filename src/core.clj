(ns core
  (:require [cljfx.api :as fx]
            [components.inputs :as input]
            [db :refer [db-spec]]
            [clojure.java.jdbc :as jdbc]))

(defn select-values [map ks]
  (for [key ks]
    (key map)))

(def *state (atom {}))

(def keywords {:input-section [:first_name :patronomyc :last_name :telephone :birth_date :weigth :height]})
(def labels {:input-section {:first_name "Имя"
                             :patronomyc "Отчество"
                             :last_name "Фамилия"
                             :telephone "Телефон в формате *+7"
                             :birth_date "Дата рождения *д.м.г"
                             :weigth "Вес *кг"
                             :height "Рост *см"}})
(def colors {:status-color (atom "#91dba4")})

(defn button [text handler]
  {:fx/type :button
   :text text
   :on-action handler})

(def status-circle {:fx/type :circle
                    :center-x 100
                    :center-y 100
                    :fill ((comp deref :status-color) colors)
                    :radius 14.5})

(defn input-section
  [{:keys [first-name patronomyc last-name telephone birth-date weigth height]}]
  (into []
        (let [keywords* (:input-section keywords)
              values [first-name patronomyc last-name telephone birth-date weigth height]
              labels* (select-values (:input-section labels) keywords*)]
          (input/labeled-inputs {:keywords keywords*
                                 :values values
                                 :labels labels*
                                 :atom *state
                                 :max-width 200}))))

(fx/mount-renderer
 *state
 (fx/create-renderer
  :middleware
  (fx/wrap-map-desc
   (fn [keys]
     {:fx/type :stage
      :showing true
      :width 1100
      :title "Heredidatem"
      :scene
      {:fx/type :scene
       :root
       {:fx/type :h-box
        :padding 40
        :spacing 20
        :children [{:fx/type :v-box
                    :padding 40
                    :spacing 20
                    :children (conj (input-section keys) (button "Забрать данные" (fn [] (jdbc/insert! db-spec :patients @*state))))}
                   {:fx/type :h-box
                    :padding 60
                    :spacing 20
                    :children (let [status-color (:status-color colors)]
                                [status-circle
                                 (button "Изменить" (fn [] (reset! status-color "#e36f6f")))
                                 (button "Удалить" (fn [] (reset! status-color "#91dba4")))])}]}}}))))


(comment
  (map (fn [key] (-> key name  symbol)) (:input-section keywords)))
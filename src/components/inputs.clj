(ns components.inputs)

(defn labeled-input [{:keys [label input]}]
  {:fx/type :v-box
   :spacing 5
   :children [{:fx/type :label
               :text label}
              input]})

(defn labeled-inputs [{:keys [keywords values labels atom max-width handler]}]
  (map (fn [value key label]
         {:fx/type (fn [{:keys [value atom]}]
                     {:fx/type labeled-input
                      :label label
                      :input {:fx/type :text-field
                              :max-width max-width
                              :text-formatter {:fx/type :text-formatter
                                               :value-converter :default
                                               :value value
                                               :on-value-changed (fn [value] (swap! atom assoc key value))}}})
          :value value
          :atom atom}) values keywords labels))

(ns inventory-app.process)

(defn convert-item-result
  "Convert the result from a database item search into a more useful form by
   grouping the attributes together so that each item is one element in a
   vector, and the attributes are a vector within the item map."
  [in]
  (loop [in in out []]
    (if (seq in)
      (recur (rest in)
             (if (= (:id (peek out)) (:item/id (first in)))
               (conj (pop out) 
                     (assoc (peek out) :attributes
                            (conj (:attributes (peek out))
                                  {:name (:attribute/name (first in))
                                   :value (:value/value (first in))
                                   :unit (:attribute/unit (first in))})))
               (conj out
                     {:id (:item/id (first in))
                      :name (:item/name (first in))
                      :attributes [{:name (:attribute/name (first in))
                                    :value (:value/value (first in))
                                    :unit (:attribute/unit (first in))}]})))
      out)))
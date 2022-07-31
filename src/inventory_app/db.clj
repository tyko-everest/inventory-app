(ns inventory-app.db
  (:require [clojure.string :refer [blank?]]
            [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]))

(defn search-where [item attributes]
  [:and
   (when (not (blank? item))
     [:= :item.name item])
   (loop [attrs attributes res [:or]]
     (if (seq attrs)
       (recur (rest attrs) (conj res [:= :attribute.name (first attrs)]))
       res))])

(defn search-query [item attributes]
  (sql/format
   (apply h/where
          (-> (h/select :item.id :item.name :attribute.name :value.value :attribute.unit)
              (h/from :value)
              (h/inner-join :item [:= :item.id :value.item_id])
              (h/inner-join :attribute [:= :attribute.id :value.attr_id])
              (h/order-by :item.id))
          (search-where item attributes))))

(defn search [item attribute]
  (let [db {:dbtype "sqlite" :dbname "inventory"}
        ds (jdbc/get-datasource db)]
    (jdbc/execute! ds (search-query item attribute))))

(defn get-all-query []
  (sql/format
   (-> (h/select :item.id :item.name :attribute.name :value.value :attribute.unit)
       (h/from :item)
       (h/left-join :value [:= :item.id :value.item_id])
       (h/left-join :attribute [:= :attribute.id :value.attr_id])
       (h/order-by :item.id))))

(defn get-all []
  (let [db {:dbtype "sqlite" :dbname "inventory"}
        ds (jdbc/get-datasource db)]
    (jdbc/execute! ds (get-all-query))))

(defn get-attributes-query []
  (sql/format 
   (-> (h/select :name :unit)
       (h/from :attribute))))

(defn get-attributes []
  (let [db {:dbtype "sqlite" :dbname "inventory"}
        ds (jdbc/get-datasource db)]
    (jdbc/execute! ds (get-attributes-query))))

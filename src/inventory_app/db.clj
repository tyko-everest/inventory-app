(ns inventory-app.db
  (:require [clojure.string :refer [blank? split]]
            [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]))

(defn search-query [item attributes]
  (sql/format
   (apply h/where
          (-> (h/select :item.id :item.name :attribute.name :value.value)
              (h/from :value)
              (h/inner-join :item [:= :item.id :value.item_id])
              (h/inner-join :attribute [:= :attribute.id :value.attr_id]))
          (if (blank? item)
            [:true]
            [:= :item.name item]) 
            (loop [attrs attributes res []]
                ;;   (println res)
              (if (seq attrs)
                (recur (rest attrs)
                       (conj res [:= :attribute.name (first attrs)]))
                res)))))

(defn search [name attribute]
  (let [db {:dbtype "sqlite" :dbname "inventory"}
        ds (jdbc/get-datasource db)]
    (jdbc/execute! ds (search-query name attribute))))

(defn get-attributes-query []
  (sql/format 
   (-> (h/select :name :unit)
       (h/from :attribute))))

(defn get-attributes []
  (let [db {:dbtype "sqlite" :dbname "inventory"}
        ds (jdbc/get-datasource db)]
    (jdbc/execute! ds (get-attributes-query))))
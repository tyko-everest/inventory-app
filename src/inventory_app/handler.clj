(ns inventory-app.handler
  (:require [clojure.string :as string]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [next.jdbc :as jdbc]
            [inventory-app.page-gen :as gen]))

(defn get-attribute-query [attribute]
  (str "SELECT item.id, item.name, attribute.name, value.value "
       "FROM ((value "
       "INNER JOIN item ON item.id = value.item_id) "
       "INNER JOIN attribute ON attribute.id = value.attr_id) "
       "WHERE attribute.name = \""
       attribute
       "\";"))

(defn get-by-attribute [attribute]
 (let [db {:dbtype "sqlite" :dbname "inventory"}
       ds (jdbc/get-datasource db)]
   (jdbc/execute! ds [(get-attribute-query attribute)])))

(defn get-attributes []
  (let [db {:dbtype "sqlite" :dbname "inventory"}
       ds (jdbc/get-datasource db)]
   (jdbc/execute! ds ["SELECT name, unit FROM attribute;"])))

(defroutes app-routes
  (GET "/" []
    (gen/page "Home" (gen/home)))
  (GET "/attributes" []
    (gen/page "Attributes" (gen/attribute-table (get-attributes))))
  (GET "/results" [attribute name]
    (gen/page "Results" (str (get-by-attribute attribute))))
  (GET "/search" []
    (gen/page "Search" (gen/search "/results")))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

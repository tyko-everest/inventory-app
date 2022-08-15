(ns inventory-app.handler
  (:require [clojure.string :refer [blank? split]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [inventory-app.page-gen :as gen]
            [inventory-app.db :as db]
            [inventory-app.process :as process]))

(defroutes app-routes
  (GET "/" []
    (gen/page "Home" (gen/home)))
  (GET "/attributes" []
    (gen/page "Attributes" (gen/attribute-table (db/get-attributes))))
  (GET "/results" [name attributes]
    (let [attributes (if (blank? attributes)
                       nil
                       (split attributes #",\s*"))]
      (gen/page "Results" (gen/results 
                           (process/convert-item-result 
                            (db/search name attributes))))))
  (GET "/search" []
    (gen/page "Search" (gen/search "/results")))
  (GET "/all" []
    (gen/page "All" (gen/all (process/convert-item-result (db/get-all)))))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

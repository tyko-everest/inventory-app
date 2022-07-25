(ns inventory-app.page-gen
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-css]]
            [hiccup.form :as form]))

(defn
  row-from-map
  "Given data in a map and a list of keywords generates an html table row."
  [data kws]
  (html (loop [kws kws res [:tr]]
          (if (seq kws)
            (recur (rest kws)
                   (conj res [:td ((first kws) data)]))
            res))))

(defn
  attribute-table
  "Generates a table of all attributes."
  [attributes]
  (html [:table [:tr [:th "Attribute"] [:th "Unit"]]
         (loop [attrs attributes res ""]
           (if (seq attrs)
             (recur (rest attrs)
                    (str res (row-from-map 
                              (first attrs) 
                              [:attribute/name :attribute/unit])))
             res))]))

(defn
  home
  "Generates the home page."
  []
  (html
   [:h1 "Inventory System"]
   [:a {:href "/attributes"} "Attributes"]
   [:br]
   [:a {:href "/search"} "Search"]))

(defn
  search
  "Generates a form that searches for items."
  [action]
  (html 
   (form/form-to
    [:get action]
    (html
     [:table
      [:tr
       [:td (form/label "name" "Name")]
       [:td (form/text-field "name")]]
      [:tr
       [:td (form/label "attributes" "Attributes")]
       [:td (form/text-field "attributes")]]]
     (form/submit-button "Search")))))

(defn page [title body]
  (html5 [:head 
          [:title (str "Inventory - " title)] 
          (include-css "table.css")] 
         [:body body]))

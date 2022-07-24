(defproject inventory-app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-defaults "0.3.3"]
                 [compojure "1.6.3"]
                 [com.github.seancorfield/next.jdbc "1.2.659"]
                 [com.github.seancorfield/honeysql "2.2.891"]
                 [org.xerial/sqlite-jdbc "3.36.0.3"]
                 [hiccup/hiccup "1.0.5"]]
  :source-paths ["src/"]
  :plugins [[lein-ring "0.12.6"]]
  :ring {:handler inventory-app.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.4.0"]]}})

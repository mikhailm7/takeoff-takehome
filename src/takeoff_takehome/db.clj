(ns takeoff-takehome.db
  (:require [hikari-cp.core :as h]
            [clojure.java.jdbc :as jdbc]
            [mount.core :refer [defstate]]))

(def datasource-options {:auto-commit        true
                         :read-only          false
                         :connection-timeout 30000
                         :validation-timeout 5000
                         :idle-timeout       600000
                         :max-lifetime       1800000
                         :minimum-idle       10
                         :maximum-pool-size  10
                         :pool-name          "db-pool"
                         :adapter            "postgresql"
                         :username           "takeoff"
                         :password           "takeoff123"
                         :database-name      "takeoff_takehome"
                         :server-name        "localhost"
                         :port-number        5432
                         :register-mbeans    false})

(defn db-url []
  (format "jdbc:%s://%s:%s/%s?user=%s&password=%s"
          (:adapter datasource-options)
          (:server-name datasource-options)
          (:port-number datasource-options)
          (:database-name datasource-options)
          (:username datasource-options)
          (:password datasource-options)))

(defstate datasource
  :start (h/make-datasource datasource-options)
  :stop (h/close-datasource datasource))

(defn run-query! [q]
  (jdbc/with-db-connection [conn {:datasource datasource}]
    (let [rows (jdbc/query conn q)]
      rows)))

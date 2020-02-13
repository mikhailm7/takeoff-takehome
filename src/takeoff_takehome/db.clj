(ns takeoff-takehome.db
  (:require [hikari-cp.core :as h]
            [clojure.java.jdbc :as jdbc]
            [jeesql.core :refer [defqueries]]
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

(defstate datasource
  :start (h/make-datasource datasource-options)
  :stop (h/close-datasource datasource))

(defn conn []
  {:datasource datasource})

;; Dynamically generates functions in the current namespace
;; based on the spec in takeoff-takehome/resources/db/queries.sql
;; See https://github.com/tatut/jeesql for more information
(defqueries "db/queries.sql")

(defn user-exists? [email]
  (->> {:email email}
       (fetch-user-count-by-email (conn))
       (= 1)))

(defn get-user-by-email! [email]
  (->> {:email email}
       (fetch-user-by-email (conn))
       (first)))

(defn get-user-permissions-by-email! [email]
  (->> {:email email}
       (fetch-user-permissions-by-email (conn))
       (map :permission)))

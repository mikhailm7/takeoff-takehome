(ns takeoff-takehome.db
  (:require [hikari-cp.core :as h]
            [clojure.java.jdbc :as jdbc]
            [cprop.core :refer [load-config]]
            [jeesql.core :refer [defqueries]]
            [mount.core :refer [defstate]]))

(def datasource-options (load-config))

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

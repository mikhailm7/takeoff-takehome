(ns takeoff-takehome.db
  (:require [hikari-cp.core :as h]
            [clojure.java.jdbc :as jdbc]
            [mount.core :refer [defstate]]
            [taoensso.timbre :as timbre :refer [info]]
            [cprop.core :refer [load-config]]))

(def datasource-options (load-config))

(defstate datasource
  :start (h/make-datasource datasource-options)
  :stop (h/close-datasource datasource))

(defn run-query! [q]
  (info "running query: " q)
  (jdbc/with-db-connection [conn {:datasource datasource}]
    (let [rows (jdbc/query conn q)]
      rows)))

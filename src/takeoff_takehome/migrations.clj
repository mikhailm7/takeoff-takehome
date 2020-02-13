(ns takeoff-takehome.migrations
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [takeoff-takehome.db :as db]
  ))

(def migration-config
  {:datastore  (jdbc/sql-database (db/db-url))
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate migration-config))

(defn rollback []
  (repl/rollback migration-config))

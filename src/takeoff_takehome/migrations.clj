(ns takeoff-takehome.migrations
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [taoensso.timbre :as log]
            [takeoff-takehome.db :as db]))

(def migration-config
  {:datastore  (jdbc/sql-database (db/db-url))
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (log/info "Migrating database...")
  (repl/migrate migration-config))

(defn rollback []
  (log/info "Rolling back database migration...")
  (repl/rollback migration-config))

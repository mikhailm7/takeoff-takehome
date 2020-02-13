(ns takeoff-takehome.auth
  (:require [buddy.sign.jwt :as jwt]
            [clj-time.core :as time]
            [buddy.core.hash :as hash]
            [taoensso.timbre :as timbre :refer [info error]]))

(defonce secret-key "86bae26023208e57a5880d5ad644143c567fc57baaf5a942")

(def secret (hash/sha256 secret-key))

(defonce expiration-in-hours 1)

(defn claims [email]
  {:user email
   :exp (time/plus (time/now) (time/hours expiration-in-hours))})

(defn generate-signature [email password]
  (info "Generating Signature for user: " email)
  (jwt/sign (claims email) secret))

(defn unsign-token [token]
  (try
    (jwt/unsign token secret)
    (catch Exception e
      (error "[ERROR] error in unsigning token: %" e))))

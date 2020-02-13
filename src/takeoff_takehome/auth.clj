(ns takeoff-takehome.auth
  (:require [buddy.sign.jwt :as jwt]
            [clj-time.core :as time]
            [buddy.core.hash :as hash]
            [buddy.hashers :as hashers]
            [taoensso.timbre :as log]))

(defonce secret-key "86bae26023208e57a5880d5ad644143c567fc57baaf5a942")

(def secret (hash/sha256 secret-key))

(defonce expiration-in-hours 1)

(defn claims [email]
  {:user email
   :exp (time/plus (time/now) (time/hours expiration-in-hours))})

(defn generate-signature [email password]
  (log/infof "Generating Signature for user: %s" email)
  (jwt/sign (claims email) secret))

(defn unsign-token [token]
  (try
    (jwt/unsign token secret)
    (catch Exception e
      (log/errorf "[ERROR] error in unsigning token: %s" e))))

(defn hash-password [password]
  (hashers/derive password {:alg :bcrypt+sha512}))

(defn check-password [plaintext hashed]
  (-> (hashers/check plaintext hashed)
      (boolean)))

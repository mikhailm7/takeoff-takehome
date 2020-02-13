(ns takeoff-takehome.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.pprint :as pp]
            [clojure.data.json :as json]
            [mount.core :refer [defstate] :as mount]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.json :refer [wrap-json-params]]
            [ring.util.http-response :as http-response]
            [takeoff-takehome.auth :as auth]
            [takeoff-takehome.db :as db]
            [taoensso.timbre :as log]))

(def not-implemented "This API is NOT currently implemented!")
(def email-for-authorized-user "john@doe.com")
(def password-for-authorized-user "blahblah")

(defn valid-login? [email password]
  (let [user (db/get-user-by-email! email)]
    (auth/check-password password (:password user))))

(defn generate-token [req]
  (let [email (get (:params req) "email")
        password (get (:params req) "password")
        resp {:token (auth/generate-signature email password)}]
    (log/infof "Generating token for user: %s" email)
    resp))

(defn ok-json [body]
  (-> (http-response/ok (json/write-str body))
      (assoc-in [:headers "Content-Type"] "text/json")))

(defn permissions-handler [req]
  (let [token (get (:headers req) "x-token")
        email (:user (auth/unsign-token token))]
    (if (db/user-exists? email)
      (ok-json (db/get-user-permissions-by-email! email))
      (http-response/unauthorized))))

(defn generate-token-handler [req]
  (let [email (get (:params req) "email")
        password (get (:params req) "password")]
    (if (valid-login? email password)
      (ok-json (generate-token req))
      (http-response/not-found))))

(defn not-found-handler [req]
  (http-response/not-found not-implemented))

(defroutes app-routes
  (GET "/api/permissions" [] permissions-handler)
  (POST "/api/generate-token" [] generate-token-handler)
  (route/not-found not-found-handler))

(defstate rest-server
  :start (let [params (wrap-json-params #'app-routes
                                        (assoc-in site-defaults [:security :anti-forgery] false))
               port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
           (let [stop-server (server/run-server params {:port port})]
             (log/infof "Webserver started at http:/127.0.0.1:%s/" port)
             (assoc (meta stop-server) :stop stop-server)))
  :stop (when rest-server
          (log/info "Stopping webserver")
          ((:stop rest-server))))

(defn -main
  "This is our app's entry point"
  [& args]
  (mount/start))

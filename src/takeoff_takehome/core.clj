(ns takeoff-takehome.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.pprint :as pp]
            [clojure.data.json :as json]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.json :refer [wrap-json-params]]
            [takeoff-takehome.auth :as auth]
            [takeoff-takehome.db :as db]
            )
  (:gen-class)
  )

(def not-implemented "This API is NOT currently implemented!")
(def email-for-authorized-user "john@doe.com")
(def password-for-authorized-user "blahblah")

(defn login-valid? [email password]
  (let [ q (str "SELECT email, password FROM users where email = '" email "'")
        res (db/run-query! [q])
        { db_email :email
         db_password :password } (first res)]
    (and (= email db_email)
         (= password db_password))))

(defn found-user? [email]
  (let [ q (str "SELECT count(email) FROM users where email = '" email "'")
        res (db/run-query! [q])
        { count :count } (first res)]
    (= 1 count)))

(defn get-user-permissions! [email]
  (let [ q (str "SELECT permissions.permission FROM users
                 INNER JOIN user_roles ON
                   users.user_id = user_roles.user_id
                 INNER JOIN roles ON
                   roles.role_id = user_roles.role_id
                 INNER JOIN role_permissions ON
                   role_permissions.role_id = roles.role_id
                 INNER JOIN permissions ON
                   permissions.permission_id = role_permissions.permission_id
                 WHERE email ='" email "'")
        res (db/run-query! [q])]
    (map :permission res)))

(defn generate-token [req]
  (let [email (get (:params req) "email")
        password (get (:params req) "password")
        resp {:token (auth/generate-signature email password)}]
    (str (json/write-str resp))))

(defn permissions-handler [req]
  (let [token (get (:headers req) "x-token")
        email (:user (auth/unsign-token token))]
    (if (found-user? email)
      {:status  200
       :headers {"Content-Type" "text/json"}
       :body   (str (json/write-str (get-user-permissions! email)))
       }
      {:status 401
       :headers {"Content-Type" "text/json"}
       :body (str (json/write-str {"message" "Unauthorized"}))
       })
    ))

(defn generate-token-handler [req]
  (let [email (get (:params req) "email")
        password (get (:params req) "password")]
    (if (login-valid? email password)
      {:status 200
       :headers {"Content-Type" "text/json"}
       :body    (generate-token req)}
      {:status 404
       :headers {"Content-Type" "text/json"}
       :body (str (json/write-str {"message" "Wrong input data"}))
       })))

(defn not-found-handler [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    not-implemented})

(defroutes app-routes
  (GET "/api/permissions" [] permissions-handler)
  (POST "/api/generate-token" [] generate-token-handler)
  (route/not-found not-found-handler))

(defn -main
  "This is our app's entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (mount.core/start)
    (server/run-server (wrap-json-params #'app-routes (assoc-in site-defaults [:security :anti-forgery] false)) {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))

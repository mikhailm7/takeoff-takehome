(defproject takeoff-takehome "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [http-kit "2.3.0"]
                 [compojure "1.6.1"]
                 [metosin/ring-http-response "0.9.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.0"]
                 [org.clojure/data.json "0.2.6"]
                 [buddy/buddy-hashers "1.4.0"]
                 [buddy/buddy-sign "1.1.0"]
                 [org.clojure/java.jdbc "0.7.3"]
                 [org.postgresql/postgresql "42.1.4"]
                 [hikari-cp "2.10.0"]
                 [mount "0.1.12"]
                 [cprop "0.1.15"]
                 [ragtime "0.8.0"]
                 [webjure/jeesql "0.4.7"]
                 [com.taoensso/timbre "4.10.0"]]
  :main ^:skip-aot takeoff-takehome.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:source-paths ["src"]
                   :dependencies [[org.clojure/tools.namespace "0.3.1"]]}}
  :repl-options {:init-ns takeoff-takehome.core})

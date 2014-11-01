(defproject scarlett "0.1.0-SNAPSHOT"
  :description "provides macros to declare vars in namespaces other than *ns*"
  :url "https://github.com/scgilardi/scarlett"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :deploy-repositories [["releases" :clojars]]
  :global-vars {*warn-on-reflection* true}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.7.0-alpha3"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0-alpha3"]]}}
  :aliases {"all" ["with-profile" "1.5:1.6:1.7"]})

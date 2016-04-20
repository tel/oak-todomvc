(defproject oak-todomvc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.8.40"]
                 [oak "0.1.0-SNAPSHOT"]
                 [prismatic/schema "1.1.0"]
                 [org.clojure/core.match "0.3.0-alpha4"]]
  :plugins [[lein-figwheel "0.5.1"]
            [lein-cljsbuild "1.1.3"]]
  :clean-targets ^{:protect false} ["resources/public/js" "target"]
  :checkout-deps-shares [:source-paths :resource-paths :compile-path]

  :cljsbuild
  {:builds [{:id "dev"
             :source-paths ["src" "checkouts/oak/src"]
             :figwheel true
             :compiler {:main oak.examples.todomvc.core
                        :asset-path "js/out"
                        :output-to "resources/public/js/todomvc.js"
                        :output-dir "resources/public/js/out"
                        :source-map-timestamp true}}]})

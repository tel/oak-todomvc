(defproject oak-todomvc "0.1.0-SNAPSHOT"
  :description "TodoMVC using Oak components"
  :url "http://github.com/tel/oak-todomvc"
  :license {:name "BSD3"
            :url "https://opensource.org/licenses/BSD-3-Clause"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.8.40"]
                 [oak "0.1.3-SNAPSHOT"]
                 [org.clojure/core.async "0.2.374"]
                 [prismatic/schema "1.1.0"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [bidi "2.0.6"]
                 [venantius/accountant "0.1.7"]
                 [mount "0.1.10"]]
  :plugins [[lein-figwheel "0.5.1"]
            [lein-cljsbuild "1.1.3"]]
  :clean-targets ^{:protect false} ["resources/public/js" "target"]
  :checkout-deps-shares [:source-paths :resource-paths :compile-path]

  :cljsbuild
  {:builds [{:id "dev"
             :source-paths ["src" "checkouts/oak/src"]
             :figwheel {:on-jsload oak.examples.todomvc.core/reload}
             :compiler {:main oak.examples.todomvc.core
                        :asset-path "js/out"
                        :output-to "resources/public/js/todomvc.js"
                        :output-dir "resources/public/js/out"
                        :source-map-timestamp true}}]})

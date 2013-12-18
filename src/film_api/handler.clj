(ns film-api.handler
  (:use compojure.core)
  (:require
	[compojure.handler :as handler]
	[compojure.route :as route]
	[ring.middleware.json :as json-wrapper]
	[clojure.java.jdbc :as j]
	[environ.core :as env]))

(def code-film-db {:subprotocol "oracle"
	:classname "oracle.jdbc.OracleDriver"
	:subname "thin:@devoradb01.dc1.gnm:1521:gucode"
	:user (env/env :r2-db-user)
	:password (env/env :r2-db-password)})

(defn read-movie-data [movie-id]
	(let [movie-data (first (j/query code-film-db ["SELECT * FROM flm_film WHERE seq_no = ?" movie-id]))]
		(assoc movie-data :players [] :genres [])))

(defroutes app-routes
  (GET "/" [] {:body {:hello "world"}})
  (GET "/movie/:id" [id] {:body (read-movie-data id)})
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
	(->
		(handler/site app-routes)
		(json-wrapper/wrap-json-response)))

(ns film-api.handler
  (:use [compojure.core])
  (:require
	[compojure.handler :as handler]
	[compojure.route :as route]
	[ring.middleware.json :as json-wrapper]
	[clojure.java.jdbc :as j]
	[environ.core :as env]
	[yesql.core :refer [defquery defqueries]]))

(def film-db {:subprotocol "oracle"
	:classname "oracle.jdbc.OracleDriver"
	:subname (env/env :r2-db-connect)
	:user (env/env :r2-db-user)
	:password (env/env :r2-db-password)})

(defqueries "film_api/sql/queries.sql")

(defn read-movie-data [movie-id]
	(let [movie-data (first (film-by-pk film-db movie-id))
		related-players (players-by-film film-db movie-id)
		genres (map :name (genres-by-film film-db movie-id))]
		(assoc movie-data :players related-players :genres genres)))

(defn read-player-data [player-id]
	(let [player-data (first(player-by-pk film-db player-id))
		related-films (films-by-player film-db player-id)]
		(assoc player-data :filmography related-films)))

(defroutes app-routes
  (GET "/" [] {:body {:hello "world" :db-user (env/env :r2-db-user)}})
  (GET "/movie/:id" [id] {:body (read-movie-data id)})
  (GET "/player/:id" [id] {:body (read-player-data id)})
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
	(->
		(handler/site app-routes)
		(json-wrapper/wrap-json-response)))

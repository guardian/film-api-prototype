(ns film-api.handler
  (:use [compojure.core]
  	[film-api.queries])
  (:require
	[compojure.handler :as handler]
	[compojure.route :as route]
	[ring.middleware.json :as json-wrapper]
	[clojure.java.jdbc :as j]
	[environ.core :as env]))

(def film-db {:subprotocol "oracle"
	:classname "oracle.jdbc.OracleDriver"
	:subname (env/env :r2-db-connect)
	:user (env/env :r2-db-user)
	:password (env/env :r2-db-password)})

(defn read-movie-data [movie-id]
	(let [movie-data (first (j/query film-db ["SELECT * FROM flm_film WHERE seq_no = ?" movie-id]))
		related-players (j/query film-db [(:players-by-film queries) movie-id])
		genres (map :name (j/query film-db [(:genres-by-film queries) movie-id]))]
		(assoc movie-data :players related-players :genres genres)))

(defn read-player-data [player-id]
	(let [player-data (first(j/query film-db ["SELECT * FROM flm_player WHERE seq_no = ?" player-id]))
		related-films (j/query film-db [(:films-by-player queries) player-id])]
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

(ns film-api.handler
  (:use compojure.core)
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

(def queries {:players-by-film (str "SELECT plyr.*, role_type.name as role_name"
	" FROM flm_player plyr"
	" INNER JOIN flm_role plyr_role ON (player_seq_no = plyr.seq_no)"
	" INNER JOIN flm_role_type role_type ON (role_type.seq_no = plyr_role.role_type_seq_no)"
	" WHERE plyr_role.film_seq_no = ?")})

(defn read-movie-data [movie-id]
	(let [movie-data (first (j/query film-db ["SELECT * FROM flm_film WHERE seq_no = ?" movie-id]))
		related-players (j/query film-db [(:players-by-film queries) movie-id])]
		(assoc movie-data :players related-players :genres [])))

(defroutes app-routes
  (GET "/" [] {:body {:hello "world" :db-user (env/env :r2-db-user)}})
  (GET "/movie/:id" [id] {:body (read-movie-data id)})
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
	(->
		(handler/site app-routes)
		(json-wrapper/wrap-json-response)))

(ns film-api.handler
  (:use compojure.core)
  (:require
	[compojure.handler :as handler]
	[compojure.route :as route]
	[ring.middleware.json :as json-wrapper]))

(defn read-movie-data [movie-id]
	{:movie_id movie-id})

(defroutes app-routes
  (GET "/" [] {:body {:hello "world"}})
  (GET "/movie/:id" [id] {:body (read-movie-data id)})
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
	(->
		(handler/site app-routes)
		(json-wrapper/wrap-json-response)))

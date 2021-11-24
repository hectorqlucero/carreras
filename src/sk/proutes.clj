(ns sk.proutes
  (:require [compojure.core :refer [GET POST defroutes]]
            [sk.handlers.admin.users.handler :as users]
            [sk.handlers.carrera.handler :as carrera]
            [sk.handlers.categorias.handler :as categorias]
            [sk.handlers.registered.handler :as registered]
            [sk.handlers.mensajes.handler :as mensajes]
            [sk.handlers.correos.handler :as correos]
            [sk.handlers.csv.handler :as csv]))

(defroutes proutes
  ;; Start users
  (GET "/admin/users"  req [] (users/users req))
  (POST "/admin/users" req [] (users/users-grid req))
  (GET "/admin/users/edit/:id" [id] (users/users-form id))
  (POST "/admin/users/save" req [] (users/users-save req))
  (POST "/admin/users/delete" req [] (users/users-delete req))
  ;; End users

  ;; Start carrera
  (GET "/carrera"  req [] (carrera/carrera req))
  (POST "/carrera" req [] (carrera/carrera-grid req))
  (GET "/carrera/edit/:id" [id] (carrera/carrera-form id))
  (POST "/carrera/save" req [] (carrera/carrera-save req))
  (POST "/carrera/delete" req [] (carrera/carrera-delete req))
  ;; End carrera

  ;; Start categorias
  (GET "/categorias"  req [] (categorias/categorias req))
  (POST "/categorias" req [] (categorias/categorias-grid req))
  (GET "/categorias/edit/:id" [id] (categorias/categorias-form id))
  (POST "/categorias/save" req [] (categorias/categorias-save req))
  (POST "/categorias/delete" req [] (categorias/categorias-delete req))
  ;; End categorias

  ;; Start display registered
  (GET "/display/registered" req [] (registered/registered req))
  (GET "/imprimir/registered/:id" [id] (registered/imprimir id))
  (GET "/update/registered/:id/:no" [id no] (registered/update-db id no))
  ;; End display registered

  ;; Start categorias
  (GET "/mensajes"  req [] (mensajes/mensajes req))
  (POST "/mensajes" req [] (mensajes/mensajes-grid req))
  (GET "/mensajes/edit/:id/:no" [id no] (mensajes/mensajes-form id no))
  (POST "/mensajes/save" req [] (mensajes/mensajes-save req))
  (POST "/mensajes/delete" req [] (mensajes/mensajes-delete req))
  ;; End categorias

  ;; Start correos
  (GET "/correos" req [] (correos/send-emails req))
  (POST "/correos/save" req [] (correos/process-emails req))
  ;; End correos

  ;; Start csv
  (GET "/carreras/csv" [] (csv/create-csv))
  ;; End csv
  )

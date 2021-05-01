(ns sk.proutes
  (:require [compojure.core :refer [defroutes GET POST]]
            [sk.handlers.admin.users.handler :as users]
            [sk.handlers.carrera.handler :as carrera]
            [sk.handlers.categorias.handler :as categorias]))

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
  )

(ns sk.handlers.correos.model
  (:require [sk.models.crud :refer
             [Query db]]))

(defn get-active-carrera []
  (->> (Query db "select id from carrera where activa = 'S'")
       (first)
       (:id)))

(defn get-emails [carrera-id]
  (Query db ["select CONCAT(nombre,' ',apell_paterno,' ',apell_materno) as nombre,email from carreras where carrera_id = ?" carrera-id]))

(comment
  (get-active-carrera)
  (get-emails (get-active-carrera)))

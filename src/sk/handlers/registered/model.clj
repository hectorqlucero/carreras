(ns sk.handlers.registered.model
  (:require [sk.models.crud :refer [Query db]]))

(defn get-active-carrera []
  (:id (first (Query db "select id from carrera where activa='S'"))))

(def registered-sql
  "
   select * 
   from carreras 
   where carrera_id = ?
   order by
   nombre,
   apell_paterno,
   apell_materno
   ")

(defn get-registered []
  (Query db [registered-sql (get-active-carrera)]))

(defn get-carrera-name [id]
  (:descripcion (first (Query db ["select descripcion from carrera where id = ?" id]))))

(defn get-active-carrera-name []
  (let [active-carrera-id (get-active-carrera)
        carrera-name (get-carrera-name active-carrera-id)]
    carrera-name))

(def register-row-sql
  "
   select
   carreras.id,
   carreras.nombre,
   carreras.apell_paterno,
   carreras.apell_materno,
   carreras.pais,
   carreras.ciudad,
   carreras.telefono,
   carreras.email,
   carreras.sexo,
   DATE_FORMAT(carreras.fecha_nacimiento, '%d/%m/%Y') as fecha_nacimiento,
   carreras.direccion,
   carreras.club,
   carreras.carrera_id,
   carreras.categoria_id,
   DATE_FORMAT(carreras.date, '%d/%m/%Y') as date,
   carrera.p1,
   carrera.p2,
   carrera.p3,
   carrera.p4,
   carrera.d1,
   carrera.d2,
   carrera.descripcion as carrera,
   categorias.descripcion as categoria
   from carreras
   left join carrera on carreras.carrera_id = carrera.id
   left join categorias on carreras.categoria_id = categorias.id
   where carreras.id = ?
   ")

(defn get-register-row [id]
  (first (Query db [register-row-sql id])))

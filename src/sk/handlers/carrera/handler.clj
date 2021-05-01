(ns sk.handlers.carrera.handler
  (:require [sk.models.crud :refer [build-form-row
                                    build-form-save
                                    build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.models.util :refer [get-session-id
                                    user-level]]
            [sk.layout :refer [application]]
            [sk.handlers.carrera.view :refer [carrera-view carrera-scripts]]))

(defn carrera
  [_]
  (try
    (let [title "Carreras Definición"
          ok (get-session-id)
          js (carrera-scripts)
          content (carrera-view title)
          level (user-level)]
      (if
        (or
          (= (user-level) "A")
          (= (user-level) "S"))
        (application title ok js content)
        (application title ok nil "Solo <strong>Administradores</strong> pueden accesar esta opción!!!")))
    (catch Exception e (.getMessage e))))

(defn carrera-grid
  [{params :params}]
  (try
    (let [table "carrera"]
      (build-grid params table))
    (catch Exception e (.getMessage e))))

(defn carrera-form
  [id]
  (try
    (let [table "carrera"]
      (build-form-row table id))
    (catch Exception e (.getMessage e))))

(defn carrera-save
  [{params :params}]
  (try
    (let [table "carrera"]
      (build-form-save params table))
    (catch Exception e (.getMessage e))))

(defn carrera-delete
  [{params :params}]
  (try
    (let [table "carrera"]
      (build-form-delete params table))
    (catch Exception e (.getMessage e))))

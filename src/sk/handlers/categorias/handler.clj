(ns sk.handlers.categorias.handler
  (:require [sk.models.crud :refer [build-form-row
                                    build-form-save
                                    build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.models.util :refer [get-session-id
                                    user-level]]
            [sk.layout :refer [application]]
            [sk.handlers.categorias.view :refer [categorias-view categorias-scripts]]))

(defn categorias
  [_]
  (try
    (let [title "Categorias"
          ok (get-session-id)
          js (categorias-scripts)
          content (categorias-view title)
          level (user-level)]
      (if
        (or
          (= (user-level) "A")
          (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Solo <strong>administradores></strong> pueden accesar esta opción!!!")))
    (catch Exception e (.getMessage e))))

(defn categorias-grid
  [{params :params}]
  (try
    (let [table "categorias"]
      (build-grid params table))
    (catch Exception e (.getMessage e))))

(defn categorias-form
  [id]
  (try
    (let [table "categorias"]
      (build-form-row table id))
    (catch Exception e (.getMessage e))))

(defn categorias-save
  [{params :params}]
  (try
    (let [table "categorias"]
      (build-form-save params table))
    (catch Exception e (.getMessage e))))

(defn categorias-delete
  [{params :params}]
  (try
    (let [table "categorias"]
      (build-form-delete params table))
    (catch Exception e (.getMessage e))))

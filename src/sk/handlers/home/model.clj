(ns sk.handlers.home.model
  (:require [sk.models.crud :refer [Query db]]))

(defn registrar-mensaje []
  (:registrar_mensaje (first (Query db ["select registrar_mensaje from mensajes where activa='S'"]))))

(defn correo-mensaje []
  (:correo_mensaje (first (Query db ["select correo_mensaje from mensajes where activa='S'"]))))
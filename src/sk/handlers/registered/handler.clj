(ns sk.handlers.registered.handler
  (:require [sk.handlers.registered.view :refer [registered-view registered-pdf]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id]]))

(defn registered [_]
  (let [title "CORREDORES REGISTRADOS"
        ok (get-session-id)
        js nil
        content (registered-view)]
    (application title ok js content)))

(defn imprimir [id]
  (registered-pdf id))
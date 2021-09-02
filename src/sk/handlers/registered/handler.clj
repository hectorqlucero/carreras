(ns sk.handlers.registered.handler
  (:require [sk.handlers.registered.view :refer [registered-view registered-pdf registered-js]]
            [sk.layout :refer [application]]
            [cheshire.core :refer [generate-string]]
            [sk.models.crud :refer [Update db]]
            [sk.models.util :refer [get-session-id]]))

(defn registered [_]
  (let [title "CORREDORES REGISTRADOS"
        ok (get-session-id)
        js (registered-js)
        content (registered-view)]
    (application title ok js content)))

(defn imprimir [id]
  (registered-pdf id))

(defn update-db [id numero-asignado]
  (let [postvars {:numero_asignado numero-asignado}
        result (Update db :carreras postvars ["id = ?" id])]
    (if (seq result)
      (generate-string {:message "Procesado correctamente!"})
      (generate-string {:message "No se pudo asignar el numero!"}))))
(ns sk.handlers.correos.handler
  (:require 
    [cheshire.core :refer [generate-string]]
    [sk.models.crud :refer [Query db]] 
    [sk.models.email :refer [host send-email]]
    [sk.layout :refer [application]]
    [sk.handlers.correos.view :refer
     [correos-view correos-scripts]]
    [sk.models.util :refer [get-session-id]]
    [sk.handlers.correos.model :refer
     [get-active-carrera get-emails]]))

(defn send-emails [req]
  (let [title "Mandar correos a los corredores"
        ok (get-session-id)
        content (correos-view)
        js (correos-scripts)]
    (application title ok js content)))

(defn process-one-email [row]
  (let [nombre (:nombre row)
        email (:email row)
        mensaje (:mensaje row)
        email-body {:from "ciclismobc@fastmail.com"
                    :to email
                    :cp "hectorqlucero@gmail.com"
                    :subject "Informacion de Ciclismobc.org"
                    :body [{:type "text/html;charset=utf-8"
                            :content (str "Hola: " nombre "</br>" mensaje)}]}]
    (send-email host email-body)))

(defn process-all-emails [rows]
  (map process-one-email rows))

(defn process-emails [{params :params}]
  (let [mensaje (:mensaje params)
        correos-success "Se mandaron los correos correctamente!"
        correos-error "No se pudieron mandar los correos!"
        crows (get-emails (get-active-carrera))
        rows (map #(assoc % :mensaje mensaje) crows)
        result (process-all-emails rows)]
    (if result 
      (generate-string {:success correos-success}) 
      (generate-string {:error correos-error}))))

(comment
  (let [crows (get-emails (get-active-carrera))
        rows (map #(assoc % :mensaje "bla blbalb ablads blaa") crows)]
    rows)
  (process-emails {:params {:mensaje "esto es todo buddy!!!"}})
  (process-one-email {:nombre "Pedro Pacas"
                      :email "ppacas@gmail.com"
                      :mensaje "bla bla bla bla, aqui va cualquier cosa."})
  (get-emails (get-active-carrera)))

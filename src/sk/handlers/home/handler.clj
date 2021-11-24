(ns sk.handlers.home.handler
  (:require [cheshire.core :refer [generate-string]]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [noir.util.crypt :as crypt]
            [pdfkit-clj.core :refer [gen-pdf]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.handlers.home.model :refer [correo-mensaje registrar-mensaje]]
            [sk.handlers.home.view :refer [login-script login-view registrar-view registrar-view-scripts]]
            [sk.handlers.registered.view :refer [build-html]]
            [sk.handlers.registered.model :refer [get-active-carrera-name]]
            [sk.layout :refer [application]]
            [sk.models.crud :refer [Query Save build-postvars config crud-fix-id db]]
            [sk.models.email :refer [host send-email]]
            [sk.models.util :refer [get-session-id]]))

;; Start Main
(def main-sql
  "SELECT
   username
   FROM users
   WHERE id = ?")

(defn get-main-title
  []
  (try
    (let [id (get-session-id)
          title (if (> id 0)
                  (str "<strong>Usuario:</strong> " (:username (first (Query db [main-sql id]))))
                  "Clic en <strong>Conectar</strong> para accesar el sitio.")]
      title)
    (catch Exception e (.getMessage e))))
(first (:id (Query db "select id from carrera where activa = 's'")))

(defn main
  [_]
  (try
    (let [title "Carreras"
          ok (get-session-id)
          content [:div [:span {:style "margin-left:20px;"} (get-main-title)]]]
      (application title ok nil content))
    (catch Exception e (.getMessage e))))
;; End Main

;; Start registrar
(defn registrar
  [_]
  (try
    (let [title "carreras"
          ok -1
          crow (first (Query db "select * from carrera where activa = 'S'"))
          content (registrar-view (anti-forgery-field) crow)
          scripts (registrar-view-scripts)]
      (application title ok scripts content))
    (catch Exception e (.getMessage e))))

(defn email-body
  "Crear el cuerpo del correo electronico"
  [params]
  (try
    (let [nombre (str (:nombre params) " " (:apell_paterno params) " " (:apell_materno params))
          domicilio (:direccion params)
          telefono (:telefono params)
          email (:email params)
          categoria_id (:categoria_id params)
          categoria (:descripcion (first (Query db ["select descripcion from categorias where id = ?" categoria_id])))
          carrera (:descripcion (first (Query db "select * from carrera where activa = 'S'")))
          subject (str "Nuevo Registro - " carrera)
          content (str "<strong>Hola Marco</strong>,</br></br>"
                       "Mis datos son los siguientes:</br></br>"
                       "<strong>Nombre:</strong> " nombre "</br></br>"
                       "<strong>Domicilio:</strong> " domicilio "</br></br>"
                       "<strong>Telefono:</strong> " telefono "</br></br>"
                       "<strong>Email:</strong> " email "</br></br>"
                       "<strong>Categoria:</strong> " categoria)
          body {:from "ciclismobc@fastmail.com"
                :to "marcopescador@hotmail.com"
                :cc "hectorqlucero@gmail.com"
                :subject subject
                :body [{:type "text/html;charset=utf-8"
                        :content content}]}]
      body)
    (catch Exception e (.getMessage e))))

(defn corredor-email-body
  "Crear el cuerpo del correo electronico de confirmacion at corredor"
  [params id]
  (try
    (let [nombre (str (:nombre params) " " (:apell_paterno params) " " (:apell_materno params))
          email (:email params)
          subject (str "Nuevo Registro - " (get-active-carrera-name))
          content (str "<strong>Hola</strong> " nombre ",<br/>" (correo-mensaje))
          body {:from "ciclismobc@fastmail.com"
                :to email
                :cd "hectorqlucero@gmail.com"
                :subject subject
                :body [{:type "text/html;charset=utf-8"
                        :content content}
                       {:type :inline
                        :content (:path (bean (gen-pdf (build-html id))))
                        :content-type "application/pdf"}]}]
      body)
    (catch Exception e (.getMessage e))))

(defn registrar-save
  [{params :params}]
  (try
    (let [table "carreras"
          id (crud-fix-id (:id params))
          email-body (email-body params)
          postvars (build-postvars table params)
          success (registrar-mensaje)
          result (Save db (keyword table) postvars ["id = ?" id])
          corredor-email-body (corredor-email-body params (:generated_key (first result)))]
      (if (seq result)
        (do
          (send-email host email-body)
          (send-email host corredor-email-body)
          (generate-string {:success success}))
        (generate-string {:error "No se puede procesar!"})))
    (catch Exception e (.getMessage e))))
;; End registrar

;; Start Login
(defn login
  [_]
  (try
    (let [title "Conectar"
          ok (get-session-id)
          content (login-view (anti-forgery-field))
          scripts (login-script)]
      (if-not (= (get-session-id) 0)
        (redirect "/")
        (application title ok scripts content)))
    (catch Exception e (.getMessage e))))

(defn login!
  [username password]
  (try
    (let [row (first (Query db ["SELECT * FROM users WHERE username = ?" username]))
          active (:active row)]
      (if (= active "T")
        (if (crypt/compare password (:password row))
          (do
            (session/put! :user_id (:id row))
            (generate-string {:url "/"}))
          (generate-string {:error "Incapaz de accesar al sitio!"}))
        (generate-string {:error "El usuario esta inactivo!"})))
    (catch Exception e (.getMessage e))))
;; End login

(defn logoff
  []
  (try
    (session/clear!)
    (redirect "/")
    (catch Exception e (.getMessage e))))

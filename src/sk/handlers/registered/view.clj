(ns sk.handlers.registered.view
  (:require [hiccup.page :refer [html5]]
            [pdfkit-clj.core :refer [as-stream gen-pdf]]
            [sk.handlers.registered.model :refer [get-active-carrera-name get-registered get-register-row]]))

(defn my-body [row]
  (let [button-path (str "'" "/imprimir/registered/" (:id row) "'")
        comando (str "location.href = " button-path)]
    [:tr
     [:td (:nombre row)]
     [:td (:apell_paterno row)]
     [:td (:apell_materno row)]
     [:td (:pais row)]
     [:td (:ciudad row)]
     [:td (:telefono row)]
     [:td (:email row)]
     [:td (:club row)]
     [:td [:button.btn.btn-outline-primary.c6 {:type "button"
                                               :onclick comando
                                               :target "_blank"} "Registro"]]]))

(defn registered-view []
  (let [rows (get-registered)]
    [:div.container
     [:center
      [:h2 "CORREDORES REGISTRADOS"]
      [:h3 (get-active-carrera-name)]]
     [:table.table.table-striped.table-hover.table-bordered
      [:thead.table-primary
       [:tr
        [:th "Nombre"]
        [:th "Apellido Paterno"]
        [:th "Apellido Materno"]
        [:th "Pais"]
        [:th "Ciudad"]
        [:th "Telefono"]
        [:th "Email"]
        [:th "Club"]
        [:th "Imprimir"]]]
      [:tbody (map my-body rows)]]]))

(def table-style
  "border:1px solid black;border-padding:0;")

(defn build-html [id]
  (let [row (get-register-row id)]
    (html5
     [:div
      [:center [:h2 [:strong (:carrera row)]]]
      [:center [:h3 [:strong "FORMATO DE REGISTRO"]]][:span {:style "float:right;margin-left:10px;"} [:strong "identificador: " (:id row)]]
      [:span "DATOS PERSONALES:"][:span {:style "float:right;"} [:strong "Fecha: " (:date row)]]
      [:table {:style "margin-top:5px;border:1px solid black;border-radius:13px;border-spacing:0;padding:13px;font-size:0.9em;width:100%;"}
       [:tbody
        [:tr
         [:td {:colspan 3
               :style table-style} [:strong "Nombre: "] (str (:nombre row) "&nbsp;"(:apell_paterno row) "&nbsp;"(:apell_materno row))]]
        [:tr
         [:td {:style table-style} [:strong "Categoria: "] (:categoria row)]
         [:td {:colspan 2
               :style table-style} [:strong  "Telefono: "] (:telefono row)]]
        [:tr
         [:td {:style table-style} [:strong "Ciudad: "] (:ciudad row)]
         [:td {:style table-style} [:strong "Club: "] (:club row)]
         [:td {:style table-style} [:strong "Sexo: "] (:sexo row)]]
        [:tr
         [:td {:colspan 3
               :style table-style} [:strong "Email: "] (:email row)]]
        [:tr
         [:td {:colspan 3
               :style table-style} [:strong "Dirección: "] (:direccion row)]]]]
      [:table {:style "margin-top:5px;border:1px solid black;border-radius:13px;border-spacing:0;padding:13px;font-size:0.9em;width:100%;"}
       [:tbody
        [:tr
         [:td {:style table-style} [:strong "Fecha de Registro: "] [:span {:style "float:right;"} [:strong "No: "]"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]]]
        [:tr
         [:td {:style table-style}[:strong "Costo del Evento el día de registro $ : "]]]]]
      [:table {:style "margin-top:5px;border:1px solid black;border-radius:13px;border-spacing:0;padding:13px;font-size:0.9em;width:100%;"}
       [:tbody
        [:tr {:align "center"}
         [:td [:strong "Políticas y reglas del Evento"]]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} (:p1 row)]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} "&nbsp;&nbsp;"]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} (:p2 row)]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} "&nbsp;&nbsp;"]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} (:p3 row)]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} "&nbsp;&nbsp;"]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} (:p4 row)]]
        [:tr {:align "center"}
         [:td {:style "font-size:0.7em;"}[:strong (:d1 row)]]]
        [:tr {:align "center"}
         [:td {:style "font-size:0.7em;"}[:strong (:d2 row)]]]]]
         [:br]
         [:br]
         [:br]
         [:br]
         [:div {:style "width:100%;font-size:1em;font-weight:bold;text:center;"}
          [:center [:hr {:style "width:70%;"}]]
          [:center [:p [:strong "Firma y Aceptación del participante y/o tutor:"]]]]])))

(defn registered-pdf [id]
  (let [html (build-html id)
        filename (str "registro_" id ".pdf")]
    {:headers {"Content-Type" "application/pdf"
               "Content-Disposition" (str "attachment;filename=" filename)
               "Cache-Control" "no-cache,no-store,max-age=0,must-revalidate,pre-check=0,post-check=0"}
     :body (as-stream (gen-pdf html
                               :margin {:top 20 :right 15 :bottom 50 :left 15}))}))

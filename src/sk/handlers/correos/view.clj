(ns sk.handlers.correos.view
  (:require [hiccup.page :refer [include-js]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.util :refer
             [build-form build-field build-button]]))

(defn correos-fields []
  (build-field
    {:id "mensaje"
     :name "mensaje"
     :class "easyui-textbox"
     :prompt "Mesaje para los corredores aqui"
     :data-options "label:'Mandar este Mesaje:',
                   labelPosition:'top',
                   required:true,
                   multiline:true,
                   width:'100%',
                   height:300"}))

(defn correos-buttons []
  (build-button
    {:href "javascript:void(0)"
     :id "submit"
     :text "Mandar Correos"
     :class "easyui-linkbutton c6"
     :onClick "saveItem()"}))

(defn correos-view []
  (build-form
    "Mandar correos"
    (anti-forgery-field)
    (correos-fields)
    (correos-buttons)))

(defn correos-scripts []
  (include-js "/js/form.js"))

(comment
  (correos-view))

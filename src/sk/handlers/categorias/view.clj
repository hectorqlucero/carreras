(ns sk.handlers.categorias.view
  (:require [hiccup.page :refer [include-js]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.util :refer [build-table
                                    build-dialog
                                    build-dialog-buttons
                                    build-radio-buttons
                                    build-toolbar
                                    build-field]]))

(def dialog-fields
  (list
    [:input {:type "hidden" :id "id" :name "id"}]
    (build-field
      {:id "descripcion"
       :name "descripcion"
       :class "easyui-textbox"
       :data-options "label:'Descripción:',
                     labelPosition:'top',
                     width:'100%',
                     required:true"})
    (build-radio-buttons
      "Activa?"
      (list
        {:id "activa_no"
         :name "activa"
         :class "easyui-radiobutton"
         :value "N"
         :data-options "label:'No',checked:true"}
        {:id "activa_si"
         :name "activa"
         :class "easyui-radiobutton"
         :value "S"
         :data-options "label:'Si'"}))))

(defn categorias-view [title]
  (list
    (anti-forgery-field)
    (build-table
      title
      "/categorias"
      (list
        [:th {:data-options "field:'id',sortable:true"} "ID"]
        [:th {:data-options "field:'descripcion',sortable:true"} "Categoria"]
        [:th {:data-options "field:'activa',sortable:true"} "Activa"]))
    (build-toolbar)
    (build-dialog title dialog-fields)))

(defn categorias-scripts []
  (include-js "js/grid.js"))

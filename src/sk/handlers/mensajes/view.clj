(ns sk.handlers.mensajes.view
  (:require [hiccup.page :refer [include-js]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.util :refer [build-dialog build-field build-radio-buttons build-table build-toolbar]]))

(def dialog-fields
  (list
   [:input {:type "hidden" :id "id" :name "id"}]
   (build-field
    {:id "registrar_mensaje"
     :name "registrar_mensaje"
     :class "easyui-textbox"
     :prompt "El mensaje que aparecera despues de precionar el boton de registrar"
     :data-options "label:'Registrar Mensaje:',
                 labelPosition:'top',
                 required:true,
                 multiline:true,
                 width:'100%',
                 height:120"})
   (build-field
    {:id "correo_mensaje"
     :name "correo_mensaje"
     :class "easyui-textbox"
     :prompt "Este mensaje aparecera en el correo del corredor"
     :data-options "label:'Correo Mensaje:',
                 labelPosition:'top',
                 required:true,
                 multiline:true,
                 width:'100%',
                 height:120"})
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

(defn mensajes-view
  "Esto crea el grid y la forma en una ventana"
  [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/mensajes"
    (list ;; Aqui los campos del grid
     [:th {:data-options "field:'registrar_mensaje',sortable:true,width:33"} "Registrar Mensaje"]
     [:th {:data-options "field:'correo_mensaje',sortable:true,width:33"} "Correo Mensaje"]
     [:th {:data-options "field:'activa',sortable:true,width:10"} "Activo?"]))
   (build-toolbar)
   (build-dialog title dialog-fields)))

(defn mensajes-scripts
  "Esto crea el javascript necesario"
  []
  (include-js "/js/grid.js"))

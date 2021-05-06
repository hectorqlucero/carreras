(ns sk.handlers.home.view
  (:require
    [hiccup.page :refer [include-js]]
    [sk.models.crud :refer [config]]
    [sk.models.util :refer [build-form
                            build-field
                            build-radio-buttons
                            build-button]]))

(defn registrar-fields
  [carrera_id]
  (list
    [:input {:type "hidden" :name "id" :id "id"}]
    [:input {:type "hidden" :name "carrera_id" :id "carrera_id" :value (str carrera_id)}]
    (build-field
      {:id "nombre"
       :name "nombre"
       :class "easyui-textbox"
       :prompt "Nombre aqui..."
       :data-options "label:'Nombre:',
                     labelPosition:'top',
                     required:true,
                     width:'100%'"})
    (build-field
      {:id "apell_paterno"
       :name "apell_paterno"
       :class "easyui-textbox"
       :prompt "Apellido paterno aqui..."
       :data-options "label:'Apellido paterno:',
                     labelPosition:'top',
                     required:true,
                     width:'100%'"})
    (build-field
      {:id "apell_materno"
       :name "apell_materno"
       :class "easyui-textbox"
       :prompt "Apellido materno aqui..."
       :data-options "label:'Apellido materno:',
                     labelPosition:'top',
                     required:true,
                     width:'100%'"})
    (build-field
      {:id "pais"
       :name "pais"
       :class "easyui-textbox"
       :prompt "Pais aqui..."
       :data-options "label:'Pais:',
                     labelPosition:'top',
                     required:true,
                     width:'100%'"})
    (build-field
      {:id "ciudad"
       :name "ciudad"
       :class "easyui-textbox"
       :prompt "Ciudad aqui..."
       :data-options "label:'Ciudad:',
                     labelPosition:'top',
                     required:true,
                     width:'100%'"})
    (build-field
      {:id "telefono"
       :name "telefono"
       :class "easyui-maskedbox"
       :mask "(999) 999-9999"
       :data-options "label:'Telefono:',labelPosition:'top',width:'100%'"})
    (build-field
      {:id "email"
       :name "email"
       :class "easyui-textbox easyui-validatebox"
       :prompt "Email aqui..."
       :validType "email"
       :data-options "label:'Email:',labelPosition:'top',required:true,width:'100%'"})
    (build-radio-buttons
      "Sexo:"
      (list
        {:id "sexo_m"
         :name "sexo"
         :class "easyui-radiobutton"
         :value "M"
         :data-options "label:'Masculino',checked:true"}
        {:id "sexo_f"
         :name "sexo"
         :class "easyui-radiobutton"
         :value "F"
         :data-options "label:'Femenino'"}))
    (build-field
      {:id "fecha_nacimiento"
       :name "fecha_nacimiento"
       :class "easyui-datebox"
       :prompt "mm/dd/aaaa ejemplo: 02/07/1957  es Febrero 7 1957"
       :data-options "label:'Fecha de Nacimiento:',labelPosition:'top',required:true,width:'100%'"})
    (build-field
      {:id "direccion"
       :name "direccion"
       :class "easyui-textbox"
       :prompt "Domicilio aqui..."
       :data-options "label:'Domicilio:', labelPosition:'top', required:false, width:'100%'"})
    (build-field
      {:id "club"
       :name "club"
       :class "easyui-textbox"
       :prompt "Si no pertenece a un club responda 'ninguno'"
       :data-options "label:'Club:', labelPosition:'top', required:true, width:'100%'"})
    (build-field
      {:id "categoria_id"
       :name "categoria_id"
       :class "easyui-combobox"
       :data-options "label:'Categoria:',
                     labelPosition:'top',
                     url:'/table_ref/categorias',
                     method:'GET',
                     required:true,
                     width:'100%'"})))

(def registrar-buttons
  (build-button
    {:href "javascript:void(0)"
     :id "submit"
     :text "Registrarse"
     :class "easyui-linkbutton c6"
     :onClick "saveItem()"}))

(defn registrar-view [token crow]
  (build-form
    (str "Registrarse - " (:descripcion crow))
    token
    (registrar-fields (:id crow))
    (if (nil? crow) nil registrar-buttons)))

(defn registrar-view-scripts
  []
 (include-js "/js/form.js"))

(defn login-view [token]
  (build-form
    "Conectar"
   token
   (list
    (build-field
     {:id "username"
      :name "username"
      :class "easyui-textbox"
      :prompt "Email aqui..."
      :validType "email"
      :data-options "label:'Email:',labelPosition:'top',required:true,width:'100%'"})
    (build-field
     {:id "password"
      :name "password"
      :class "easyui-passwordbox"
      :prompt "Contraseña aqui..."
      :data-options "label:'Contraseña:',labelPosition:'top',required:true,width:'100%'"})
    (build-button
     {:href "javascript:void(0)"
      :id "submit"
      :text "Acceder al sitio"
      :class "easyui-linkbutton c6"
      :onClick "submitForm()"}))
   (list
    [:div {:style "margin-bottom:10px;"}
     [:a {:href "/register"} "Clic para registrarse"]]
    [:div {:style "margin-bottom:10px;"}
     [:a {:href "/rpaswd"} "Clic para resetear su contraseña"]])))

(defn login-script []
  [:script
   "
    function submitForm() {
        $('.fm').form('submit', {
            onSubmit:function() {
                if($(this).form('validate')) {
                  $('a#submit').linkbutton('disable');
                  $('a#submit').linkbutton({text: 'Processando!'});
                }
                return $(this).form('enableValidation').form('validate');
            },
            success: function(data) {
                try {
                    var dta = JSON.parse(data);
                    if(dta.hasOwnProperty('url')) {
                        window.location.href = dta.url;
                    } else if(dta.hasOwnProperty('error')) {
                        $.messager.show({
                            title: 'Error: ',
                            msg: dta.error
                        });
                        $('a#submit').linkbutton('enable');
                        $('a#submit').linkbutton({text: 'Acceder al sitio'});
                    }
                } catch(e) {
                    console.error('Invalid JSON');
                }
            }
        });
    }
   "])

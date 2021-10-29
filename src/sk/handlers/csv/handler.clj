(ns sk.handlers.csv.handler
  (:require 
    [sk.models.crud :refer [Query db]]
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]))

(def get-carreras-sql
  "SELECT
  c.id,
  c.nombre,
  c.apell_paterno,
  c.apell_materno,
  c.pais,
  c.ciudad,
  c.telefono,
  c.email,
  c.sexo,
  DATE_FORMAT(c.fecha_nacimiento,'%d/%m/%Y') as fecha_nacimiento,
  c.direccion,
  c.club,
  a.descripcion as carrera,
  b.descripcion as categoria,
  c.numero_asignado
  FROM carreras c
  JOIN carrera a on a.id = c.carrera_id
  JOIN categorias b on b.id = c.categoria_id
  ORDER BY
  carrera,
  b.descripcion,
  c.nombre,
  c.apell_paterno,
  c.apell_materno
  ")

(defn get-carreras-rows []
  (let [rows (Query db get-carreras-sql)]
    (conj rows {:email "EMAIL"
                :categoria "CATEGORIA"
                :apell_materno "APELL MATERNO"
                :pais "PAIS"
                :numero_asignado "NUMERO ASIGNADO"
                :fecha_nacimiento "FECHA NACIMIENTO"
                :sexo "SEXO"
                :direccion "DIRECCION"
                :id "ID"
                :apell_paterno "APELL PATERNO"
                :ciudad "CIUDAD"
                :club "CLUB"
                :carrera "CARRERA"
                :telefono "TELEFONO"
                :nombre "NOMBRE"})))

(defn get-values [row]
  (vals row))

(defn convert-to-csv []
  (with-open [writer (io/writer "out-file.csv")]
    (csv/write-csv writer
                   (map get-values (get-carreras-rows)))))

(defn create-csv []
  (convert-to-csv)
  {:status 200
   :headers {"Content-Type" "text/csv"
             "Content-Disposition" "attachment;filename=carreras.csv"}
   :body (slurp "out-file.csv")})

(comment
  (get-carreras-rows)
  (create-csv)
  (convert-to-csv {})
  (map get-values (get-carreras-rows)))

(ns sk.models.cdb
  (:require [sk.models.crud :refer [db
                                    Query!
                                    Insert-multi]]
            [noir.util.crypt :as crypt]))


;; Start users table
(def users-sql
  "CREATE TABLE users (
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  lastname varchar(45) DEFAULT NULL,
  firstname varchar(45) DEFAULT NULL,
  username varchar(45) DEFAULT NULL,
  password TEXT DEFAULT NULL,
  dob date DEFAULT NULL,
  cell varchar(45) DEFAULT NULL,
  phone varchar(45) DEFAULT NULL,fax varchar(45) DEFAULT NULL,
  email varchar(100) DEFAULT NULL,
  level char(1) DEFAULT NULL COMMENT 'A=Administrator,U=User,S=System',
  active char(1) DEFAULT NULL COMMENT 'T=Active,F=Not active',
  imagen varchar(200) DEFAULT NULL,
  UNIQUE KEY username (username)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8")

(def users-rows
  [{:lastname  "User"
    :firstname "Regular"
    :username  "user@gmail.com"
    :password  (crypt/encrypt "user")
    :dob       "1957-02-07"
    :email     "user@gmail.com"
    :level     "U"
    :active    "T"}
   {:lastname "User"
    :firstname "Admin"
    :username "admin@gmail.com"
    :password (crypt/encrypt "admin")
    :dob "1957-02-07"
    :email "admin@gmail.com"
    :level "S"
    :active "T"}])
;; End users table

;; Start carrera table
(def carrera-sql
  "CREATE TABLE carrera (
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  descripcion varchar(200) DEFAULT NULL,
  activa char(1) DEFAULT NULL COMMENT 'S=si,N=No'
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8")
;; End carrera table

;; Start categorias table
(def categorias-sql
  "CREATE TABLE categorias (
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  descripcion varchar(200) DEFAULT NULL,
  activa char(1) DEFAULT NULL COMMENT 'S=si,N=No'
  ) ENGINE InnoDB DEFAULT CHARSET=utf8")
;; End categorias table

;; Start carreras table
(def carreras-sql
  "CREATE TABLE carreras (
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nombre varchar(100) DEFAULT NULL,
  apell_paterno varchar(100) DEFAULT NULL,
  apell_materno varchar(100) DEFAULT NULL,
  pais varchar(100) DEFAULT NULL,
  ciudad varchar(100) DEFAULT NULL,
  telefono varchar(100) DEFAULT NULL,
  email varchar(100) DEFAULT NULL,
  sexo varchar(100) DEFAULT NULL,
  fecha_nacimiento date DEFAULT NULL,
  direccion varchar(200) DEFAULT NULL,
  club varchar(100) DEFAULT NULL,
  carrera_id int NOT NULL,
  categoria_id int NOT NULL,
  CONSTRAINT fk_carreras_carrera_id FOREIGN KEY (carrera_id) REFERENCES carrera (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_carreras_categoria_id FOREIGN KEY (categoria_id) REFERENCES categorias (id) ON UPDATE CASCADE ON DELETE CASCADE
  ) ENGINE=InnoDB CHARSET=utf8")
;; End carreras table

(defn drop-tables 
  "Drops tables if they exist"
  []
  (Query! db "DROP table IF EXISTS carreras")
  (Query! db "DROP table IF EXISTS categorias")
  (Query! db "DROP table IF EXISTS carrera")
  (Query! db "DROP table IF EXISTS users"))

(defn create-tables
  "Creates tables"
  []
  (Query! db users-sql)
  (Query! db carrera-sql)
  (Query! db categorias-sql)
  (Query! db carreras-sql))

(defn populate-tables
  "Populates table with default data"
  []
  (Query! db "LOCK TABLES users WRITE;")
  (Insert-multi db :users users-rows)
  (Query! db "UNLOCK TABLES;"))

(defn reset-database
  "Removes existing tables and re-creates them"
  []
  (drop-tables)
  (create-tables)
  (populate-tables))

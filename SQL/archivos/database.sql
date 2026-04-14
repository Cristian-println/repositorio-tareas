CREATE DATABASE IF NOT EXISTS classroom_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE classroom_db;

CREATE TABLE IF NOT EXISTS materias (
    id      INT PRIMARY KEY AUTO_INCREMENT,
    nombre  VARCHAR(100) NOT NULL,
    codigo  VARCHAR(20)  NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS docentes (
    id      INT PRIMARY KEY AUTO_INCREMENT,
    nombre  VARCHAR(100) NOT NULL,
    email   VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS estudiantes (
    id      INT PRIMARY KEY AUTO_INCREMENT,
    nombre  VARCHAR(100) NOT NULL,
    codigo  VARCHAR(20)  NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS inscripciones (
    id             INT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id  INT NOT NULL,
    materia_id     INT NOT NULL,
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE,
    FOREIGN KEY (materia_id)    REFERENCES materias(id)    ON DELETE CASCADE,
    UNIQUE KEY uk_inscripcion (estudiante_id, materia_id)
);

CREATE TABLE IF NOT EXISTS tareas (
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    titulo               VARCHAR(200)   NOT NULL,
    descripcion          TEXT,
    fecha_limite         DATETIME       NOT NULL,
    calificacion_maxima  DECIMAL(5,2)   NOT NULL DEFAULT 100.00,
    archivo_adjunto      VARCHAR(500),
    materia_id           INT            NOT NULL,
    docente_id           INT            NOT NULL,
    fecha_creacion       DATETIME       DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (materia_id)  REFERENCES materias(id)  ON DELETE CASCADE,
    FOREIGN KEY (docente_id)  REFERENCES docentes(id)  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS entregas (
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    tarea_id              INT          NOT NULL,
    estudiante_id         INT          NOT NULL,
    archivo_ruta          VARCHAR(500),
    comentario_estudiante TEXT,
    fecha_entrega         DATETIME     DEFAULT CURRENT_TIMESTAMP,
    es_tardio             TINYINT(1)   DEFAULT 0,
    FOREIGN KEY (tarea_id)      REFERENCES tareas(id)      ON DELETE CASCADE,
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE,
    UNIQUE KEY uk_entrega (tarea_id, estudiante_id)
);

CREATE TABLE IF NOT EXISTS calificaciones (
    id                INT PRIMARY KEY AUTO_INCREMENT,
    entrega_id        INT          NOT NULL UNIQUE,
    nota              DECIMAL(5,2) NOT NULL,
    comentario        TEXT,
    fecha_calificacion DATETIME    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (entrega_id) REFERENCES entregas(id) ON DELETE CASCADE
);

INSERT INTO materias (nombre, codigo) VALUES
    ('Programación I',   'PROG01'),
    ('Base de Datos',    'BD01'),
    ('Algoritmos',       'ALG01'),
    ('Redes',            'RED01');

INSERT INTO docentes (nombre, email) VALUES
    ('Prof. García',    'garcia@edu.com'),
    ('Prof. Martínez',  'martinez@edu.com');

INSERT INTO estudiantes (nombre, codigo) VALUES
    ('Juan Pérez',   'EST001'),
    ('María López',  'EST002'),
    ('Carlos Ruiz',  'EST003'),
    ('Ana Torres',   'EST004'),
    ('Luis Mendoza', 'EST005');

INSERT INTO inscripciones (estudiante_id, materia_id) VALUES
    (1,1),(1,2),(1,3),
    (2,1),(2,2),(2,4),
    (3,1),(3,3),
    (4,2),(4,4),
    (5,1),(5,2),(5,3);

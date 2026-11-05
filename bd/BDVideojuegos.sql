DROP DATABASE IF EXISTS roqui_games;

CREATE DATABASE roqui_games
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE roqui_games;

-- ============================================================
-- 1. USUARIOS
-- ============================================================

CREATE TABLE usuarios (
                          id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(512) NOT NULL,
                          apellidos VARCHAR(512),
                          nick VARCHAR(50) NOT NULL UNIQUE,
                          email VARCHAR(512) NOT NULL,
                          email_hash CHAR(64) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          rol ENUM('CLIENTE', 'TENDERO', 'ADMIN') NOT NULL DEFAULT 'CLIENTE',
                          fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- ============================================================
-- 2. PLATAFORMAS
-- ============================================================

CREATE TABLE plataformas (
                             id_plataforma INT AUTO_INCREMENT PRIMARY KEY,
                             nombre VARCHAR(100) NOT NULL UNIQUE
);

-- ============================================================
-- 3. GENEROS
-- ============================================================

CREATE TABLE generos (
                         id_genero INT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(80) NOT NULL UNIQUE
);

-- ============================================================
-- 4. VIDEOJUEGOS
-- ============================================================

CREATE TABLE videojuegos (
                             id_videojuego INT AUTO_INCREMENT PRIMARY KEY,
                             codigo_producto VARCHAR(50) NOT NULL UNIQUE,
                             titulo VARCHAR(200) NOT NULL,
                             descripcion TEXT,
                             anio_lanzamiento INT,
                             id_plataforma INT NOT NULL,
                             precio_compra DECIMAL(10,2),
                             precio_alquiler DECIMAL(10,2),
                             disponible_compra BOOLEAN NOT NULL DEFAULT TRUE,
                             disponible_alquiler BOOLEAN NOT NULL DEFAULT FALSE,
                             activo BOOLEAN NOT NULL DEFAULT TRUE,
                             FOREIGN KEY (id_plataforma) REFERENCES plataformas(id_plataforma),
                             CHECK (precio_compra IS NULL OR precio_compra >= 0),
                             CHECK (precio_alquiler IS NULL OR precio_alquiler >= 0)
);

-- ============================================================
-- 5. VIDEOJUEGOS_GENEROS
-- ============================================================

CREATE TABLE videojuegos_generos (
                                     id_videojuego INT NOT NULL,
                                     id_genero INT NOT NULL,
                                     PRIMARY KEY (id_videojuego, id_genero),
                                     FOREIGN KEY (id_videojuego) REFERENCES videojuegos(id_videojuego),
                                     FOREIGN KEY (id_genero) REFERENCES generos(id_genero)
);

-- ============================================================
-- 6. EJEMPLARES
-- ============================================================

CREATE TABLE ejemplares (
                            id_ejemplar INT AUTO_INCREMENT PRIMARY KEY,
                            id_videojuego INT NOT NULL,
                            codigo_ejemplar VARCHAR(60) NOT NULL UNIQUE,
                            estado ENUM('DISPONIBLE', 'ALQUILADO', 'VENDIDO', 'BAJA')
        NOT NULL DEFAULT 'DISPONIBLE',
                            observaciones VARCHAR(500),
                            FOREIGN KEY (id_videojuego) REFERENCES videojuegos(id_videojuego)
);

-- ============================================================
-- 7. PEDIDOS
-- ============================================================

CREATE TABLE pedidos (
                         id_pedido INT AUTO_INCREMENT PRIMARY KEY,
                         id_usuario INT NOT NULL,
                         fecha_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         estado ENUM('PENDIENTE', 'CONFIRMADO', 'COMPLETADO', 'CANCELADO')
        NOT NULL DEFAULT 'PENDIENTE',
                         importe_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                         FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
                         CHECK (importe_total >= 0)
);

-- ============================================================
-- 8. DETALLES_PEDIDO
-- ============================================================

CREATE TABLE detalles_pedido (
                                 id_detalle INT AUTO_INCREMENT PRIMARY KEY,
                                 id_pedido INT NOT NULL,
                                 id_videojuego INT NOT NULL,
                                 id_ejemplar INT NOT NULL,
                                 codigo_producto VARCHAR(50) NOT NULL,
                                 codigo_ejemplar VARCHAR(60) NOT NULL,
                                 nombre_videojuego VARCHAR(200) NOT NULL,
                                 tipo_operacion ENUM('COMPRA', 'ALQUILER') NOT NULL,
                                 precio_unitario DECIMAL(10,2) NOT NULL,
                                 FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido),
                                 FOREIGN KEY (id_videojuego) REFERENCES videojuegos(id_videojuego),
                                 FOREIGN KEY (id_ejemplar) REFERENCES ejemplares(id_ejemplar),
                                 CHECK (precio_unitario >= 0),
                                 INDEX idx_detalles_pedido (id_pedido),
                                 INDEX idx_detalles_ejemplar (id_ejemplar)
);

-- ============================================================
-- 9. VENTAS
-- ============================================================

CREATE TABLE ventas (
                        id_venta INT AUTO_INCREMENT PRIMARY KEY,
                        id_detalle INT NOT NULL UNIQUE,
                        fecha_venta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        importe DECIMAL(10,2) NOT NULL,
                        estado ENUM('REGISTRADA', 'ANULADA') NOT NULL DEFAULT 'REGISTRADA',
                        FOREIGN KEY (id_detalle) REFERENCES detalles_pedido(id_detalle),
                        CHECK (importe >= 0)
);

-- ============================================================
-- 10. ALQUILERES
-- ============================================================

CREATE TABLE alquileres (
                            id_alquiler INT AUTO_INCREMENT PRIMARY KEY,
                            id_detalle INT NOT NULL UNIQUE,
                            fecha_inicio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            fecha_devolucion_prevista DATE NOT NULL,
                            fecha_devolucion_real DATETIME,
                            estado ENUM('ACTIVO', 'DEVUELTO', 'RETRASADO', 'CANCELADO')
        NOT NULL DEFAULT 'ACTIVO',
                            FOREIGN KEY (id_detalle) REFERENCES detalles_pedido(id_detalle)
);

-- ============================================================
-- 11. VALORACIONES
-- ============================================================

CREATE TABLE valoraciones (
                              id_valoracion INT AUTO_INCREMENT PRIMARY KEY,
                              id_usuario INT NOT NULL,
                              id_videojuego INT NOT NULL,
                              puntuacion TINYINT NOT NULL,
                              critica TEXT,
                              fecha_valoracion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
                              FOREIGN KEY (id_videojuego) REFERENCES videojuegos(id_videojuego),
                              UNIQUE (id_usuario, id_videojuego),
                              CHECK (puntuacion BETWEEN 1 AND 10)
);

-- ============================================================
-- DATOS DE PRUEBA
-- ============================================================

-- PLATAFORMAS

INSERT INTO plataformas (nombre) VALUES
                                     ('PC'),
                                     ('PlayStation'),
                                     ('PlayStation 2'),
                                     ('PlayStation 3'),
                                     ('PlayStation 4'),
                                     ('PlayStation 5'),
                                     ('Xbox'),
                                     ('Xbox 360'),
                                     ('Nintendo Entertainment System'),
                                     ('Super Nintendo'),
                                     ('Nintendo 64'),
                                     ('Game Boy'),
                                     ('Game Boy Advance'),
                                     ('Nintendo DS'),
                                     ('Nintendo Switch'),
                                     ('Sega Mega Drive');

-- GENEROS

INSERT INTO generos (nombre) VALUES
                                 ('Acción'),
                                 ('Aventura'),
                                 ('Plataformas'),
                                 ('Rol (RPG)'),
                                 ('Acción RPG'),
                                 ('Estrategia'),
                                 ('Deportes'),
                                 ('Carreras'),
                                 ('Lucha'),
                                 ('Disparos'),
                                 ('Terror'),
                                 ('Supervivencia'),
                                 ('Puzles'),
                                 ('Simulación'),
                                 ('Metroidvania');

-- ============================================================
-- USUARIOS DE PRUEBA
-- ============================================================
-- Las contraseñas están almacenadas mediante BCrypt.
-- Los email_hash se han generado con HMAC-SHA-256.
-- ============================================================

INSERT INTO usuarios
(nombre, apellidos, nick, email, email_hash, password, rol)
VALUES
    (
        'Carlos',
        'Pérez López',
        'CarlosRetro',
        'carlos@example.com',
        'c3f5e7ffdfdf8751907378864c086699cd0ff3e06a54792ee244018772c88942',
        '$2y$10$A.fUQOO3aXL6nZv0HmHqsO1tReranIUzizepCXFTSlv0Uig/weZjG',
        'CLIENTE'
    ),
    (
        'Laura',
        'García Martín',
        'LauraRetro',
        'laura@example.com',
        'c9a3a07c6bbfa2eebec50ce139fc6c59bffed4bc177faf2193b6fa05240b0213',
        '$2y$10$JuG34YJGsENNs4TCyfCrHOgOqA9EVpfU9tElBR9qMOHcqxW8Wr3y.',
        'CLIENTE'
    ),
    (
        'Miguel',
        'Sánchez Ruiz',
        'MiguelTienda',
        'miguel@example.com',
        '90600c68e07a635589f3f492e594311e90dc328b16d6fd2cf45bab479150607f',
        '$2y$10$MS9Q2BpSPI8khOdHobTt8.iMIBmIX9IqTHJ9tkMxkDQ87NH4TK.uC',
        'TENDERO'
    ),
    (
        'Administrador',
        'Roqui Games',
        'AdminRoqui',
        'admin@example.com',
        '62108bbd6255ad4ced93b33d9b5fc5d4a42f852f1edbfac8f1bef8c7c557fd4d',
        '$2y$10$L6/8RDIxcDV2ux1.PaQtJOWEuZemNrAHhE0azaZjeT2VpJQ6RusAm',
        'ADMIN'
    );

-- ============================================================
-- VIDEOJUEGOS
-- ============================================================

INSERT INTO videojuegos
(codigo_producto, titulo, descripcion, anio_lanzamiento,
 id_plataforma, precio_compra, precio_alquiler,
 disponible_compra, disponible_alquiler)
VALUES
    (
        'NES-MARIO-001',
        'Super Mario Bros.',
        'Acompaña a Mario en una aventura clásica a través del Reino Champiñón para rescatar a la princesa Peach de las garras de Bowser. Recorre niveles llenos de plataformas, enemigos, tuberías secretas y bloques sorpresa mientras buscas monedas y potenciadores. Un título fundamental de la historia de los videojuegos que destaca por su diseño de niveles preciso, sus controles sencillos y una dificultad que recompensa la habilidad.',
        1985, 9, 25.00, 5.00, TRUE, TRUE
    ),
    (
        'SNES-ZELDA-001',
        'The Legend of Zelda: A Link to the Past',
        'Explora el reino de Hyrule en una aventura que combina exploración, combates, resolución de puzles y mazmorras repletas de secretos. Link deberá reunir objetos legendarios, enfrentarse a peligrosos enemigos y descubrir la conexión entre el Mundo de la Luz y el Mundo Oscuro para detener una amenaza que pone en peligro el reino. Su mundo interconectado, sus mazmorras ingeniosas y su variedad de herramientas lo convierten en una de las grandes aventuras de la era de los 16 bits.',
        1991, 10, 35.00, 7.00, TRUE, TRUE
    ),
    (
        'PS2-GTA-001',
        'Grand Theft Auto: San Andreas',
        'Ponte en la piel de Carl Johnson, conocido como CJ, que regresa a Los Santos tras conocer la muerte de su madre y descubre que su barrio y su familia atraviesan una situación complicada. Recorre un enorme mundo abierto inspirado en California y Nevada, participa en misiones de la historia, conduce una gran variedad de vehículos y explora ciudades, pueblos y zonas rurales. Su libertad de acción, la personalización del personaje y la amplitud de sus actividades lo convirtieron en uno de los títulos más emblemáticos de PlayStation 2.',
        2004, 3, 20.00, 4.00, TRUE, TRUE
    ),
    (
        'MD-SONIC-001',
        'Sonic the Hedgehog',
        'Controla a Sonic, el veloz erizo azul, mientras atraviesas escenarios coloridos a toda velocidad para frustrar los planes del malvado Dr. Robotnik. Corre por colinas, bucles y plataformas, recoge anillos para protegerte de los golpes y descubre rutas alternativas escondidas en cada zona. Su combinación de velocidad, reflejos y exploración, junto con su música y su estilo visual, ayudó a convertirlo en uno de los grandes iconos de Sega y de los juegos de plataformas.',
        1991, 16, 18.00, 3.00, TRUE, TRUE
    ),
    (
        'PC-DOOM-001',
        'DOOM',
        'Adéntrate en una instalación marciana invadida por criaturas demoníacas tras un experimento que ha abierto una puerta al infierno. Armado con un arsenal de armas, tendrás que recorrer complejos laberínticos, encontrar llaves, descubrir zonas secretas y sobrevivir a oleadas de enemigos cada vez más peligrosos. Su ritmo frenético, sus enfrentamientos directos y su diseño de niveles contribuyeron a definir el género de los disparos en primera persona.',
        1993, 1, 15.00, NULL, TRUE, FALSE
    ),
    (
        'GBA-POKEMON-001',
        'Pokémon Esmeralda',
        'Viaja por la región de Hoenn como entrenador Pokémon y emprende una aventura en la que tendrás que capturar criaturas, entrenarlas y formar un equipo capaz de superar los desafíos de los gimnasios. Durante el recorrido te enfrentarás al Equipo Aqua y al Equipo Magma, cuyos planes amenazan el equilibrio de la región, mientras descubres el papel de Pokémon legendarios como Groudon, Kyogre y Rayquaza. La aventura combina exploración, estrategia por turnos, intercambios y combates que premian la preparación del equipo.',
        2004, 13, 45.00, 8.00, TRUE, TRUE
    );

-- ============================================================
-- RELACION VIDEOJUEGOS - GENEROS
-- ============================================================

INSERT INTO videojuegos_generos (id_videojuego, id_genero) VALUES
                                                               (1, 3),
                                                               (2, 2),
                                                               (2, 4),
                                                               (3, 1),
                                                               (3, 2),
                                                               (4, 3),
                                                               (5, 10),
                                                               (6, 4),
                                                               (6, 2);

-- ============================================================
-- EJEMPLARES
-- ============================================================

INSERT INTO ejemplares (id_videojuego, codigo_ejemplar, estado) VALUES
                                                                    (1, 'EJ-MARIO-001', 'DISPONIBLE'),
                                                                    (1, 'EJ-MARIO-002', 'DISPONIBLE'),
                                                                    (2, 'EJ-ZELDA-001', 'DISPONIBLE'),
                                                                    (3, 'EJ-GTA-001', 'DISPONIBLE'),
                                                                    (3, 'EJ-GTA-002', 'DISPONIBLE'),
                                                                    (4, 'EJ-SONIC-001', 'DISPONIBLE'),
                                                                    (5, 'EJ-DOOM-001', 'DISPONIBLE'),
                                                                    (6, 'EJ-POKEMON-001', 'DISPONIBLE');

-- ============================================================
-- PEDIDOS
-- ============================================================

INSERT INTO pedidos
(id_usuario, fecha_pedido, estado, importe_total)
VALUES
    (1, '2026-10-20 12:00:00', 'COMPLETADO', 43.00),
    (2, '2026-10-21 16:30:00', 'COMPLETADO', 4.00);

-- ============================================================
-- DETALLES DE PEDIDOS
-- ============================================================

INSERT INTO detalles_pedido
(id_pedido, id_videojuego, id_ejemplar, codigo_producto,
 codigo_ejemplar, nombre_videojuego, tipo_operacion, precio_unitario)
VALUES
    (1, 1, 1, 'NES-MARIO-001', 'EJ-MARIO-001',
     'Super Mario Bros.', 'COMPRA', 25.00),
    (1, 4, 6, 'MD-SONIC-001', 'EJ-SONIC-001',
     'Sonic the Hedgehog', 'COMPRA', 18.00),
    (2, 3, 4, 'PS2-GTA-001', 'EJ-GTA-001',
     'Grand Theft Auto: San Andreas', 'ALQUILER', 4.00);

-- ============================================================
-- VENTAS
-- ============================================================

INSERT INTO ventas (id_detalle, fecha_venta, importe, estado) VALUES
                                                                  (1, '2026-10-20 12:00:00', 25.00, 'REGISTRADA'),
                                                                  (2, '2026-10-20 12:00:00', 18.00, 'REGISTRADA');

-- ============================================================
-- ALQUILERES
-- ============================================================

INSERT INTO alquileres
(id_detalle, fecha_inicio, fecha_devolucion_prevista, estado)
VALUES
    (3, '2026-10-21 16:30:00', '2026-10-28', 'ACTIVO');

-- ============================================================
-- ACTUALIZAR ESTADO DE EJEMPLARES
-- ============================================================

UPDATE ejemplares
SET estado = 'VENDIDO'
WHERE id_ejemplar IN (1, 6);

UPDATE ejemplares
SET estado = 'ALQUILADO'
WHERE id_ejemplar = 4;

-- ============================================================
-- VALORACIONES
-- ============================================================

INSERT INTO valoraciones
(id_usuario, id_videojuego, puntuacion, critica)
VALUES
    (
        1, 1, 9,
        'Un clásico de plataformas que sigue siendo divertido décadas después. Su diseño de niveles es sencillo de entender, pero exige precisión y buenos reflejos para superar las últimas fases.'
    ),
    (
        2, 3, 8,
        'Un mundo abierto enorme para su época, con muchas actividades y una historia memorable. La libertad para explorar las ciudades y el campo hace que siempre haya algo distinto que hacer.'
    ),
    (
        1, 2, 10,
        'Una aventura sobresaliente por la variedad de sus mazmorras, los secretos que esconde Hyrule y la forma en que introduce nuevas herramientas para descubrir zonas antes inaccesibles.'
    );

select * from usuarios;


-- ============================================================
-- CONTRASENAS --
--       Usuario	Correo	Contraseña	Rol
--  Carlos	carlos@example.com	Carlos123	CLIENTE
--  Laura	laura@example.com	Laura123	CLIENTE
--  Miguel	miguel@example.com	Miguel123	TENDERO
--  Administrador	admin@example.com	Admin123	ADMIN
-- ============================================================

-- ============================================================================
-- SCRIPT DE BASE DE DATOS - MINIMARKET LA ESQUINA
-- ============================================================================
-- Sistema de Control de Inventario y Ventas
-- Versión: 2.0 (Compatible con Apache Tomcat 10, Jakarta EE y SHA-256)
-- ============================================================================

-- Crear base de datos
DROP DATABASE IF EXISTS minimarket_db;
CREATE DATABASE minimarket_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE minimarket_db;

-- ============================================================================
-- TABLA: USUARIOS
-- ============================================================================
CREATE TABLE usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol ENUM('Administrador', 'Almacenero', 'Cajero') NOT NULL DEFAULT 'Cajero',
    estado TINYINT(1) NOT NULL DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_correo (correo),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- TABLA: PRODUCTOS
-- ============================================================================
CREATE TABLE productos (
    id_producto INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    precio_compra DECIMAL(10, 2) NOT NULL,
    precio_venta DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 5,
    fecha_vencimiento DATE,
    estado TINYINT(1) NOT NULL DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_codigo (codigo),
    INDEX idx_categoria (categoria),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- DATOS DE PRUEBA: USUARIOS (Contraseñas con HASH SHA-256)
-- ============================================================================
-- admin@minimarket.com   -> admin123   (SHA2: 240be518fabd2724ddb6f04eeb1da5967448d7e42b0f01c503ac721a377f0ec0)
-- almacen@minimarket.com -> almacen123 (SHA2: fdf68ebc732ce02787c80521e427d118bb9d12f36d6a6d65445aa4c19efae56b)
-- cajero@minimarket.com  -> cajero123  (SHA2: b99e74d32095ec40d7c5885fe77b49ef1a690bf03b9b9a690ce1b1bc2dc67822)
-- ============================================================================
INSERT INTO usuarios (nombres, apellidos, correo, password, rol, estado) VALUES
('Xavier', 'Administrador', 'admin@minimarket.com', SHA2('admin123', 256), 'Administrador', 1),
('Juan', 'Almacenero', 'almacen@minimarket.com', SHA2('almacen123', 256), 'Almacenero', 1),
('Carlos', 'Cajero', 'cajero@minimarket.com', SHA2('cajero123', 256), 'Cajero', 1);

-- ============================================================================
-- DATOS DE PRUEBA: PRODUCTOS INICIALES
-- ============================================================================
INSERT INTO productos (codigo, nombre, categoria, precio_compra, precio_venta, stock, stock_minimo, fecha_vencimiento, estado) VALUES
('PROD001', 'Arroz Costeño Extra 1kg', 'Abarrotes', 3.80, 4.80, 80, 15, '2027-12-31', 1),
('PROD002', 'Aceite Vegetal Primor 1L', 'Abarrotes', 7.50, 9.20, 45, 10, '2027-08-30', 1),
('PROD003', 'Leche Evaporada Gloria Azul 400g', 'Lácteos', 3.60, 4.30, 8, 20, '2027-05-15', 1), -- Alerta de bajo stock (8 <= 20)
('PROD004', 'Queso Fresco Pasteurizado 500g', 'Lácteos', 8.00, 12.00, 4, 10, '2026-12-15', 1), -- Alerta de bajo stock (4 <= 10)
('PROD005', 'Pan de Molde Blanco Bimbo 550g', 'Panadería', 5.50, 7.20, 25, 8, '2026-11-20', 1),
('PROD006', 'Detergente Ariel Doble Poder 750g', 'Limpieza', 6.20, 8.50, 30, 10, '2028-01-01', 1),
('PROD007', 'Gaseosa Coca Cola 1.5L', 'Bebidas', 4.50, 6.00, 3, 12, '2027-03-10', 1); -- Alerta de bajo stock (3 <= 12)

-- ============================================================================
-- VISTAS AUXILIARES
-- ============================================================================
CREATE OR REPLACE VIEW v_usuarios_activos AS
SELECT 
    id_usuario,
    CONCAT(nombres, ' ', apellidos) AS nombre_completo,
    correo,
    rol,
    fecha_creacion
FROM usuarios
WHERE estado = 1;

CREATE OR REPLACE VIEW v_productos_bajo_stock AS
SELECT 
    id_producto,
    codigo,
    nombre,
    categoria,
    stock,
    stock_minimo,
    (stock_minimo - stock) AS deficit,
    precio_compra,
    precio_venta
FROM productos
WHERE stock <= stock_minimo AND estado = 1
ORDER BY deficit DESC;

-- ============================================================================
-- FIN DEL SCRIPT
-- ============================================================================

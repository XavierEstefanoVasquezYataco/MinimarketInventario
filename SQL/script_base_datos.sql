-- ============================================================================
-- SCRIPT DE BASE DE DATOS - MINIMARKET LA ESQUINA
-- ============================================================================
-- Autor: Sistema de Inventario
-- Versión: 1.0
-- Descripción: Script de creación de tablas para sistema de inventario
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
-- INSERTAR DATOS DE PRUEBA - USUARIOS
-- ============================================================================
INSERT INTO usuarios (nombres, apellidos, correo, password, rol, estado) VALUES
('Admin', 'Sistema', 'admin@minimarket.com', MD5('admin123'), 'Administrador', 1),
('Juan', 'Almacenero', 'juan@minimarket.com', MD5('juan123'), 'Almacenero', 1),
('Carlos', 'Cajero', 'carlos@minimarket.com', MD5('carlos123'), 'Cajero', 1);

-- ============================================================================
-- INSERTAR DATOS DE PRUEBA - PRODUCTOS
-- ============================================================================
INSERT INTO productos (codigo, nombre, categoria, precio_compra, precio_venta, stock, stock_minimo, fecha_vencimiento, estado) VALUES
('PROD001', 'Arroz 1kg', 'Alimentos', 1.50, 2.50, 100, 20, '2026-12-31', 1),
('PROD002', 'Aceite 1L', 'Alimentos', 2.00, 3.50, 50, 10, '2026-06-30', 1),
('PROD003', 'Leche 1L', 'Lácteos', 1.20, 2.00, 80, 15, '2026-06-15', 1),
('PROD004', 'Queso 500g', 'Lácteos', 3.00, 5.00, 30, 5, '2026-07-01', 1),
('PROD005', 'Pan Integral', 'Panadería', 0.50, 1.00, 200, 50, '2026-06-02', 1);

-- ============================================================================
-- VISTA DE INFORMACIÓN DE USUARIOS (Opcional)
-- ============================================================================
CREATE VIEW v_usuarios_activos AS
SELECT 
    id_usuario,
    CONCAT(nombres, ' ', apellidos) as nombre_completo,
    correo,
    rol,
    fecha_creacion
FROM usuarios
WHERE estado = 1;

-- ============================================================================
-- VISTA DE PRODUCTOS CON BAJO STOCK
-- ============================================================================
CREATE VIEW v_productos_bajo_stock AS
SELECT 
    id_producto,
    codigo,
    nombre,
    categoria,
    stock,
    stock_minimo,
    (stock_minimo - stock) as deficit
FROM productos
WHERE stock <= stock_minimo AND estado = 1
ORDER BY deficit DESC;

-- ============================================================================
-- FIN DEL SCRIPT
-- ============================================================================

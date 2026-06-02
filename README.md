# 🛒 Sistema de Inventario - Minimarket La Esquina

## 📋 Descripción del Proyecto

Sistema completo de gestión de inventario para un minimarket, desarrollado con Java Web (JSP, Servlets), MySQL y arquitectura MVC. Permite registrar, actualizar, eliminar y consultar productos, además de gestionar la autenticación de usuarios con roles específicos.

**Autor:** Xavier Estefano Vasquez Yataco  
**Versión:** 1.0  
**Fecha:** Junio 2026  

---

## 🎯 Objetivos

- Implementar un sistema de inventario robusto y escalable
- Aplicar patrones de diseño SOLID (MVC, DAO, Singleton)
- Gestionar la autenticación y autorización de usuarios
- Realizar operaciones CRUD completas sobre productos
- Proporcionar una interfaz amigable y responsiva
- Utilizar buenas prácticas en seguridad y manejo de datos

---

## 🏗️ Arquitectura del Sistema

### Patrón MVC

```
┌─────────────────────────────────────────────────────┐
│                    VISTA (JSP)                      │
│  login.jsp, menu.jsp, productos.jsp                │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────────┐
│                CONTROLADOR (Servlet)                │
│  LoginServlet, ProductoServlet                      │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────────┐
│           MODELO + DAO (Java)                       │
│  Usuario, Producto, UsuarioDAO, ProductoDAO        │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────────┐
│         BASE DE DATOS (MySQL)                       │
│  minimarket_db (usuarios, productos)                │
└─────────────────────────────────────────────────────┘
```

---

## 📁 Estructura del Proyecto

```
MinimarketInventario/
├── SQL/
│   └── script_base_datos.sql          # Script para crear BD
├── src/
│   ├── modelo/
│   │   ├── Usuario.java               # Modelo Usuario
│   │   └── Producto.java              # Modelo Producto
│   ├── dao/
│   │   ├── UsuarioDAO.java            # DAO de Usuarios
│   │   └── ProductoDAO.java           # DAO de Productos
│   ├── controlador/
│   │   ├── LoginServlet.java          # Servlet de autenticación
│   │   └── ProductoServlet.java       # Servlet de productos CRUD
│   └── util/
│       └── Conexion.java              # Gestión conexión JDBC
├── web/
│   ├── login.jsp                       # Página de login
│   ├── menu.jsp                        # Menú principal
│   ├── productos.jsp                   # Gestión de productos
│   └── WEB-INF/
│       └── web.xml                     # Configuración web
└── README.md                           # Este archivo
```

---

## 🔧 Tecnologías Utilizadas

| Tecnología | Versión | Descripción |
|-----------|---------|------------|
| **Java** | 8+ | Lenguaje de programación |
| **JSP** | 2.3+ | Vistas dinámicas |
| **Servlet** | 4.0 | Controladores HTTP |
| **MySQL** | 5.7+ | Base de datos relacional |
| **JDBC** | - | Conector Java-MySQL |
| **Tomcat** | 10.x | Servidor de aplicaciones |
| **NetBeans** | 12+ | IDE de desarrollo |

---

## 💾 Base de Datos

### Tabla: usuarios

```sql
CREATE TABLE usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol ENUM('Administrador', 'Almacenero', 'Cajero'),
    estado TINYINT(1) DEFAULT 1
);
```

**Roles disponibles:**
- **Administrador:** Acceso total al sistema
- **Almacenero:** Gestión de inventario
- **Cajero:** Visualización de productos

---

### Tabla: productos

```sql
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
    estado TINYINT(1) DEFAULT 1
);
```

---

## 🚀 Cómo Ejecutar el Proyecto

### 1️⃣ Requisitos Previos

- Java JDK 8 o superior
- MySQL Server 5.7+
- Apache Tomcat 10.x
- NetBeans 12+
- MySQL Connector/J

### 2️⃣ Configuración de la Base de Datos

```bash
# Abrir MySQL desde terminal
mysql -u root -p

# Ejecutar el script SQL
source C:/ruta/del/proyecto/SQL/script_base_datos.sql

# Verificar creación
SHOW DATABASES;
USE minimarket_db;
SHOW TABLES;
```

**Datos de prueba disponibles:**
- **Email:** admin@minimarket.com
- **Contraseña:** admin123
- **Rol:** Administrador

### 3️⃣ Configuración en NetBeans

1. **Crear nuevo proyecto Java Web**
   - New → Project → Java Web → Web Application
   - Nombre: `MinimarketInventario`
   - Servidor: Apache Tomcat 10.x

2. **Copiar archivos fuente**
   ```
   Copiar carpeta src/ al Source Packages
   Copiar archivos .jsp a Web Pages
   ```

3. **Agregar Driver MySQL**
   - Right-click en Project → Properties
   - Libraries → Add JAR/Folder
   - Seleccionar `mysql-connector-java-8.0.x.jar`

4. **Configurar web.xml**
   - Copiar contenido a `web/WEB-INF/web.xml`

### 4️⃣ Configurar Conexión a Base de Datos

Editar archivo `src/util/Conexion.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/minimarket_db";
private static final String USER = "root";
private static final String PASSWORD = ""; // tu contraseña
```

### 5️⃣ Ejecutar Proyecto

1. **En NetBeans:**
   ```
   Run → Run Project (F6)
   ```

2. **URL de acceso:**
   ```
   http://localhost:8080/MinimarketInventario/login.jsp
   ```

---

## 📖 Guía de Funcionalidades

### 🔐 Módulo de Autenticación

**LoginServlet.java**
- POST `/login` → Autentica usuario
- GET `/login?accion=logout` → Cierra sesión
- Valida credenciales contra BD
- Crea sesión HttpSession

**Seguridad implementada:**
- Contraseñas hasheadas con MD5
- Validación de sesión en todas las páginas
- Timeout de sesión: 30 minutos

### 📦 Módulo de Productos

**ProductoServlet.java**

**Operaciones CRUD:**

| Operación | Método | Endpoint | Descripción |
|-----------|--------|----------|------------|
| **Crear** | POST | /producto | Registrar nuevo producto |
| **Leer** | GET | /producto | Listar todos los productos |
| **Buscar** | GET | /producto?accion=buscar | Buscar por nombre |
| **Actualizar** | POST | /producto | Modificar producto |
| **Eliminar** | POST | /producto?accion=eliminar | Eliminar producto |

**Validaciones implementadas:**

```java
✓ Código único
✓ Campos obligatorios
✓ Precios > 0
✓ Stock no negativo
✓ Fecha de vencimiento válida
✓ Stock mínimo configurado
```

---

## 🗂️ Descripción de Clases

### Modelo (modelo/)

#### **Usuario.java**
Representa un usuario del sistema.

```java
// Propiedades principales
- idUsuario: int
- nombres: String
- apellidos: String
- correo: String
- password: String
- rol: String (Enum)
- estado: int

// Métodos útiles
+ getNombreCompleto(): String
+ isAdministrador(): boolean
+ isActivo(): boolean
```

#### **Producto.java**
Representa un producto del inventario.

```java
// Propiedades principales
- idProducto: int
- codigo: String (unique)
- nombre: String
- categoria: String
- precioCompra: BigDecimal
- precioVenta: BigDecimal
- stock: int
- stockMinimo: int
- fechaVencimiento: LocalDate

// Métodos de cálculo
+ calcularGanancia(): BigDecimal
+ calcularMargenGanancia(): BigDecimal
+ esBajoStock(): boolean
+ estaVencido(): boolean
```

---

### DAO (dao/)

#### **UsuarioDAO.java**
Acceso a datos de usuarios.

```java
+ autenticar(correo, password): Usuario
+ obtenerPorId(id): Usuario
+ obtenerTodos(): List<Usuario>
+ obtenerPorRol(rol): List<Usuario>
+ insertar(usuario): boolean
+ actualizar(usuario): boolean
+ desactivar(id): boolean
+ correoExiste(correo): boolean
```

#### **ProductoDAO.java**
Acceso a datos de productos.

```java
+ obtenerPorId(id): Producto
+ obtenerTodos(): List<Producto>
+ buscarPorNombre(nombre): List<Producto>
+ obtenerProductosBajoStock(): List<Producto>
+ insertar(producto): boolean
+ actualizar(producto): boolean
+ desactivar(id): boolean
+ codigoExiste(codigo): boolean
```

---

### Controladores (controlador/)

#### **LoginServlet.java**
```java
@WebServlet("/login")
- doGet(): Redirección a login
- doPost(): Autenticación de usuario
```

#### **ProductoServlet.java**
```java
@WebServlet("/producto")
- doGet(): Listar, buscar, editar productos
- doPost(): Guardar, actualizar, eliminar productos
- validarProducto(): Validar datos de entrada
```

---

### Utilidades (util/)

#### **Conexion.java**
Gestión de conexión a MySQL.

```java
+ getConnection(): Connection
+ cerrarConexion(): void
+ isConectado(): boolean
```

**Características:**
- Singleton pattern
- Connection pooling básico
- Manejo de excepciones

---

## 🔐 Seguridad

### Implementaciones de seguridad:

1. **Autenticación**
   - Validación de credenciales
   - Contraseñas hasheadas (MD5)
   - HttpSession para mantener sesión

2. **Autorización**
   - Validación de sesión activa
   - Roles diferenciados
   - Acceso restringido por rol

3. **Protección de datos**
   - PreparedStatement contra SQL Injection
   - Validación de inputs
   - Manejo seguro de excepciones

4. **HTTPS Recomendado**
   - Implementar SSL/TLS en producción
   - Configurar cookies seguras

---

## 📝 Guía de Git/GitHub

### Commits realizados:

```bash
# Commit 1: Base de datos
git add SQL/
git commit -m "Script SQL: Creación de base de datos y tablas"
git push

# Commit 2: Modelos
git add src/modelo/
git commit -m "Modelo: Clases Usuario y Producto"
git push

# Commit 3: DAO
git add src/dao/
git commit -m "DAO: Clases UsuarioDAO y ProductoDAO con CRUD"
git push

# Commit 4: Controladores
git add src/controlador/
git commit -m "Controlador: Servlets LoginServlet y ProductoServlet"
git push

# Commit 5: Vistas
git add web/*.jsp
git commit -m "Vista: Páginas JSP (login, menu, productos)"
git push

# Commit 6: Configuración
git add web/WEB-INF/
git commit -m "Configuración: web.xml y documentación"
git push
```

---

## 🐛 Solución de Problemas

### Error: "No such table: usuarios"
**Solución:** Ejecutar script SQL en MySQL
```sql
source SQL/script_base_datos.sql
```

### Error: "Driver not found: com.mysql.cj.jdbc.Driver"
**Solución:** Agregar mysql-connector-java al classpath
1. Download: mysql-connector-java-8.0.x.jar
2. NetBeans: Libraries → Add JAR

### Error: "Access denied for user 'root'@'localhost'"
**Solución:** Verificar credenciales en Conexion.java
```java
private static final String USER = "root";
private static final String PASSWORD = "tu_password";
```

### Puerto 8080 en uso
**Solución:** Cambiar puerto en Tomcat
1. Tomcat → Edit Configuration
2. Cambiar puerto a 8081 o similar

---

## 📊 Casos de Uso

### 1. Gestión de Inventario
```
- Admin inicia sesión
- Registra nuevo producto
- Establece precios y stock
- Sistema calcula ganancia
```

### 2. Reposición de Stock
```
- Sistema detecta bajo stock
- Genera alerta
- Almacenero ve productos a reponer
- Actualiza stock en sistema
```

### 3. Búsqueda de Productos
```
- Cajero busca producto por nombre
- Sistema retorna coincidencias
- Información de disponibilidad inmediata
```

---

## 🚀 Mejoras Futuras

- [ ] Módulo de reportes (PDF, Excel)
- [ ] Sistema de ventas/facturación
- [ ] Auditoría de cambios
- [ ] Exportación de datos
- [ ] Interfaz mejorada con Bootstrap
- [ ] Integración con API REST
- [ ] Aplicación móvil
- [ ] Sistema de alertas por email
- [ ] Dashboard con gráficos

---

## 📚 Referencias

- [Documentación Java](https://docs.oracle.com/javase/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Apache Tomcat Docs](https://tomcat.apache.org/tomcat-10.0-doc/)
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)
- [JSP Specification](https://javaee.github.io/jsp/)

---

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## 👨‍💻 Autor

**Xavier Estefano Vasquez Yataco**  
Ingeniería de Sistemas  
Universidad Tecnológica del Perú (UTP)

---

## 📞 Contacto

Para dudas o sugerencias, contactar al desarrollador.

---

**Última actualización:** Junio 2026  
**Estado:** ✅ Funcional y Completo

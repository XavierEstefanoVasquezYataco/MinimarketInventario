<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="modelo.Usuario" %>
<%
    // Validar sesión
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String nombreUsuario = (String) session.getAttribute("nombreUsuario");
    String rol = (String) session.getAttribute("rol");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú Principal - Minimarket</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f5f5;
        }
        
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 15px 30px;
            color: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        
        .navbar h2 {
            font-size: 24px;
        }
        
        .user-info {
            display: flex;
            gap: 30px;
            align-items: center;
        }
        
        .user-info p {
            font-size: 14px;
        }
        
        .btn-logout {
            background: white;
            color: #667eea;
            padding: 8px 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
        }
        
        .btn-logout:hover {
            opacity: 0.9;
        }
        
        .container {
            padding: 40px;
        }
        
        .welcome {
            background: white;
            padding: 30px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        
        .welcome h1 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .welcome p {
            color: #666;
            font-size: 16px;
        }
        
        .modules {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }
        
        .module-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s, box-shadow 0.3s;
            text-align: center;
        }
        
        .module-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
        }
        
        .module-icon {
            font-size: 48px;
            margin-bottom: 15px;
        }
        
        .module-card h3 {
            color: #333;
            margin-bottom: 10px;
            font-size: 18px;
        }
        
        .module-card p {
            color: #666;
            font-size: 14px;
            margin-bottom: 20px;
        }
        
        .btn-module {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            transition: transform 0.2s;
        }
        
        .btn-module:hover {
            transform: scale(1.05);
        }
        
        .footer {
            text-align: center;
            padding: 20px;
            color: #999;
            font-size: 12px;
            margin-top: 40px;
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <div class="navbar">
        <h2>🛒 Minimarket La Esquina</h2>
        <div class="user-info">
            <div>
                <p>Bienvenido: <strong><%= nombreUsuario %></strong></p>
                <p>Rol: <strong><%= rol %></strong></p>
            </div>
            <a href="<%= request.getContextPath() %>/login?accion=logout" class="btn-logout">Cerrar Sesión</a>
        </div>
    </div>
    
    <!-- Contenido -->
    <div class="container">
        <div class="welcome">
            <h1>Bienvenido al Sistema de Inventario</h1>
            <p>Selecciona un módulo para comenzar a trabajar.</p>
        </div>
        
        <div class="modules">
            <!-- Módulo de Productos -->
            <div class="module-card">
                <div class="module-icon">📦</div>
                <h3>Gestión de Productos</h3>
                <p>Crear, editar, eliminar y buscar productos en el inventario.</p>
                <a href="<%= request.getContextPath() %>/producto" class="btn-module">Ir al Módulo</a>
            </div>
            
            <!-- Módulo de Bajo Stock -->
            <div class="module-card">
                <div class="module-icon">⚠️</div>
                <h3>Productos Bajo Stock</h3>
                <p>Visualiza los productos que necesitan reposición urgente.</p>
                <a href="<%= request.getContextPath() %>/producto?accion=bajo_stock" class="btn-module">Ver Alertas</a>
            </div>
            
            <!-- Módulo de Reportes -->
            <div class="module-card">
                <div class="module-icon">📊</div>
                <h3>Reportes</h3>
                <p>Genera reportes de inventario y análisis de ventas.</p>
                <a href="#" class="btn-module" onclick="alert('Módulo en desarrollo'); return false;">En Desarrollo</a>
            </div>
            
            <!-- Módulo de Configuración -->
            <div class="module-card">
                <div class="module-icon">⚙️</div>
                <h3>Configuración</h3>
                <p>Ajusta la configuración del sistema y permisos de usuario.</p>
                <a href="#" class="btn-module" onclick="alert('Módulo en desarrollo'); return false;">En Desarrollo</a>
            </div>
        </div>
    </div>
    
    <div class="footer">
        <p>&copy; 2026 Sistema de Inventario - Minimarket La Esquina. Todos los derechos reservados.</p>
    </div>
</body>
</html>

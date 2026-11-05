<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="modelo.Usuario" %>
<%
    // Validar sesión activa
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String nombreUsuario = (String) session.getAttribute("nombreUsuario");
    String rol = (String) session.getAttribute("rol");
    if (nombreUsuario == null) {
        nombreUsuario = usuario.getNombreCompleto();
    }
    if (rol == null) {
        rol = usuario.getRol();
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú Principal - Minimarket La Esquina</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        .module-card {
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            border: none;
            border-radius: 0.75rem;
        }
        .module-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 0.75rem 1.5rem rgba(0, 0, 0, 0.1) !important;
        }
        .icon-box {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 1.75rem;
        }
    </style>
</head>
<body class="bg-light min-vh-100 d-flex flex-column">

    <!-- Barra de Navegación -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold d-flex align-items-center" href="<%= request.getContextPath() %>/menu.jsp">
                <i class="bi bi-cart4 fs-4 me-2"></i> Minimarket La Esquina
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navContent">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navContent">
                <ul class="navbar-nav align-items-lg-center gap-2">
                    <li class="nav-item text-white me-lg-3">
                        <i class="bi bi-person-circle me-1"></i>
                        <span><%= nombreUsuario %></span>
                        <% if ("Administrador".equalsIgnoreCase(rol)) { %>
                            <span class="badge bg-danger ms-1">Administrador</span>
                        <% } else if ("Almacenero".equalsIgnoreCase(rol)) { %>
                            <span class="badge bg-warning text-dark ms-1">Almacenero</span>
                        <% } else { %>
                            <span class="badge bg-info text-dark ms-1">Cajero</span>
                        <% } %>
                    </li>
                    <li class="nav-item">
                        <a href="<%= request.getContextPath() %>/login?accion=logout" class="btn btn-outline-light btn-sm">
                            <i class="bi bi-box-arrow-right me-1"></i> Cerrar Sesión
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Contenido Principal -->
    <main class="container py-5 flex-grow-1">
        <!-- Banner de Bienvenida -->
        <div class="card border-0 shadow-sm rounded-4 p-4 p-md-5 mb-4 bg-white">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h1 class="h2 fw-bold text-dark mb-2">¡Hola, <%= nombreUsuario %>!</h1>
                    <p class="text-muted mb-3">
                        Bienvenido al sistema de inventario del Minimarket. Selecciona un módulo para gestionar los productos y alertas del negocio.
                    </p>
                    <div class="d-flex flex-wrap gap-2 align-items-center">
                        <span class="text-secondary small fw-semibold">Nivel de Acceso:</span>
                        <% if ("Administrador".equalsIgnoreCase(rol)) { %>
                            <span class="badge bg-success-subtle text-success border border-success-subtle px-2 py-1">
                                <i class="bi bi-shield-lock-fill me-1"></i> Acceso Total (CRUD + Eliminación)
                            </span>
                        <% } else if ("Almacenero".equalsIgnoreCase(rol)) { %>
                            <span class="badge bg-primary-subtle text-primary border border-primary-subtle px-2 py-1">
                                <i class="bi bi-boxes me-1"></i> Gestión de Inventario (Registro y Edición)
                            </span>
                        <% } else { %>
                            <span class="badge bg-secondary-subtle text-secondary border border-secondary-subtle px-2 py-1">
                                <i class="bi bi-eye-fill me-1"></i> Modo Consulta (Solo Lectura)
                            </span>
                        <% } %>
                    </div>
                </div>
                <div class="col-md-4 text-center text-md-end mt-4 mt-md-0">
                    <i class="bi bi-shop text-primary opacity-25" style="font-size: 6rem;"></i>
                </div>
            </div>
        </div>

        <!-- Módulos del Sistema -->
        <div class="row g-4">
            <!-- Módulo 1: Gestión de Productos -->
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card module-card shadow-sm h-100 p-4 bg-white text-center">
                    <div class="icon-box bg-primary-subtle text-primary mx-auto mb-3">
                        <i class="bi bi-box-seam-fill"></i>
                    </div>
                    <h2 class="h5 fw-bold mb-2">Inventario de Productos</h2>
                    <p class="text-muted small mb-4">
                        Visualiza, busca, registra y actualiza productos en existencia en el minimarket.
                    </p>
                    <div class="mt-auto">
                        <a href="<%= request.getContextPath() %>/producto" class="btn btn-primary w-100">
                            <i class="bi bi-arrow-right-circle me-1"></i> Ingresar al Módulo
                        </a>
                    </div>
                </div>
            </div>

            <!-- Módulo 2: Alertas de Bajo Stock -->
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card module-card shadow-sm h-100 p-4 bg-white text-center">
                    <div class="icon-box bg-warning-subtle text-warning mx-auto mb-3">
                        <i class="bi bi-exclamation-triangle-fill"></i>
                    </div>
                    <h2 class="h5 fw-bold mb-2">Alertas de Bajo Stock</h2>
                    <p class="text-muted small mb-4">
                        Revisa los productos que han alcanzado o superado el stock mínimo de seguridad.
                    </p>
                    <div class="mt-auto">
                        <a href="<%= request.getContextPath() %>/producto?accion=bajo_stock" class="btn btn-warning w-100 fw-semibold text-dark">
                            <i class="bi bi-bell-fill me-1"></i> Ver Alertas de Stock
                        </a>
                    </div>
                </div>
            </div>

            <!-- Módulo 3: Información y Seguridad -->
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card module-card shadow-sm h-100 p-4 bg-white text-center">
                    <div class="icon-box bg-info-subtle text-info mx-auto mb-3">
                        <i class="bi bi-shield-check"></i>
                    </div>
                    <h2 class="h5 fw-bold mb-2">Seguridad y Roles</h2>
                    <p class="text-muted small mb-4">
                        El sistema opera bajo control de roles (RBAC) y contraseñas seguras SHA-256.
                    </p>
                    <div class="mt-auto">
                        <button class="btn btn-outline-secondary w-100" data-bs-toggle="modal" data-bs-target="#infoModal">
                            <i class="bi bi-info-circle me-1"></i> Ver Privilegios
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Modal de Información de Roles -->
    <div class="modal fade" id="infoModal" tabindex="-1" aria-labelledby="infoModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="infoModalLabel"><i class="bi bi-shield-lock me-2"></i>Matriz de Roles y Permisos</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body">
                    <ul class="list-group list-group-flush small">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <span class="badge bg-danger me-2">Administrador</span>
                                <span>Control total: Registrar, Editar, Desactivar y Consultar</span>
                            </div>
                            <i class="bi bi-check-circle-fill text-success"></i>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <span class="badge bg-warning text-dark me-2">Almacenero</span>
                                <span>Gestión operativa: Registrar, Editar y Consultar inventario</span>
                            </div>
                            <i class="bi bi-check-circle-fill text-success"></i>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <span class="badge bg-info text-dark me-2">Cajero</span>
                                <span>Solo consulta: Búsqueda y visualización de existencias</span>
                            </div>
                            <i class="bi bi-eye-fill text-primary"></i>
                        </li>
                    </ul>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Entendido</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Pie de Página -->
    <footer class="bg-white border-top py-3 text-center text-muted small mt-auto">
        <div class="container">
            &copy; 2026 Minimarket La Esquina. Desarrollado con Java Web MVC y Bootstrap 5.
        </div>
    </footer>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

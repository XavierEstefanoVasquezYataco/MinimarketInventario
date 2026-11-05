<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Si ya hay sesión activa, redirigir al menú principal
    if (session != null && session.getAttribute("usuario") != null) {
        response.sendRedirect(request.getContextPath() + "/menu.jsp");
        return;
    }
    String error = (String) request.getAttribute("error");
    String mensaje = (String) request.getAttribute("mensaje");
    String correoPrevio = (String) request.getAttribute("correo");
    if (correoPrevio == null) {
        correoPrevio = "";
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Minimarket La Esquina</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            min-height: 100vh;
        }
        .login-card {
            border: none;
            border-radius: 1rem;
            box-shadow: 0 1rem 3rem rgba(0, 0, 0, 0.25);
        }
        .brand-icon {
            width: 70px;
            height: 70px;
            background: #e7f1ff;
            color: #0d6efd;
            border-radius: 50%;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 2rem;
        }
    </style>
</head>
<body class="d-flex align-items-center justify-content-center py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-sm-10 col-md-8 col-lg-5 col-xl-4">
                <div class="card login-card p-4 p-md-5 bg-white">
                    <div class="text-center mb-4">
                        <div class="brand-icon mb-3">
                            <i class="bi bi-cart4"></i>
                        </div>
                        <h2 class="h4 fw-bold text-dark mb-1">Minimarket La Esquina</h2>
                        <p class="text-muted small">Sistema de Control de Inventario</p>
                    </div>

                    <% if (error != null && !error.trim().isEmpty()) { %>
                        <div class="alert alert-danger alert-dismissible fade show d-flex align-items-center" role="alert">
                            <i class="bi bi-exclamation-octagon-fill me-2 fs-5"></i>
                            <div><%= error %></div>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
                        </div>
                    <% } %>

                    <% if (mensaje != null && !mensaje.trim().isEmpty()) { %>
                        <div class="alert alert-success alert-dismissible fade show d-flex align-items-center" role="alert">
                            <i class="bi bi-check-circle-fill me-2 fs-5"></i>
                            <div><%= mensaje %></div>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
                        </div>
                    <% } %>

                    <form method="POST" action="<%= request.getContextPath() %>/login">
                        <div class="mb-3">
                            <label for="correo" class="form-label fw-semibold">Correo Electrónico</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light"><i class="bi bi-envelope"></i></span>
                                <input type="email" class="form-control" id="correo" name="correo" 
                                       placeholder="ejemplo@minimarket.com" required autofocus
                                       value="<%= correoPrevio %>">
                            </div>
                        </div>

                        <div class="mb-4">
                            <label for="password" class="form-label fw-semibold">Contraseña</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light"><i class="bi bi-lock"></i></span>
                                <input type="password" class="form-control" id="password" name="password" 
                                       placeholder="••••••••" required>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold shadow-sm">
                            <i class="bi bi-box-arrow-in-right me-1"></i> Iniciar Sesión
                        </button>
                    </form>

                    <div class="mt-4 pt-3 border-top">
                        <div class="card bg-light border-0 p-3 small">
                            <div class="fw-bold text-secondary mb-2">
                                <i class="bi bi-info-circle me-1"></i> Cuentas de Prueba:
                            </div>
                            <div class="d-flex justify-content-between mb-1">
                                <span><span class="badge bg-danger">Admin</span> admin@minimarket.com</span>
                                <code>admin123</code>
                            </div>
                            <div class="d-flex justify-content-between mb-1">
                                <span><span class="badge bg-warning text-dark">Almacén</span> almacen@minimarket.com</span>
                                <code>almacen123</code>
                            </div>
                            <div class="d-flex justify-content-between">
                                <span><span class="badge bg-info text-dark">Cajero</span> cajero@minimarket.com</span>
                                <code>cajero123</code>
                            </div>
                        </div>
                    </div>

                    <div class="text-center mt-3 text-muted" style="font-size: 0.75rem;">
                        &copy; 2026 Minimarket La Esquina. Todos los derechos reservados.
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

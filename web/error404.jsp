<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Página no encontrada - 404</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center min-vh-100">
    <div class="container text-center">
        <div class="card shadow-sm mx-auto p-4" style="max-width: 500px;">
            <div class="display-1 text-warning mb-3">
                <i class="bi bi-exclamation-triangle"></i>
            </div>
            <h1 class="h3 fw-bold mb-2">Página no encontrada (404)</h1>
            <p class="text-muted mb-4">El recurso solicitado no existe o fue movido.</p>
            <a href="<%= request.getContextPath() %>/menu.jsp" class="btn btn-primary">
                <i class="bi bi-house-door me-1"></i> Ir al Menú Principal
            </a>
        </div>
    </div>
</body>
</html>

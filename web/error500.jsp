<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error del Servidor - 500</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center min-vh-100">
    <div class="container text-center">
        <div class="card shadow-sm mx-auto p-4" style="max-width: 550px;">
            <div class="display-1 text-danger mb-3">
                <i class="bi bi-x-octagon"></i>
            </div>
            <h1 class="h3 fw-bold mb-2">Error Interno del Servidor (500)</h1>
            <p class="text-muted mb-4">Ocurrió un problema inesperado al procesar la solicitud en el servidor.</p>
            <% if (exception != null && exception.getMessage() != null) { %>
                <div class="alert alert-danger text-start small mb-3">
                    <strong>Detalle:</strong> <%= exception.getMessage() %>
                </div>
            <% } %>
            <a href="<%= request.getContextPath() %>/menu.jsp" class="btn btn-primary">
                <i class="bi bi-house-door me-1"></i> Volver al Menú Principal
            </a>
        </div>
    </div>
</body>
</html>

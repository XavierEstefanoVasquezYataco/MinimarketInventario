<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="modelo.Usuario, modelo.Producto, java.util.List, java.math.BigDecimal" %>
<%
    // Validar sesión activa
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String nombreUsuario = (String) session.getAttribute("nombreUsuario");
    String rol = (String) session.getAttribute("rol");
    if (nombreUsuario == null) nombreUsuario = usuario.getNombreCompleto();
    if (rol == null) rol = usuario.getRol();

    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    Producto productoEdicion = (Producto) request.getAttribute("producto");
    String busqueda = (String) request.getAttribute("busqueda");
    String filtro = (String) request.getAttribute("filtro");
    String error = (String) request.getAttribute("error");
    String mensaje = (String) request.getAttribute("mensaje");
    List<String> errores = (List<String>) request.getAttribute("errores");

    boolean esModoEdicion = (productoEdicion != null && productoEdicion.getIdProducto() > 0);
    boolean puedeModificar = (usuario.isAdministrador() || usuario.isAlmacenero());
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Productos - Minimarket La Esquina</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        .table-hover tbody tr:hover {
            background-color: #f1f5f9;
        }
        .stock-alert {
            animation: pulse-stock 2s infinite;
        }
        @keyframes pulse-stock {
            0% { opacity: 1; }
            50% { opacity: 0.7; }
            100% { opacity: 1; }
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
                            <i class="bi bi-box-arrow-right me-1"></i> Salir
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Contenedor Principal -->
    <main class="container py-4 flex-grow-1">

        <!-- Barra de migas y botón de retorno -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <a href="<%= request.getContextPath() %>/menu.jsp" class="btn btn-outline-secondary btn-sm">
                    <i class="bi bi-arrow-left me-1"></i> Volver al Menú
                </a>
            </div>
            <div>
                <span class="text-muted small">Módulo actual: <strong>Gestión de Productos</strong></span>
            </div>
        </div>

        <!-- Alertas y Mensajes -->
        <% if (mensaje != null && !mensaje.trim().isEmpty()) { %>
            <div class="alert alert-success alert-dismissible fade show d-flex align-items-center shadow-sm" role="alert">
                <i class="bi bi-check-circle-fill me-2 fs-5"></i>
                <div><%= mensaje %></div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
            </div>
        <% } %>

        <% if (error != null && !error.trim().isEmpty()) { %>
            <div class="alert alert-danger alert-dismissible fade show d-flex align-items-center shadow-sm" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2 fs-5"></i>
                <div><%= error %></div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
            </div>
        <% } %>

        <% if (errores != null && !errores.isEmpty()) { %>
            <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
                <div class="d-flex align-items-center mb-1">
                    <i class="bi bi-x-octagon-fill me-2 fs-5"></i>
                    <strong>Por favor corrige los siguientes errores:</strong>
                </div>
                <ul class="mb-0 ps-4 small">
                    <% for (String err : errores) { %>
                        <li><%= err %></li>
                    <% } %>
                </ul>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
            </div>
        <% } %>

        <!-- SECCIÓN 1: Formulario de Registro / Edición (Solo para Administrador y Almacenero) -->
        <% if (puedeModificar) { %>
            <div class="card border-0 shadow-sm rounded-3 mb-4 <%= esModoEdicion ? "border-start border-warning border-4" : "" %>">
                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0 fw-bold <%= esModoEdicion ? "text-warning" : "text-primary" %>">
                        <i class="bi <%= esModoEdicion ? "bi-pencil-square" : "bi-plus-circle" %> me-2"></i>
                        <%= esModoEdicion ? "Editar Producto: " + productoEdicion.getNombre() : "Registrar Nuevo Producto" %>
                    </h5>
                    <% if (esModoEdicion) { %>
                        <a href="<%= request.getContextPath() %>/producto" class="btn btn-outline-secondary btn-sm">
                            <i class="bi bi-x me-1"></i> Cancelar Modo Edición
                        </a>
                    <% } %>
                </div>
                <div class="card-body p-4">
                    <form method="POST" action="<%= request.getContextPath() %>/producto" class="row g-3">
                        <input type="hidden" name="accion" value="<%= esModoEdicion ? "actualizar" : "guardar" %>">
                        <% if (esModoEdicion) { %>
                            <input type="hidden" name="idProducto" value="<%= productoEdicion.getIdProducto() %>">
                        <% } %>

                        <!-- Fila 1: Código, Nombre y Categoría -->
                        <div class="col-md-3">
                            <label for="codigo" class="form-label fw-semibold">Código *</label>
                            <input type="text" class="form-control" id="codigo" name="codigo" required
                                   placeholder="Ej: PROD001"
                                   value="<%= (productoEdicion != null && productoEdicion.getCodigo() != null) ? productoEdicion.getCodigo() : "" %>">
                        </div>

                        <div class="col-md-5">
                            <label for="nombre" class="form-label fw-semibold">Nombre del Producto *</label>
                            <input type="text" class="form-control" id="nombre" name="nombre" required
                                   placeholder="Ej: Arroz Costeño 1kg"
                                   value="<%= (productoEdicion != null && productoEdicion.getNombre() != null) ? productoEdicion.getNombre() : "" %>">
                        </div>

                        <div class="col-md-4">
                            <label for="categoria" class="form-label fw-semibold">Categoría *</label>
                            <input type="text" class="form-control" id="categoria" name="categoria" required
                                   placeholder="Ej: Abarrotes, Lácteos, Bebidas..."
                                   value="<%= (productoEdicion != null && productoEdicion.getCategoria() != null) ? productoEdicion.getCategoria() : "" %>">
                        </div>

                        <!-- Fila 2: Precios, Stocks y Vencimiento -->
                        <div class="col-md-3">
                            <label for="precioCompra" class="form-label fw-semibold">Precio Compra (S/.) *</label>
                            <input type="number" step="0.01" min="0.01" class="form-control" id="precioCompra" name="precioCompra" required
                                   placeholder="0.00"
                                   value="<%= (productoEdicion != null && productoEdicion.getPrecioCompra() != null) ? productoEdicion.getPrecioCompra() : "" %>">
                        </div>

                        <div class="col-md-3">
                            <label for="precioVenta" class="form-label fw-semibold">Precio Venta (S/.) *</label>
                            <input type="number" step="0.01" min="0.01" class="form-control" id="precioVenta" name="precioVenta" required
                                   placeholder="0.00"
                                   value="<%= (productoEdicion != null && productoEdicion.getPrecioVenta() != null) ? productoEdicion.getPrecioVenta() : "" %>">
                        </div>

                        <div class="col-md-2">
                            <label for="stock" class="form-label fw-semibold">Stock Actual *</label>
                            <input type="number" min="0" class="form-control" id="stock" name="stock" required
                                   placeholder="0"
                                   value="<%= (productoEdicion != null) ? productoEdicion.getStock() : "0" %>">
                        </div>

                        <div class="col-md-2">
                            <label for="stockMinimo" class="form-label fw-semibold">Stock Mínimo *</label>
                            <input type="number" min="0" class="form-control" id="stockMinimo" name="stockMinimo" required
                                   placeholder="5"
                                   value="<%= (productoEdicion != null) ? productoEdicion.getStockMinimo() : "5" %>">
                        </div>

                        <div class="col-md-2">
                            <label for="fechaVencimiento" class="form-label fw-semibold">Vencimiento</label>
                            <input type="date" class="form-control" id="fechaVencimiento" name="fechaVencimiento"
                                   value="<%= (productoEdicion != null && productoEdicion.getFechaVencimiento() != null) ? productoEdicion.getFechaVencimiento().toString() : "" %>">
                        </div>

                        <!-- Botones de Acción -->
                        <div class="col-12 text-end pt-2">
                            <% if (esModoEdicion) { %>
                                <a href="<%= request.getContextPath() %>/producto" class="btn btn-outline-secondary me-2">
                                    <i class="bi bi-x-circle me-1"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-warning text-dark fw-semibold">
                                    <i class="bi bi-check-circle me-1"></i> Guardar Cambios
                                </button>
                            <% } else { %>
                                <button type="reset" class="btn btn-outline-secondary me-2">
                                    <i class="bi bi-arrow-counterclockwise me-1"></i> Limpiar Formulario
                                </button>
                                <button type="submit" class="btn btn-success fw-semibold">
                                    <i class="bi bi-save me-1"></i> Registrar Producto
                                </button>
                            <% } %>
                        </div>
                    </form>
                </div>
            </div>
        <% } else { %>
            <div class="alert alert-info border-0 shadow-sm d-flex align-items-center mb-4">
                <i class="bi bi-info-circle-fill me-2 fs-5"></i>
                <div>
                    <strong>Modo Consulta (Rol: Cajero):</strong> Puedes buscar y revisar el stock disponible de todos los productos en catálogo.
                </div>
            </div>
        <% } %>

        <!-- SECCIÓN 2: Barra de Búsqueda y Filtros Rápidos -->
        <div class="card border-0 shadow-sm rounded-3 mb-4 bg-white p-3">
            <div class="row g-3 align-items-center">
                <div class="col-12 col-md-7">
                    <form method="GET" action="<%= request.getContextPath() %>/producto" class="d-flex gap-2">
                        <input type="hidden" name="accion" value="buscar">
                        <div class="input-group">
                            <span class="input-group-text bg-light"><i class="bi bi-search"></i></span>
                            <input type="text" class="form-control" name="criterio" 
                                   placeholder="Buscar por nombre o por código..."
                                   value="<%= (busqueda != null) ? busqueda : "" %>">
                            <button class="btn btn-primary" type="submit">Buscar</button>
                        </div>
                        <% if (busqueda != null && !busqueda.trim().isEmpty()) { %>
                            <a href="<%= request.getContextPath() %>/producto" class="btn btn-outline-secondary" title="Limpiar búsqueda">
                                <i class="bi bi-x-lg"></i>
                            </a>
                        <% } %>
                    </form>
                </div>
                <div class="col-12 col-md-5 text-md-end">
                    <div class="btn-group" role="group">
                        <a href="<%= request.getContextPath() %>/producto" class="btn <%= (filtro == null && busqueda == null) ? "btn-secondary" : "btn-outline-secondary" %> btn-sm">
                            <i class="bi bi-list-ul me-1"></i> Todos
                        </a>
                        <a href="<%= request.getContextPath() %>/producto?accion=bajo_stock" class="btn <%= "Productos con Bajo Stock (Reposición Urgente)".equals(filtro) ? "btn-warning text-dark" : "btn-outline-warning text-dark" %> btn-sm">
                            <i class="bi bi-exclamation-triangle-fill me-1"></i> Bajo Stock
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- SECCIÓN 3: Tabla de Resultados de Productos -->
        <div class="card border-0 shadow-sm rounded-3 bg-white">
            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                <h5 class="card-title mb-0 fw-bold text-dark">
                    <i class="bi bi-box-seam me-2"></i> Listado de Productos
                </h5>
                <span class="badge bg-light text-secondary border">
                    Total: <%= (productos != null) ? productos.size() : 0 %> producto(s)
                </span>
            </div>

            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr class="small text-secondary text-uppercase">
                            <th class="ps-3">Código</th>
                            <th>Nombre</th>
                            <th>Categoría</th>
                            <th class="text-end">P. Compra</th>
                            <th class="text-end">P. Venta</th>
                            <th class="text-center">Stock</th>
                            <th class="text-center">Mínimo</th>
                            <th class="text-end">Ganancia</th>
                            <th>Vencimiento</th>
                            <th class="text-center pe-3">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (productos != null && !productos.isEmpty()) { %>
                            <% for (Producto p : productos) { 
                                boolean esBajoStock = p.esBajoStock();
                                boolean vencido = p.estaVencido();
                            %>
                                <tr class="<%= esBajoStock ? "table-warning bg-opacity-25" : "" %>">
                                    <td class="ps-3 fw-bold text-primary"><%= p.getCodigo() %></td>
                                    <td>
                                        <div class="fw-semibold text-dark"><%= p.getNombre() %></div>
                                        <% if (esBajoStock) { %>
                                            <span class="badge bg-danger stock-alert" style="font-size: 0.65rem;">
                                                <i class="bi bi-exclamation-circle me-1"></i>Stock crítico
                                            </span>
                                        <% } %>
                                    </td>
                                    <td><span class="badge bg-light text-dark border"><%= p.getCategoria() %></span></td>
                                    <td class="text-end text-muted">S/. <%= String.format("%.2f", p.getPrecioCompra()) %></td>
                                    <td class="text-end fw-bold text-dark">S/. <%= String.format("%.2f", p.getPrecioVenta()) %></td>
                                    <td class="text-center">
                                        <% if (esBajoStock) { %>
                                            <span class="badge bg-danger px-2 py-1"><%= p.getStock() %></span>
                                        <% } else { %>
                                            <span class="badge bg-success-subtle text-success border border-success-subtle px-2 py-1">
                                                <%= p.getStock() %>
                                            </span>
                                        <% } %>
                                    </td>
                                    <td class="text-center text-muted"><%= p.getStockMinimo() %></td>
                                    <td class="text-end text-success fw-semibold">
                                        +S/. <%= String.format("%.2f", p.calcularGanancia()) %>
                                    </td>
                                    <td>
                                        <% if (p.getFechaVencimiento() != null) { %>
                                            <span class="<%= vencido ? "badge bg-danger" : "small text-muted" %>">
                                                <%= p.getFechaVencimiento() %>
                                                <%= vencido ? " (Vencido)" : "" %>
                                            </span>
                                        <% } else { %>
                                            <span class="text-muted small">-</span>
                                        <% } %>
                                    </td>
                                    <td class="text-center pe-3">
                                        <% if (puedeModificar) { %>
                                            <div class="btn-group btn-group-sm" role="group">
                                                <a href="<%= request.getContextPath() %>/producto?accion=editar&id=<%= p.getIdProducto() %>" 
                                                   class="btn btn-outline-primary" title="Editar este producto">
                                                    <i class="bi bi-pencil"></i>
                                                </a>
                                                <% if (usuario.isAdministrador()) { %>
                                                    <form method="POST" action="<%= request.getContextPath() %>/producto" 
                                                          style="display:inline;"
                                                          onsubmit="return confirm('¿Confirma la desactivación del producto <%= p.getNombre() %>?');">
                                                        <input type="hidden" name="accion" value="eliminar">
                                                        <input type="hidden" name="id" value="<%= p.getIdProducto() %>">
                                                        <button type="submit" class="btn btn-outline-danger" title="Desactivar producto">
                                                            <i class="bi bi-trash"></i>
                                                        </button>
                                                    </form>
                                                <% } %>
                                            </div>
                                        <% } else { %>
                                            <span class="badge bg-secondary-subtle text-secondary small">Solo lectura</span>
                                        <% } %>
                                    </td>
                                </tr>
                            <% } %>
                        <% } else { %>
                            <tr>
                                <td colspan="10" class="text-center py-5 text-muted">
                                    <i class="bi bi-inbox fs-1 d-block mb-2 text-secondary opacity-50"></i>
                                    No se encontraron productos registrados en el inventario.
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>

    </main>

    <!-- Pie de Página -->
    <footer class="bg-white border-top py-3 text-center text-muted small mt-auto">
        <div class="container">
            &copy; 2026 Minimarket La Esquina. Todos los derechos reservados.
        </div>
    </footer>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

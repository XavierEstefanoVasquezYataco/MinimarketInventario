<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="modelo.Usuario, modelo.Producto, java.util.List" %>
<%
    // Validar sesión
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String nombreUsuario = (String) session.getAttribute("nombreUsuario");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Productos - Minimarket</title>
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
        
        .btn-logout {
            background: white;
            color: #667eea;
            padding: 8px 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: 600;
            text-decoration: none;
        }
        
        .container {
            padding: 30px;
            max-width: 1400px;
            margin: 0 auto;
        }
        
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }
        
        .header h1 {
            color: #333;
            font-size: 28px;
        }
        
        .btn-primary {
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
        
        .btn-primary:hover {
            transform: scale(1.05);
        }
        
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 14px;
        }
        
        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .form-section {
            background: white;
            padding: 30px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        
        .form-section h3 {
            color: #333;
            margin-bottom: 20px;
            font-size: 20px;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 15px;
            margin-bottom: 20px;
        }
        
        .form-group {
            display: flex;
            flex-direction: column;
        }
        
        label {
            color: #333;
            font-weight: 600;
            margin-bottom: 5px;
            font-size: 14px;
        }
        
        input[type="text"],
        input[type="email"],
        input[type="number"],
        input[type="date"],
        select,
        textarea {
            padding: 10px;
            border: 2px solid #e0e0e0;
            border-radius: 5px;
            font-size: 14px;
            font-family: inherit;
            transition: border-color 0.3s;
        }
        
        input:focus,
        select:focus,
        textarea:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .form-buttons {
            display: flex;
            gap: 10px;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: 600;
            transition: transform 0.2s;
        }
        
        .btn-save {
            background: #28a745;
            color: white;
        }
        
        .btn-save:hover {
            transform: scale(1.05);
        }
        
        .btn-cancel {
            background: #6c757d;
            color: white;
        }
        
        .btn-edit {
            background: #ffc107;
            color: #333;
            padding: 6px 12px;
            font-size: 12px;
        }
        
        .btn-delete {
            background: #dc3545;
            color: white;
            padding: 6px 12px;
            font-size: 12px;
        }
        
        .table-section {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            overflow-x: auto;
        }
        
        .table-section h3 {
            color: #333;
            margin-bottom: 20px;
            font-size: 20px;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
        }
        
        table thead {
            background: #667eea;
            color: white;
        }
        
        table th {
            padding: 12px;
            text-align: left;
            font-weight: 600;
            font-size: 14px;
        }
        
        table td {
            padding: 12px;
            border-bottom: 1px solid #e0e0e0;
            font-size: 14px;
        }
        
        table tbody tr:hover {
            background-color: #f9f9f9;
        }
        
        .badge {
            padding: 4px 8px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: 600;
        }
        
        .badge-success {
            background: #d4edda;
            color: #155724;
        }
        
        .badge-warning {
            background: #fff3cd;
            color: #856404;
        }
        
        .action-buttons {
            display: flex;
            gap: 5px;
        }
        
        .no-data {
            text-align: center;
            padding: 40px;
            color: #999;
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <div class="navbar">
        <h2>🛒 Minimarket - Gestión de Productos</h2>
        <div class="user-info">
            <p>Sesión: <strong><%= nombreUsuario %></strong></p>
            <a href="<%= request.getContextPath() %>/login?accion=logout" class="btn-logout">Cerrar</a>
        </div>
    </div>
    
    <div class="container">
        <!-- Botón volver -->
        <div style="margin-bottom: 20px;">
            <a href="<%= request.getContextPath() %>/menu.jsp" class="btn-primary">← Volver al Menú</a>
        </div>
        
        <!-- Mensajes -->
        <%
            String error = (String) request.getAttribute("error");
            String mensaje = (String) request.getAttribute("mensaje");
            List<String> errores = (List<String>) request.getAttribute("errores");
        %>
        
        <% if (error != null) { %>
            <div class="alert alert-error">✗ <%= error %></div>
        <% } %>
        
        <% if (mensaje != null) { %>
            <div class="alert alert-success">✓ <%= mensaje %></div>
        <% } %>
        
        <% if (errores != null && !errores.isEmpty()) { %>
            <div class="alert alert-error">
                ✗ Errores en el formulario:<br>
                <% for (String err : errores) { %>
                    • <%= err %><br>
                <% } %>
            </div>
        <% } %>
        
        <!-- Formulario de Registro/Actualización -->
        <div class="form-section">
            <h3>Registrar Nuevo Producto</h3>
            <form method="POST" action="<%= request.getContextPath() %>/producto">
                <input type="hidden" name="accion" value="guardar">
                
                <div class="form-row">
                    <div class="form-group">
                        <label>Código del Producto *</label>
                        <input type="text" name="codigo" required placeholder="Ej: PROD001">
                    </div>
                    <div class="form-group">
                        <label>Nombre del Producto *</label>
                        <input type="text" name="nombre" required placeholder="Ej: Arroz 1kg">
                    </div>
                    <div class="form-group">
                        <label>Categoría *</label>
                        <input type="text" name="categoria" required placeholder="Ej: Alimentos">
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label>Precio de Compra *</label>
                        <input type="number" name="precioCompra" step="0.01" required placeholder="0.00">
                    </div>
                    <div class="form-group">
                        <label>Precio de Venta *</label>
                        <input type="number" name="precioVenta" step="0.01" required placeholder="0.00">
                    </div>
                    <div class="form-group">
                        <label>Stock *</label>
                        <input type="number" name="stock" required placeholder="0">
                    </div>
                    <div class="form-group">
                        <label>Stock Mínimo *</label>
                        <input type="number" name="stockMinimo" required placeholder="5">
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label>Fecha de Vencimiento</label>
                        <input type="date" name="fechaVencimiento">
                    </div>
                </div>
                
                <div class="form-buttons">
                    <button type="submit" class="btn btn-save">💾 Guardar Producto</button>
                    <button type="reset" class="btn btn-cancel">🔄 Limpiar</button>
                </div>
            </form>
        </div>
        
        <!-- Búsqueda -->
        <div class="form-section">
            <h3>Buscar Producto</h3>
            <form method="GET" action="<%= request.getContextPath() %>/producto">
                <input type="hidden" name="accion" value="buscar">
                <div class="form-row">
                    <div class="form-group" style="grid-column: 1 / -1;">
                        <input type="text" name="nombre" placeholder="Ingrese el nombre del producto..." style="width: 100%;">
                    </div>
                </div>
                <button type="submit" class="btn-primary">🔍 Buscar</button>
            </form>
        </div>
        
        <!-- Tabla de Productos -->
        <div class="table-section">
            <h3>Listado de Productos</h3>
            
            <%
                List<Producto> productos = (List<Producto>) request.getAttribute("productos");
                String busqueda = (String) request.getAttribute("busqueda");
                String filtro = (String) request.getAttribute("filtro");
            %>
            
            <% if (busqueda != null) { %>
                <p style="margin-bottom: 10px; color: #666;">
                    Resultados para: <strong><%= busqueda %></strong> 
                    (<%= productos != null ? productos.size() : 0 %> producto(s))
                </p>
            <% } %>
            
            <% if (filtro != null) { %>
                <p style="margin-bottom: 10px; color: #666;">
                    <strong><%= filtro %></strong> 
                    (<%= productos != null ? productos.size() : 0 %> producto(s))
                </p>
            <% } %>
            
            <% if (productos != null && !productos.isEmpty()) { %>
                <table>
                    <thead>
                        <tr>
                            <th>Código</th>
                            <th>Nombre</th>
                            <th>Categoría</th>
                            <th>P. Compra</th>
                            <th>P. Venta</th>
                            <th>Stock</th>
                            <th>Stock Mín</th>
                            <th>Ganancia</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Producto p : productos) { %>
                            <tr>
                                <td><strong><%= p.getCodigo() %></strong></td>
                                <td><%= p.getNombre() %></td>
                                <td><%= p.getCategoria() %></td>
                                <td>S/. <%= String.format("%.2f", p.getPrecioCompra()) %></td>
                                <td>S/. <%= String.format("%.2f", p.getPrecioVenta()) %></td>
                                <td>
                                    <% if (p.esBajoStock()) { %>
                                        <span class="badge badge-warning"><%= p.getStock() %></span>
                                    <% } else { %>
                                        <span class="badge badge-success"><%= p.getStock() %></span>
                                    <% } %>
                                </td>
                                <td><%= p.getStockMinimo() %></td>
                                <td>S/. <%= String.format("%.2f", p.calcularGanancia()) %></td>
                                <td>
                                    <% if (p.isActivo()) { %>
                                        <span class="badge badge-success">Activo</span>
                                    <% } else { %>
                                        <span class="badge badge-warning">Inactivo</span>
                                    <% } %>
                                </td>
                                <td>
                                    <div class="action-buttons">
                                        <a href="<%= request.getContextPath() %>/producto?accion=editar&id=<%= p.getIdProducto() %>" 
                                           class="btn btn-edit">✏️ Editar</a>
                                        <form method="POST" action="<%= request.getContextPath() %>/producto" 
                                              style="display:inline;" 
                                              onsubmit="return confirm('¿Eliminar este producto?');">
                                            <input type="hidden" name="accion" value="eliminar">
                                            <input type="hidden" name="id" value="<%= p.getIdProducto() %>">
                                            <button type="submit" class="btn btn-delete">🗑️ Eliminar</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="no-data">
                    <p>📭 No hay productos para mostrar</p>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>

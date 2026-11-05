package controlador;

import dao.ProductoDAO;
import modelo.Producto;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet controlador para la gestión del inventario de productos (CRUD).
 */
@WebServlet(name = "ProductoServlet", urlPatterns = {"/producto"})
public class ProductoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 1. Validar autenticación de sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        // Capturar mensajes enviados por redirección (Patrón PRG)
        String mensajeParam = request.getParameter("mensaje");
        if (mensajeParam != null && !mensajeParam.trim().isEmpty()) {
            request.setAttribute("mensaje", mensajeParam);
        }
        String errorParam = request.getParameter("error");
        if (errorParam != null && !errorParam.trim().isEmpty()) {
            request.setAttribute("error", errorParam);
        }

        try {
            switch (accion) {
                case "editar":
                    // El rol Cajero no puede editar
                    if (usuario.isCajero()) {
                        request.setAttribute("error", "Acceso denegado: Su rol (Cajero) solo tiene permisos de consulta.");
                        request.setAttribute("productos", productoDAO.obtenerTodos());
                    } else {
                        cargarProductoParaEdicion(request);
                    }
                    break;

                case "buscar":
                    String criterio = request.getParameter("criterio");
                    if (criterio == null) {
                        criterio = request.getParameter("nombre"); // retrocompatibilidad
                    }
                    if (criterio != null && !criterio.trim().isEmpty()) {
                        List<Producto> productos = productoDAO.buscarPorNombreOCodigo(criterio);
                        request.setAttribute("productos", productos);
                        request.setAttribute("busqueda", criterio.trim());
                    } else {
                        request.setAttribute("productos", productoDAO.obtenerTodos());
                    }
                    break;

                case "bajo_stock":
                    request.setAttribute("productos", productoDAO.obtenerProductosBajoStock());
                    request.setAttribute("filtro", "Productos con Bajo Stock (Reposición Urgente)");
                    break;

                case "listar":
                default:
                    request.setAttribute("productos", productoDAO.obtenerTodos());
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error en ProductoServlet.doGet: " + e.getMessage());
            request.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            request.setAttribute("productos", productoDAO.obtenerTodos());
        }

        request.getRequestDispatcher("/productos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 1. Validar autenticación de sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String accion = request.getParameter("accion");

        // 2. Control de Acceso por Rol (RBAC): Cajero tiene prohibida cualquier mutación
        if (usuario.isCajero()) {
            String msg = URLEncoder.encode("Acceso denegado: El rol Cajero solo tiene permisos de consulta.", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/producto?error=" + msg);
            return;
        }

        try {
            if ("guardar".equalsIgnoreCase(accion)) {
                procesarGuardar(request, response);
            } else if ("actualizar".equalsIgnoreCase(accion)) {
                procesarActualizar(request, response);
            } else if ("eliminar".equalsIgnoreCase(accion)) {
                procesarEliminar(request, response, usuario);
            } else {
                response.sendRedirect(request.getContextPath() + "/producto");
            }
        } catch (Exception e) {
            System.err.println("Error general en ProductoServlet.doPost: " + e.getMessage());
            String msg = URLEncoder.encode("Error interno: " + e.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/producto?error=" + msg);
        }
    }

    private void cargarProductoParaEdicion(HttpServletRequest request) {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                Producto producto = productoDAO.obtenerPorId(id);
                if (producto != null) {
                    request.setAttribute("producto", producto);
                } else {
                    request.setAttribute("error", "El producto solicitado no fue encontrado.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Identificador de producto inválido.");
            }
        }
        request.setAttribute("productos", productoDAO.obtenerTodos());
    }

    private void procesarGuardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String codigo = request.getParameter("codigo");
        String nombre = request.getParameter("nombre");
        String categoria = request.getParameter("categoria");
        String precioCompraStr = request.getParameter("precioCompra");
        String precioVentaStr = request.getParameter("precioVenta");
        String stockStr = request.getParameter("stock");
        String stockMinimoStr = request.getParameter("stockMinimo");
        String fechaVencimientoStr = request.getParameter("fechaVencimiento");

        List<String> errores = validarFormulario(codigo, nombre, categoria, precioCompraStr,
                precioVentaStr, stockStr, stockMinimoStr, fechaVencimientoStr);

        // Validar código único
        if (codigo != null && !codigo.trim().isEmpty() && productoDAO.codigoExiste(codigo.trim())) {
            errores.add("El código '" + codigo.trim() + "' ya se encuentra registrado.");
        }

        if (!errores.isEmpty()) {
            Producto formProd = construirProductoDesdeParams(0, codigo, nombre, categoria, precioCompraStr,
                    precioVentaStr, stockStr, stockMinimoStr, fechaVencimientoStr);
            request.setAttribute("producto", formProd);
            request.setAttribute("errores", errores);
            request.setAttribute("productos", productoDAO.obtenerTodos());
            request.getRequestDispatcher("/productos.jsp").forward(request, response);
            return;
        }

        Producto nuevo = construirProductoDesdeParams(0, codigo, nombre, categoria, precioCompraStr,
                precioVentaStr, stockStr, stockMinimoStr, fechaVencimientoStr);

        boolean guardado = productoDAO.insertar(nuevo);
        if (guardado) {
            String msg = URLEncoder.encode("Producto '" + nuevo.getNombre() + "' registrado exitosamente.", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/producto?mensaje=" + msg);
        } else {
            request.setAttribute("error", "No se pudo guardar el producto en la base de datos.");
            request.setAttribute("productos", productoDAO.obtenerTodos());
            request.getRequestDispatcher("/productos.jsp").forward(request, response);
        }
    }

    private void procesarActualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("idProducto");
        int idProducto = 0;
        try {
            idProducto = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            String msg = URLEncoder.encode("ID de producto inválido para actualización.", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/producto?error=" + msg);
            return;
        }

        String codigo = request.getParameter("codigo");
        String nombre = request.getParameter("nombre");
        String categoria = request.getParameter("categoria");
        String precioCompraStr = request.getParameter("precioCompra");
        String precioVentaStr = request.getParameter("precioVenta");
        String stockStr = request.getParameter("stock");
        String stockMinimoStr = request.getParameter("stockMinimo");
        String fechaVencimientoStr = request.getParameter("fechaVencimiento");

        List<String> errores = validarFormulario(codigo, nombre, categoria, precioCompraStr,
                precioVentaStr, stockStr, stockMinimoStr, fechaVencimientoStr);

        // Validar código único excluyendo el ID actual
        if (codigo != null && !codigo.trim().isEmpty() && productoDAO.codigoExisteParaOtroId(codigo.trim(), idProducto)) {
            errores.add("El código '" + codigo.trim() + "' ya está en uso por otro producto.");
        }

        if (!errores.isEmpty()) {
            Producto formProd = construirProductoDesdeParams(idProducto, codigo, nombre, categoria, precioCompraStr,
                    precioVentaStr, stockStr, stockMinimoStr, fechaVencimientoStr);
            request.setAttribute("producto", formProd);
            request.setAttribute("errores", errores);
            request.setAttribute("productos", productoDAO.obtenerTodos());
            request.getRequestDispatcher("/productos.jsp").forward(request, response);
            return;
        }

        Producto producto = construirProductoDesdeParams(idProducto, codigo, nombre, categoria, precioCompraStr,
                precioVentaStr, stockStr, stockMinimoStr, fechaVencimientoStr);

        boolean actualizado = productoDAO.actualizar(producto);
        if (actualizado) {
            String msg = URLEncoder.encode("Producto actualizado correctamente.", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/producto?mensaje=" + msg);
        } else {
            request.setAttribute("error", "No se pudo actualizar el producto.");
            request.setAttribute("producto", producto);
            request.setAttribute("productos", productoDAO.obtenerTodos());
            request.getRequestDispatcher("/productos.jsp").forward(request, response);
        }
    }

    private void procesarEliminar(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException {

        // Solo Administrador o Almacenero pueden desactivar productos
        if (!usuario.isAdministrador() && !usuario.isAlmacenero()) {
            String msg = URLEncoder.encode("No posee permisos suficientes para desactivar productos.", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/producto?error=" + msg);
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                boolean eliminado = productoDAO.desactivar(id);
                if (eliminado) {
                    String msg = URLEncoder.encode("Producto eliminado (desactivado) correctamente.", StandardCharsets.UTF_8);
                    response.sendRedirect(request.getContextPath() + "/producto?mensaje=" + msg);
                    return;
                } else {
                    String msg = URLEncoder.encode("No se pudo desactivar el producto.", StandardCharsets.UTF_8);
                    response.sendRedirect(request.getContextPath() + "/producto?error=" + msg);
                    return;
                }
            } catch (NumberFormatException e) {
                String msg = URLEncoder.encode("Identificador de producto no válido.", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/producto?error=" + msg);
                return;
            }
        }
        response.sendRedirect(request.getContextPath() + "/producto");
    }

    private List<String> validarFormulario(String codigo, String nombre, String categoria,
                                          String precioCompraStr, String precioVentaStr,
                                          String stockStr, String stockMinimoStr,
                                          String fechaVencimientoStr) {

        List<String> errores = new ArrayList<>();

        if (codigo == null || codigo.trim().isEmpty()) {
            errores.add("El código del producto es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            errores.add("El nombre del producto es obligatorio.");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            errores.add("La categoría es obligatoria.");
        }

        BigDecimal pCompra = null;
        try {
            if (precioCompraStr == null || precioCompraStr.trim().isEmpty()) {
                errores.add("El precio de compra es obligatorio.");
            } else {
                pCompra = new BigDecimal(precioCompraStr.trim());
                if (pCompra.compareTo(BigDecimal.ZERO) <= 0) {
                    errores.add("El precio de compra debe ser estrictamente mayor a 0.");
                }
            }
        } catch (NumberFormatException e) {
            errores.add("El precio de compra no tiene un formato decimal válido.");
        }

        BigDecimal pVenta = null;
        try {
            if (precioVentaStr == null || precioVentaStr.trim().isEmpty()) {
                errores.add("El precio de venta es obligatorio.");
            } else {
                pVenta = new BigDecimal(precioVentaStr.trim());
                if (pVenta.compareTo(BigDecimal.ZERO) <= 0) {
                    errores.add("El precio de venta debe ser estrictamente mayor a 0.");
                }
            }
        } catch (NumberFormatException e) {
            errores.add("El precio de venta no tiene un formato decimal válido.");
        }

        if (pCompra != null && pVenta != null && pVenta.compareTo(pCompra) < 0) {
            errores.add("Aviso/Advertencia: El precio de venta no debería ser menor al precio de compra.");
        }

        try {
            if (stockStr == null || stockStr.trim().isEmpty()) {
                errores.add("El stock actual es obligatorio.");
            } else {
                int stock = Integer.parseInt(stockStr.trim());
                if (stock < 0) {
                    errores.add("El stock no puede ser un número negativo.");
                }
            }
        } catch (NumberFormatException e) {
            errores.add("El stock debe ser un número entero válido.");
        }

        try {
            if (stockMinimoStr == null || stockMinimoStr.trim().isEmpty()) {
                errores.add("El stock mínimo es obligatorio.");
            } else {
                int stockMin = Integer.parseInt(stockMinimoStr.trim());
                if (stockMin < 0) {
                    errores.add("El stock mínimo no puede ser negativo.");
                }
            }
        } catch (NumberFormatException e) {
            errores.add("El stock mínimo debe ser un número entero válido.");
        }

        if (fechaVencimientoStr != null && !fechaVencimientoStr.trim().isEmpty()) {
            try {
                LocalDate.parse(fechaVencimientoStr.trim(), dateFormatter);
            } catch (DateTimeParseException e) {
                errores.add("La fecha de vencimiento debe tener el formato válido (AAAA-MM-DD).");
            }
        }

        return errores;
    }

    private Producto construirProductoDesdeParams(int id, String codigo, String nombre, String categoria,
                                                 String pCompraStr, String pVentaStr, String stockStr,
                                                 String stockMinStr, String fechaVencStr) {
        Producto p = new Producto();
        p.setIdProducto(id);
        p.setCodigo(codigo != null ? codigo.trim() : "");
        p.setNombre(nombre != null ? nombre.trim() : "");
        p.setCategoria(categoria != null ? categoria.trim() : "");

        try {
            p.setPrecioCompra(new BigDecimal(pCompraStr.trim()));
        } catch (Exception ignored) {}

        try {
            p.setPrecioVenta(new BigDecimal(pVentaStr.trim()));
        } catch (Exception ignored) {}

        try {
            p.setStock(Integer.parseInt(stockStr.trim()));
        } catch (Exception ignored) {}

        try {
            p.setStockMinimo(Integer.parseInt(stockMinStr.trim()));
        } catch (Exception ignored) {}

        if (fechaVencStr != null && !fechaVencStr.trim().isEmpty()) {
            try {
                p.setFechaVencimiento(LocalDate.parse(fechaVencStr.trim(), dateFormatter));
            } catch (Exception ignored) {}
        }
        p.setEstado(1);
        return p;
    }
}

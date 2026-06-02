package controlador;

import dao.ProductoDAO;
import modelo.Producto;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet controlador para gestionar productos.
 * Implementa operaciones CRUD: Crear, Leer, Actualizar, Eliminar.
 * 
 * @author Sistema de Inventario
 * @version 1.0
 * @since 2026-06-01
 */
@WebServlet(name = "ProductoServlet", urlPatterns = {"/producto"})
public class ProductoServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private ProductoDAO productoDAO = new ProductoDAO();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Procesa solicitudes GET
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Validar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String accion = request.getParameter("accion");
        
        if ("editar".equals(accion)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    Producto producto = productoDAO.obtenerPorId(id);
                    request.setAttribute("producto", producto);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "ID inválido");
                }
            }
        } else if ("buscar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            if (nombre != null && !nombre.trim().isEmpty()) {
                List<Producto> productos = productoDAO.buscarPorNombre(nombre);
                request.setAttribute("productos", productos);
                request.setAttribute("busqueda", nombre);
            }
        } else if ("bajo_stock".equals(accion)) {
            List<Producto> productos = productoDAO.obtenerProductosBajoStock();
            request.setAttribute("productos", productos);
            request.setAttribute("filtro", "Productos con Bajo Stock");
        } else {
            List<Producto> productos = productoDAO.obtenerTodos();
            request.setAttribute("productos", productos);
        }
        
        request.getRequestDispatcher("/productos.jsp").forward(request, response);
    }
    
    /**
     * Procesa solicitudes POST
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Validar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String accion = request.getParameter("accion");
        
        try {
            if ("guardar".equals(accion)) {
                guardarProducto(request, response);
            } else if ("actualizar".equals(accion)) {
                actualizarProducto(request, response);
            } else if ("eliminar".equals(accion)) {
                eliminarProducto(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            request.setAttribute("error", "Error al procesar producto");
        }
        
        doGet(request, response);
    }
    
    /**
     * Guarda un nuevo producto
     */
    private void guardarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String codigo = request.getParameter("codigo");
        String nombre = request.getParameter("nombre");
        String categoria = request.getParameter("categoria");
        String precioCompraStr = request.getParameter("precioCompra");
        String precioVentaStr = request.getParameter("precioVenta");
        String stockStr = request.getParameter("stock");
        String stockMinimoStr = request.getParameter("stockMinimo");
        String fechaVencimientoStr = request.getParameter("fechaVencimiento");
        
        // Validaciones
        List<String> errores = validarProducto(codigo, nombre, categoria, precioCompraStr,
                                                precioVentaStr, stockStr, stockMinimoStr);
        
        if (!errores.isEmpty()) {
            request.setAttribute("errores", errores);
            request.getRequestDispatcher("/productos.jsp").forward(request, response);
            return;
        }
        
        // Verificar código único
        if (productoDAO.codigoExiste(codigo)) {
            request.setAttribute("error", "El código de producto ya existe");
            request.getRequestDispatcher("/productos.jsp").forward(request, response);
            return;
        }
        
        // Crear producto
        Producto producto = new Producto();
        producto.setCodigo(codigo);
        producto.setNombre(nombre);
        producto.setCategoria(categoria);
        producto.setPrecioCompra(new BigDecimal(precioCompraStr));
        producto.setPrecioVenta(new BigDecimal(precioVentaStr));
        producto.setStock(Integer.parseInt(stockStr));
        producto.setStockMinimo(Integer.parseInt(stockMinimoStr));
        
        if (fechaVencimientoStr != null && !fechaVencimientoStr.isEmpty()) {
            producto.setFechaVencimiento(LocalDate.parse(fechaVencimientoStr, dateFormatter));
        }
        
        // Guardar
        if (productoDAO.insertar(producto)) {
            request.setAttribute("mensaje", "Producto registrado correctamente");
        } else {
            request.setAttribute("error", "Error al registrar producto");
        }
    }
    
    /**
     * Actualiza un producto
     */
    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("idProducto");
        
        try {
            int id = Integer.parseInt(idStr);
            String nombre = request.getParameter("nombre");
            String categoria = request.getParameter("categoria");
            String precioCompraStr = request.getParameter("precioCompra");
            String precioVentaStr = request.getParameter("precioVenta");
            String stockStr = request.getParameter("stock");
            String stockMinimoStr = request.getParameter("stockMinimo");
            String fechaVencimientoStr = request.getParameter("fechaVencimiento");
            
            Producto producto = new Producto();
            producto.setIdProducto(id);
            producto.setNombre(nombre);
            producto.setCategoria(categoria);
            producto.setPrecioCompra(new BigDecimal(precioCompraStr));
            producto.setPrecioVenta(new BigDecimal(precioVentaStr));
            producto.setStock(Integer.parseInt(stockStr));
            producto.setStockMinimo(Integer.parseInt(stockMinimoStr));
            
            if (fechaVencimientoStr != null && !fechaVencimientoStr.isEmpty()) {
                producto.setFechaVencimiento(LocalDate.parse(fechaVencimientoStr, dateFormatter));
            }
            
            if (productoDAO.actualizar(producto)) {
                request.setAttribute("mensaje", "Producto actualizado correctamente");
            } else {
                request.setAttribute("error", "Error al actualizar");
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un producto
     */
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        try {
            int id = Integer.parseInt(idStr);
            if (productoDAO.desactivar(id)) {
                request.setAttribute("mensaje", "Producto eliminado correctamente");
            } else {
                request.setAttribute("error", "Error al eliminar");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
        }
    }
    
    /**
     * Valida los datos del producto
     */
    private List<String> validarProducto(String codigo, String nombre, String categoria,
                                         String precioCompra, String precioVenta,
                                         String stock, String stockMinimo) {
        
        List<String> errores = new ArrayList<>();
        
        if (codigo == null || codigo.trim().isEmpty()) {
            errores.add("El código es obligatorio");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            errores.add("El nombre es obligatorio");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            errores.add("La categoría es obligatoria");
        }
        
        try {
            if (precioCompra == null || precioCompra.trim().isEmpty()) {
                errores.add("El precio de compra es obligatorio");
            } else if (new BigDecimal(precioCompra).compareTo(BigDecimal.ZERO) <= 0) {
                errores.add("El precio de compra debe ser mayor a cero");
            }
        } catch (NumberFormatException e) {
            errores.add("Precio de compra inválido");
        }
        
        try {
            if (precioVenta == null || precioVenta.trim().isEmpty()) {
                errores.add("El precio de venta es obligatorio");
            } else if (new BigDecimal(precioVenta).compareTo(BigDecimal.ZERO) <= 0) {
                errores.add("El precio de venta debe ser mayor a cero");
            }
        } catch (NumberFormatException e) {
            errores.add("Precio de venta inválido");
        }
        
        try {
            if (stock == null || stock.trim().isEmpty()) {
                errores.add("El stock es obligatorio");
            } else if (Integer.parseInt(stock) < 0) {
                errores.add("El stock no puede ser negativo");
            }
        } catch (NumberFormatException e) {
            errores.add("Stock inválido");
        }
        
        try {
            if (stockMinimo == null || stockMinimo.trim().isEmpty()) {
                errores.add("El stock mínimo es obligatorio");
            } else if (Integer.parseInt(stockMinimo) < 0) {
                errores.add("El stock mínimo no puede ser negativo");
            }
        } catch (NumberFormatException e) {
            errores.add("Stock mínimo inválido");
        }
        
        return errores;
    }
}

package dao;

import modelo.Producto;
import util.Conexion;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la entidad Producto.
 * Implementa el patrón DAO para encapsular la lógica de acceso a datos.
 * 
 * @author Sistema de Inventario
 * @version 1.0
 * @since 2026-06-01
 */
public class ProductoDAO {
    
    /**
     * Obtiene un producto por su ID.
     * 
     * @param idProducto ID del producto
     * @return Producto encontrado, o null si no existe
     */
    public Producto obtenerPorId(int idProducto) {
        Producto producto = null;
        String sql = "SELECT id_producto, codigo, nombre, categoria, precio_compra, " +
                     "precio_venta, stock, stock_minimo, fecha_vencimiento, estado " +
                     "FROM productos WHERE id_producto = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idProducto);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto = crearProductoDesdeResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error en obtenerPorId: " + e.getMessage());
        }
        
        return producto;
    }
    
    /**
     * Obtiene todos los productos activos.
     * 
     * @return Lista de productos
     */
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, codigo, nombre, categoria, precio_compra, " +
                     "precio_venta, stock, stock_minimo, fecha_vencimiento, estado " +
                     "FROM productos WHERE estado = 1 ORDER BY categoria, nombre";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                productos.add(crearProductoDesdeResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error en obtenerTodos: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * Busca productos por nombre.
     * 
     * @param nombre Nombre a buscar
     * @return Lista de productos encontrados
     */
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, codigo, nombre, categoria, precio_compra, " +
                     "precio_venta, stock, stock_minimo, fecha_vencimiento, estado " +
                     "FROM productos WHERE nombre LIKE ? AND estado = 1 ORDER BY nombre";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + nombre + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(crearProductoDesdeResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error en buscarPorNombre: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * Obtiene productos con bajo stock.
     * 
     * @return Lista de productos con stock bajo
     */
    public List<Producto> obtenerProductosBajoStock() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, codigo, nombre, categoria, precio_compra, " +
                     "precio_venta, stock, stock_minimo, fecha_vencimiento, estado " +
                     "FROM productos WHERE stock <= stock_minimo AND estado = 1 " +
                     "ORDER BY stock ASC";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                productos.add(crearProductoDesdeResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error en obtenerProductosBajoStock: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * Inserta un nuevo producto.
     * 
     * @param producto Producto a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (codigo, nombre, categoria, precio_compra, " +
                     "precio_venta, stock, stock_minimo, fecha_vencimiento, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setBigDecimal(4, producto.getPrecioCompra());
            ps.setBigDecimal(5, producto.getPrecioVenta());
            ps.setInt(6, producto.getStock());
            ps.setInt(7, producto.getStockMinimo());
            
            if (producto.getFechaVencimiento() != null) {
                ps.setDate(8, java.sql.Date.valueOf(producto.getFechaVencimiento()));
            } else {
                ps.setNull(8, java.sql.Types.DATE);
            }
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Actualiza un producto existente.
     * 
     * @param producto Producto a actualizar
     * @return true si se actualizó correctamente
     */
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, categoria = ?, " +
                     "precio_compra = ?, precio_venta = ?, stock = ?, " +
                     "stock_minimo = ?, fecha_vencimiento = ? WHERE id_producto = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getCategoria());
            ps.setBigDecimal(3, producto.getPrecioCompra());
            ps.setBigDecimal(4, producto.getPrecioVenta());
            ps.setInt(5, producto.getStock());
            ps.setInt(6, producto.getStockMinimo());
            
            if (producto.getFechaVencimiento() != null) {
                ps.setDate(7, java.sql.Date.valueOf(producto.getFechaVencimiento()));
            } else {
                ps.setNull(7, java.sql.Types.DATE);
            }
            
            ps.setInt(8, producto.getIdProducto());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Desactiva un producto.
     * 
     * @param idProducto ID del producto
     * @return true si se desactivó correctamente
     */
    public boolean desactivar(int idProducto) {
        String sql = "UPDATE productos SET estado = 0 WHERE id_producto = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al desactivar producto: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Verifica si un código existe.
     * 
     * @param codigo Código a verificar
     * @return true si existe
     */
    public boolean codigoExiste(String codigo) {
        String sql = "SELECT 1 FROM productos WHERE codigo = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, codigo);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar código: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Obtiene el total de productos.
     * 
     * @return Cantidad de productos
     */
    public int obtenerTotal() {
        String sql = "SELECT COUNT(*) as total FROM productos WHERE estado = 1";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener total: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Método auxiliar para crear Producto desde ResultSet.
     */
    private Producto crearProductoDesdeResultSet(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("id_producto"));
        producto.setCodigo(rs.getString("codigo"));
        producto.setNombre(rs.getString("nombre"));
        producto.setCategoria(rs.getString("categoria"));
        producto.setPrecioCompra(rs.getBigDecimal("precio_compra"));
        producto.setPrecioVenta(rs.getBigDecimal("precio_venta"));
        producto.setStock(rs.getInt("stock"));
        producto.setStockMinimo(rs.getInt("stock_minimo"));
        
        Date sqlDate = rs.getDate("fecha_vencimiento");
        if (sqlDate != null) {
            producto.setFechaVencimiento(sqlDate.toLocalDate());
        }
        
        producto.setEstado(rs.getInt("estado"));
        return producto;
    }
}

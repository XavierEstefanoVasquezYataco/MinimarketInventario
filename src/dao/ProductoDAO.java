package dao;

import modelo.Producto;
import util.Conexion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la entidad Producto.
 * Encapsula todas las operaciones sobre la tabla productos.
 */
public class ProductoDAO {

    /**
     * Obtiene un producto por su ID.
     */
    public Producto obtenerPorId(int idProducto) {
        String sql = "SELECT id_producto, codigo, nombre, categoria, precio_compra, " +
                     "precio_venta, stock, stock_minimo, fecha_vencimiento, estado " +
                     "FROM productos WHERE id_producto = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearProductoDesdeResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en ProductoDAO.obtenerPorId: " + e.getMessage());
        }

        return null;
    }

    /**
     * Obtiene todos los productos activos ordenados por categoría y nombre.
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
            System.err.println("Error en ProductoDAO.obtenerTodos: " + e.getMessage());
        }

        return productos;
    }

    /**
     * Busca productos por nombre o por código.
     */
    public List<Producto> buscarPorNombreOCodigo(String criterio) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, codigo, nombre, categoria, precio_compra, " +
                     "precio_venta, stock, stock_minimo, fecha_vencimiento, estado " +
                     "FROM productos WHERE (nombre LIKE ? OR codigo LIKE ?) AND estado = 1 ORDER BY nombre";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + (criterio != null ? criterio.trim() : "") + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(crearProductoDesdeResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en ProductoDAO.buscarPorNombreOCodigo: " + e.getMessage());
        }

        return productos;
    }

    /**
     * Busca productos por nombre (conservado por compatibilidad).
     */
    public List<Producto> buscarPorNombre(String nombre) {
        return buscarPorNombreOCodigo(nombre);
    }

    /**
     * Obtiene productos cuyo stock actual sea menor o igual al stock mínimo.
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
            System.err.println("Error en ProductoDAO.obtenerProductosBajoStock: " + e.getMessage());
        }

        return productos;
    }

    /**
     * Inserta un nuevo producto en la base de datos.
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
                ps.setDate(8, Date.valueOf(producto.getFechaVencimiento()));
            } else {
                ps.setNull(8, Types.DATE);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en ProductoDAO.insertar: " + e.getMessage());
        }

        return false;
    }

    /**
     * Actualiza un producto existente.
     */
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET codigo = ?, nombre = ?, categoria = ?, " +
                     "precio_compra = ?, precio_venta = ?, stock = ?, " +
                     "stock_minimo = ?, fecha_vencimiento = ? WHERE id_producto = ?";

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
                ps.setDate(8, Date.valueOf(producto.getFechaVencimiento()));
            } else {
                ps.setNull(8, Types.DATE);
            }

            ps.setInt(9, producto.getIdProducto());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en ProductoDAO.actualizar: " + e.getMessage());
        }

        return false;
    }

    /**
     * Desactiva lógicamente un producto (estado = 0).
     */
    public boolean desactivar(int idProducto) {
        String sql = "UPDATE productos SET estado = 0 WHERE id_producto = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en ProductoDAO.desactivar: " + e.getMessage());
        }

        return false;
    }

    /**
     * Verifica si un código ya existe en algún producto.
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
            System.err.println("Error en ProductoDAO.codigoExiste: " + e.getMessage());
        }

        return false;
    }

    /**
     * Verifica si un código ya pertenece a otro producto diferente al ID proporcionado.
     */
    public boolean codigoExisteParaOtroId(String codigo, int idProducto) {
        String sql = "SELECT 1 FROM productos WHERE codigo = ? AND id_producto != ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setInt(2, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Error en ProductoDAO.codigoExisteParaOtroId: " + e.getMessage());
        }

        return false;
    }

    /**
     * Obtiene el total de productos activos.
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
            System.err.println("Error en ProductoDAO.obtenerTotal: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Mapea una fila de ResultSet a un objeto Producto.
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

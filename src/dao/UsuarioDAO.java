package dao;

import modelo.Usuario;
import util.Conexion;
import util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la entidad Usuario.
 */
public class UsuarioDAO {

    /**
     * Autentica a un usuario por correo y contraseña.
     * 
     * @param correo Correo electrónico
     * @param password Contraseña en texto plano
     * @return Usuario autenticado o null si las credenciales son inválidas
     */
    public Usuario autenticar(String correo, String password) {
        if (correo == null || password == null) {
            return null;
        }

        String sql = "SELECT id_usuario, nombres, apellidos, correo, password, rol, estado " +
                     "FROM usuarios WHERE correo = ? AND estado = 1";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashAlmacenado = rs.getString("password");
                    if (PasswordUtil.verificar(password, hashAlmacenado)) {
                        return mapearUsuario(rs);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.autenticar: " + e.getMessage());
        }

        return null;
    }

    /**
     * Obtiene un usuario por su ID.
     */
    public Usuario obtenerPorId(int idUsuario) {
        String sql = "SELECT id_usuario, nombres, apellidos, correo, password, rol, estado " +
                     "FROM usuarios WHERE id_usuario = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.obtenerPorId: " + e.getMessage());
        }

        return null;
    }

    /**
     * Obtiene todos los usuarios activos.
     */
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombres, apellidos, correo, password, rol, estado " +
                     "FROM usuarios WHERE estado = 1 ORDER BY apellidos, nombres";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.obtenerTodos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Inserta un nuevo usuario con contraseña hasheada.
     */
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombres, apellidos, correo, password, rol, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombres());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, PasswordUtil.hash(usuario.getPassword()));
            ps.setString(5, usuario.getRol());
            ps.setInt(6, usuario.getEstado());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.insertar: " + e.getMessage());
        }

        return false;
    }

    /**
     * Verifica si un correo ya se encuentra registrado.
     */
    public boolean correoExiste(String correo) {
        String sql = "SELECT 1 FROM usuarios WHERE correo = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.correoExiste: " + e.getMessage());
        }

        return false;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombres(rs.getString("nombres"));
        u.setApellidos(rs.getString("apellidos"));
        u.setCorreo(rs.getString("correo"));
        u.setPassword(rs.getString("password"));
        u.setRol(rs.getString("rol"));
        u.setEstado(rs.getInt("estado"));
        return u;
    }
}

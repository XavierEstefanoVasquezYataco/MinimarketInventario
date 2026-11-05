package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestor de conexión JDBC a la base de datos MySQL.
 */
public class Conexion {

    // Configuración de la base de datos
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String HOST = "localhost";
    private static final String PUERTO = "3306";
    private static final String DATABASE = "minimarket_db";
    private static final String PARAMS = "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + DATABASE + PARAMS;
    private static final String USER = "root";
    
    // Contraseña de MySQL: cambiar aquí si tu servidor MySQL tiene contraseña (por ejemplo "root", "1234", etc.)
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Error crítico: Driver MySQL no encontrado en el classpath: " + e.getMessage());
        }
    }

    /**
     * Obtiene una nueva conexión a la base de datos.
     * 
     * @return Connection abierta
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Cierra de manera segura recursos JDBC.
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignored) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }
}

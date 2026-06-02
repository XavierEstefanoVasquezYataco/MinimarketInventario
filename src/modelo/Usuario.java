package modelo;

import java.io.Serializable;

/**
 * Clase modelo que representa un usuario del sistema
 * Implementa Serializable para ser almacenado en sesión
 */
public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String password;
    private String rol;
    private int estado;
    
    // Constructor vacío
    public Usuario() {
    }
    
    // Constructor con todos los parámetros
    public Usuario(int idUsuario, String nombres, String apellidos, String correo, 
                   String password, String rol, int estado) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }
    
    // Constructor para autenticación (sin id ni estado)
    public Usuario(String nombres, String apellidos, String correo, String rol) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.rol = rol;
        this.estado = 1;
    }
    
    // ============ GETTERS ============
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public String getNombres() {
        return nombres;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getRol() {
        return rol;
    }
    
    public int getEstado() {
        return estado;
    }
    
    // ============ SETTERS ============
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    // ============ MÉTODOS DE NEGOCIO ============
    
    /**
     * Obtiene el nombre completo del usuario
     * @return nombre y apellido concatenados
     */
    public String getNombreCompleto() {
        return this.nombres + " " + this.apellidos;
    }
    
    /**
     * Valida si el usuario está activo
     * @return true si estado es 1, false si es 0
     */
    public boolean isActivo() {
        return this.estado == 1;
    }
    
    /**
     * Valida si el usuario es administrador
     * @return true si el rol es "Administrador"
     */
    public boolean isAdministrador() {
        return "Administrador".equalsIgnoreCase(this.rol);
    }
    
    /**
     * Valida si el usuario es almacenero
     * @return true si el rol es "Almacenero"
     */
    public boolean isAlmacenero() {
        return "Almacenero".equalsIgnoreCase(this.rol);
    }
    
    /**
     * Valida si el usuario es cajero
     * @return true si el rol es "Cajero"
     */
    public boolean isCajero() {
        return "Cajero".equalsIgnoreCase(this.rol);
    }
    
    /**
     * Representación en String del objeto Usuario
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correo='" + correo + '\'' +
                ", rol='" + rol + '\'' +
                ", estado=" + estado +
                '}';
    }
}

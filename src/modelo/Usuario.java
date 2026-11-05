package modelo;

import java.io.Serializable;

/**
 * Modelo que representa a un usuario del sistema de inventario.
 */
public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String password;
    private String rol; // 'Administrador', 'Almacenero', 'Cajero'
    private int estado; // 1 = Activo, 0 = Inactivo
    
    public Usuario() {
    }
    
    public Usuario(int idUsuario, String nombres, String apellidos, String correo, String password, String rol, int estado) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getNombres() {
        return nombres;
    }
    
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public String getNombreCompleto() {
        String n = (nombres != null) ? nombres.trim() : "";
        String a = (apellidos != null) ? apellidos.trim() : "";
        return (n + " " + a).trim();
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public int getEstado() {
        return estado;
    }
    
    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public boolean isAdministrador() {
        return "Administrador".equalsIgnoreCase(rol);
    }
    
    public boolean isAlmacenero() {
        return "Almacenero".equalsIgnoreCase(rol);
    }
    
    public boolean isCajero() {
        return "Cajero".equalsIgnoreCase(rol);
    }
    
    public boolean isActivo() {
        return this.estado == 1;
    }
}

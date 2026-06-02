package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet controlador para gestionar la autenticación de usuarios.
 * Maneja el inicio y cierre de sesión.
 * 
 * @author Sistema de Inventario
 * @version 1.0
 * @since 2026-06-01
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    /**
     * Procesa solicitudes GET (redirección a login)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if ("logout".equals(accion)) {
            HttpSession session = request.getSession();
            session.invalidate();
            request.setAttribute("mensaje", "Sesión cerrada correctamente");
        }
        
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    /**
     * Procesa solicitudes POST (autenticación)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        
        // Validaciones
        if (correo == null || correo.trim().isEmpty()) {
            request.setAttribute("error", "El correo es requerido");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "La contraseña es requerida");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        try {
            // Autenticar usuario
            Usuario usuario = usuarioDAO.autenticar(correo, password);
            
            if (usuario != null) {
                // Crear sesión
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", usuario);
                session.setAttribute("idUsuario", usuario.getIdUsuario());
                session.setAttribute("nombreUsuario", usuario.getNombreCompleto());
                session.setAttribute("rol", usuario.getRol());
                session.setMaxInactiveInterval(30 * 60);
                
                System.out.println("✓ Login: " + usuario.getNombreCompleto());
                response.sendRedirect(request.getContextPath() + "/menu.jsp");
                
            } else {
                request.setAttribute("error", "Correo o contraseña incorrectos");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            request.setAttribute("error", "Error en la autenticación");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}

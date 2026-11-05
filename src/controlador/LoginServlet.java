package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet controlador para gestionar la autenticación de usuarios.
 * Maneja el inicio y cierre de sesión.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Procesa solicitudes GET (redirección a login o cierre de sesión).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("logout".equalsIgnoreCase(accion)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            request.setAttribute("mensaje", "Sesión cerrada correctamente.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Si ya tiene sesión activa, redirigir al menú principal
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/menu.jsp");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * Procesa solicitudes POST (autenticación de credenciales).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        // Validaciones de entrada
        if (correo == null || correo.trim().isEmpty()) {
            request.setAttribute("error", "El correo electrónico es obligatorio.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "La contraseña es obligatoria.");
            request.setAttribute("correo", correo);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(correo.trim(), password);

            if (usuario != null) {
                // Crear sesión y almacenar atributos del usuario
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", usuario);
                session.setAttribute("idUsuario", usuario.getIdUsuario());
                session.setAttribute("nombreUsuario", usuario.getNombreCompleto());
                session.setAttribute("rol", usuario.getRol());
                session.setMaxInactiveInterval(30 * 60); // 30 minutos

                response.sendRedirect(request.getContextPath() + "/menu.jsp");
            } else {
                request.setAttribute("error", "Correo o contraseña incorrectos, o cuenta inactiva.");
                request.setAttribute("correo", correo);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Error en autenticación: " + e.getMessage());
            request.setAttribute("error", "Error interno al procesar el inicio de sesión.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}

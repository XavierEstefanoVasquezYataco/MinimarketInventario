package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para el hashing y verificación segura de contraseñas usando SHA-256.
 */
public class PasswordUtil {

    /**
     * Genera un hash SHA-256 en formato hexadecimal de 64 caracteres.
     * Compatible directamente con la función SHA2(password, 256) de MySQL.
     *
     * @param password Contraseña en texto plano
     * @return Hash SHA-256 hexadecimal o null si el parámetro es nulo
     */
    public static String hash(String password) {
        if (password == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash SHA-256", e);
        }
    }

    /**
     * Verifica si una contraseña en texto plano coincide con el hash almacenado.
     *
     * @param passwordPlano Contraseña ingresada por el usuario
     * @param hashAlmacenado Hash almacenado en la base de datos
     * @return true si coinciden
     */
    public static boolean verificar(String passwordPlano, String hashAlmacenado) {
        if (passwordPlano == null || hashAlmacenado == null) {
            return false;
        }
        String hashCalculado = hash(passwordPlano);
        return hashCalculado.equalsIgnoreCase(hashAlmacenado.trim());
    }
}

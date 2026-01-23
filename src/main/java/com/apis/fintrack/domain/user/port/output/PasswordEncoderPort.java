package com.apis.fintrack.domain.user.port.output;

/**
 * Puerto de salida para el servicio de codificaciÃ³n de contraseÃ±as.
 * 
 * Define el contrato que debe implementar el adaptador de seguridad
 * (Infrastructure layer) para hashear y verificar contraseÃ±as.
 * 
 * NOTA: Esta interfaz NO tiene dependencias de Spring Security.
 * Es una abstracciÃ³n pura del dominio.
 */
public interface PasswordEncoderPort {
    
    /**
     * Codifica (hashea) una contraseÃ±a en texto plano.
     * 
     * @param rawPassword la contraseÃ±a en texto plano
     * @return la contraseÃ±a hasheada
     */
    String encode(String rawPassword);
    
    /**
     * Verifica si una contraseÃ±a en texto plano coincide con una hasheada.
     * 
     * @param rawPassword la contraseÃ±a en texto plano
     * @param encodedPassword la contraseÃ±a hasheada
     * @return true si coinciden
     */
    boolean matches(String rawPassword, String encodedPassword);
}




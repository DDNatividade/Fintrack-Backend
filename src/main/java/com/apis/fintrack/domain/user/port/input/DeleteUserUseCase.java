package com.apis.fintrack.domain.user.port.input;

/**
 * Puerto de entrada para el caso de uso de eliminaciÃ³n de usuario.
 * 
 * Define el contrato que debe implementar la capa de aplicaciÃ³n
 * para eliminar usuarios del sistema.
 */
public interface DeleteUserUseCase {
    
    /**
     * Elimina un usuario por su ID.
     * 
     * @param userId el ID del usuario a eliminar
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     */
    void deleteById(Long userId);
    
    /**
     * Elimina un usuario por su email.
     * 
     * @param email el email del usuario a eliminar
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     */
    void deleteByEmail(String email);
}



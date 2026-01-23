package com.apis.fintrack.domain.user.port.input;

import com.apis.fintrack.domain.user.model.RoleType;
import com.apis.fintrack.domain.user.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Puerto de entrada para el caso de uso de actualizaciÃ³n de usuario.
 * 
 * Define el contrato que debe implementar la capa de aplicaciÃ³n
 * para actualizar datos de usuarios existentes.
 */
public interface UpdateUserUseCase {
    
    /**
     * Actualiza el nombre de un usuario.
     * 
     * @param userId el ID del usuario
     * @param newName el nuevo nombre
     * @return el usuario actualizado
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     */
    User updateName(Long userId, String newName);
    
    /**
     * Actualiza el apellido de un usuario.
     * 
     * @param userId el ID del usuario
     * @param newSurname el nuevo apellido
     * @return el usuario actualizado
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     */
    User updateSurname(Long userId, String newSurname);
    
    /**
     * Actualiza el email de un usuario.
     * 
     * @param userId el ID del usuario
     * @param newEmail el nuevo email
     * @return el usuario actualizado
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     * @throws com.apis.fintrack.Domain.user.exception.EmailAlreadyExistsException si el email ya estÃ¡ en uso
     */
    User updateEmail(Long userId, String newEmail);
    
    /**
     * Actualiza la contraseÃ±a de un usuario.
     * 
     * @param userId el ID del usuario
     * @param newPassword la nueva contraseÃ±a (sin hashear)
     * @return el usuario actualizado
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     */
    User updatePassword(Long userId, String newPassword);
    
    /**
     * Actualiza la fecha de nacimiento de un usuario.
     * 
     * @param userId el ID del usuario
     * @param newBirthDate la nueva fecha de nacimiento
     * @return el usuario actualizado
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     */
    User updateBirthDate(Long userId, LocalDate newBirthDate);
    
    /**
     * Actualiza el rol de un usuario.
     * 
     * @param userId el ID del usuario
     * @param newRole el nuevo rol
     * @return el usuario actualizado
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     */
    User updateRole(Long userId, RoleType newRole);
    
    /**
     * AÃ±ade fondos a la cuenta de un usuario.
     * 
     * @param userId el ID del usuario
     * @param amount la cantidad a aÃ±adir
     * @return el usuario actualizado
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    User addFunds(Long userId, BigDecimal amount);
    
    /**
     * Retira fondos de la cuenta de un usuario.
     * 
     * @param userId el ID del usuario
     * @param amount la cantidad a retirar
     * @return el usuario actualizado
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     * @throws IllegalArgumentException si la cantidad es negativa o excede los fondos disponibles
     */
    User withdrawFunds(Long userId, BigDecimal amount);
}



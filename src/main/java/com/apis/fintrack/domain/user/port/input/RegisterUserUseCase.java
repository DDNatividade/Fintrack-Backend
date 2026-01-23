package com.apis.fintrack.domain.user.port.input;

import com.apis.fintrack.domain.user.model.User;

import java.time.LocalDate;

/**
 * Puerto de entrada para el caso de uso de registro de usuario.
 * 
 * Define el contrato que debe implementar la capa de aplicaciÃ³n
 * para registrar nuevos usuarios en el sistema.
 */
public interface RegisterUserUseCase {
    
    /**
     * Comando que encapsula los datos necesarios para registrar un usuario.
     */
    record RegisterUserCommand(
        String name,
        String surname,
        String email,
        String password,
        LocalDate birthDate
    ) {}
    
    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param command los datos del usuario a registrar
     * @return el usuario registrado con su ID asignado
     * @throws com.apis.fintrack.Domain.user.exception.EmailAlreadyExistsException si el email ya estÃ¡ registrado
     * @throws IllegalArgumentException si los datos no son vÃ¡lidos
     */
    User execute(RegisterUserCommand command);
}



package com.apis.fintrack.domain.user.port.input;

import com.apis.fintrack.domain.user.model.RoleType;
import com.apis.fintrack.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * Puerto de entrada para el caso de uso de bÃºsqueda de usuarios.
 * 
 * Define el contrato que debe implementar la capa de aplicaciÃ³n
 * para buscar usuarios en el sistema.
 */
public interface FindUserUseCase {
    
    /**
     * Busca un usuario por su ID.
     * 
     * @param userId el ID del usuario
     * @return el usuario encontrado
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     */
    User findById(Long userId);
    
    /**
     * Busca un usuario por su email.
     * 
     * @param email el email del usuario
     * @return el usuario encontrado
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     */
    User findByEmail(String email);
    
    /**
     * Busca un usuario por nombre y apellido.
     * 
     * @param name el nombre del usuario
     * @param surname el apellido del usuario
     * @return el usuario encontrado
     * @throws com.apis.fintrack.Domain.user.exception.UserNotFoundException si no existe
     */
    User findByNameAndSurname(String name, String surname);
    
    /**
     * Busca usuarios por rol con paginaciÃ³n.
     *
     * @param role el rol a buscar
     * @param pageable objeto Pageable con la informaciÃ³n de pÃ¡gina y tamaÃ±o
     * @return lista de usuarios con ese rol
     */
    Page<User> findByRole(RoleType role, Pageable pageable);

    /**
     * Busca usuarios nacidos entre dos fechas con paginaciÃ³n.
     *
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @param pageable objeto Pageable con la informaciÃ³n de pÃ¡gina y tamaÃ±o
     * @return lista de usuarios en ese rango de fechas
     */
    Page<User> findByBirthDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Obtiene todos los usuarios con paginaciÃ³n.
     *
     * @param pageable objeto Pageable con la informaciÃ³n de pÃ¡gina y tamaÃ±o
     * @return lista de usuarios
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Verifica si existe un usuario con el email dado.
     * 
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
}

package com.apis.fintrack.domain.user.port.output;

import com.apis.fintrack.domain.user.model.RoleType;
import com.apis.fintrack.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia de usuarios.
 * 
 * Define el contrato que debe implementar el adaptador de persistencia
 * (Infrastructure layer) para almacenar y recuperar usuarios.
 * 
 * NOTA: Esta interfaz NO tiene dependencias de Spring Data ni JPA.
 * Es una abstracciÃ³n pura del dominio.
 */
public interface UserRepositoryPort {
    
    // ==================== OPERACIONES DE LECTURA ====================
    
    /**
     * Busca un usuario por su ID.
     * 
     * @param id el ID del usuario
     * @return Optional con el usuario si existe
     */
    Optional<User> findById(Long id);
    
    /**
     * Busca un usuario por su email.
     * 
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Busca un usuario por nombre y apellido.
     * 
     * @param name el nombre
     * @param surname el apellido
     * @return Optional con el usuario si existe
     */
    Optional<User> findByNameAndSurname(String name, String surname);
    
    /**
     * Busca usuarios por rol con paginaciÃ³n.
     * 
     * @param role el rol a buscar
     * @param page nÃºmero de pÃ¡gina (0-indexed)
     * @return lista de usuarios con ese rol
     */
    Page<User> findByRole(RoleType role, Pageable page);
    
    /**
     * Busca usuarios nacidos entre dos fechas con paginaciÃ³n.
     * 
     * @param startDate fecha de inicio (inclusive)
     * @param endDate fecha de fin (inclusive)
     * @param page nÃºmero de pÃ¡gina (0-indexed)
     * @return lista de usuarios en ese rango
     */
    Page<User> findByBirthDateBetween(LocalDate startDate, LocalDate endDate, Pageable page);
    
    /**
     * Obtiene todos los usuarios con paginaciÃ³n.
     * 
     * @param page nÃºmero de pÃ¡gina (0-indexed)
     * @return lista de usuarios
     */
    Page<User> findAll(Pageable page);
    
    /**
     * Verifica si existe un usuario con el email dado.
     * 
     * @param email el email a verificar
     * @return true si existe
     */
    boolean existsByEmail(String email);
    
    /**
     * Verifica si existe un usuario con el ID dado.
     * 
     * @param id el ID a verificar
     * @return true si existe
     */
    boolean existsById(Long id);
    
    /**
     * Cuenta el total de usuarios.
     * 
     * @return el nÃºmero total de usuarios
     */
    long count();
    
    // ==================== OPERACIONES DE ESCRITURA ====================
    
    /**
     * Guarda un usuario (nuevo o existente).
     * 
     * Si el usuario no tiene ID, se crea uno nuevo.
     * Si tiene ID, se actualiza el existente.
     * 
     * @param user el usuario a guardar
     * @return el usuario guardado con su ID asignado
     */
    User save(User user);
    
    /**
     * Elimina un usuario por su ID.
     * 
     * @param id el ID del usuario a eliminar
     */
    void deleteById(Long id);
    
    /**
     * Elimina un usuario por su email.
     * 
     * @param email el email del usuario a eliminar
     */
    void deleteByEmail(String email);
}



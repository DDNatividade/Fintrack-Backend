package com.apis.fintrack.domain.subscription.port.output;

import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Puerto de salida (Outbound Port) para persistencia de suscripciones.
 *
 * Parte del dominio puro, sin dependencias de frameworks.
 */
public interface SubscriptionRepositoryPort {

    /**
     * Guarda o actualiza una suscripción.
     *
     * @param subscription la suscripción a guardar
     * @return la suscripción guardada con ID asignado
     */
    Subscription save(Subscription subscription);

    /**
     * Busca la suscripción asociada a un cliente (usuario).
     *
     * @param customer el usuario cliente
     * @return Optional con la suscripción si existe
     */
    Optional<Subscription> findByCustomer(User customer);

    /**
     * Busca una suscripción por su id primario.
     *
     * @param subscriptionId id de la suscripción
     * @return Optional con la suscripción si existe
     */
    Optional<Subscription> findById(Long subscriptionId);

    /**
     * Obtiene todas las suscripciones paginadas.
     *
     * @param pageable objeto Pageable para paginación
     * @return página de suscripciones
     */
    Page<Subscription> findAll(Pageable pageable);

    /**
     * Obtiene las suscripciones activas paginadas.
     *
     * @param pageable objeto Pageable para paginación
     * @return página de suscripciones activas
     */
    Page<Subscription> findActiveSubscriptions(Pageable pageable);

    /**
     * Cuenta el total de suscripciones activas.
     *
     * @return número total de suscripciones activas
     */
    long countActiveSubscriptions();

    /**
     * Elimina una suscripción por su ID.
     *
     * @param subscriptionId ID de la suscripción a eliminar
     */
    void deleteById(Long subscriptionId);

    /**
     * Elimina todas las suscripciones de un usuario.
     *
     * @param userId ID del usuario
     */
    void deleteAllByUserId(Long userId);

    /**
     * Verifica si existe una suscripción por ID.
     *
     * @param subscriptionId ID a verificar
     * @return true si existe
     */
    boolean existsById(Long subscriptionId);

}

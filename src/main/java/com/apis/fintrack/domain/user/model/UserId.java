package com.apis.fintrack.domain.user.model;

import lombok.Getter;

import java.util.Objects;

/**
 * Value Object que representa el identificador único de un usuario.
 *
 * Características:
 * - Inmutable
 * - Validación en construcción
 * - Igualdad por valor
 */
@Getter
public final class UserId {
    
    private final Long value;
    
    private UserId(Long value) {
        this.value = value;
    }

    // Private no-arg constructor used for empty() instances
    private UserId() {
        this.value = null;
    }

    /**
     * Crea un UserId a partir de un valor Long.
     * 
     * @param value el valor del identificador
     * @return una nueva instancia de UserId
     * @throws IllegalArgumentException si el valor es nulo o negativo
     */
    public static UserId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("UserId must be a positive number");
        }
        return new UserId(value);
    }
    
    /**
     * Crea un UserId sin validación (para nuevos usuarios sin ID asignado).
     *
     * @return un UserId vacío representado como null internamente
     */
    public static UserId empty() {
        return new UserId();
    }

    public boolean isEmpty() {
        return value == null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value != null ? value.toString() : "empty";
    }
}

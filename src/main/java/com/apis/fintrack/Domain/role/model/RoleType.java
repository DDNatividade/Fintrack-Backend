package com.apis.fintrack.domain.user.model.role.model;

import lombok.Getter;

/**
 * Enum que representa los tipos de rol en el sistema.
 * 
 * Roles disponibles:
 * - USER: Usuario estándar con permisos básicos
 * - ADMIN: Administrador con todos los permisos
 * - SUBSCRIBER: Usuario con suscripción activa
 */
@Getter
public enum RoleType {
    USER("Usuario estándar"),
    ADMIN("Administrador"),
    SUBSCRIBER("Suscriptor");
    
    private final String description;
    
    RoleType(String description) {
        this.description = description;
    }

    /**
     * Verifica si este rol tiene privilegios de administrador.
     * 
     * @return true si es ADMIN
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }
    
    /**
     * Verifica si este rol tiene suscripción activa.
     * 
     * @return true si es SUBSCRIBER o ADMIN
     */
    public boolean hasSubscription() {
        return this == SUBSCRIBER || this == ADMIN;
    }


}



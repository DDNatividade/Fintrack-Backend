package com.apis.fintrack.domain.property.model;

import lombok.Getter;

import java.util.Locale;
import java.util.Set;

@Getter
public enum PropertyEnum {
    READ,
    WRITE,
    DELETE,
    UPDATE,
    CREATE;

    /**
     * Convierte un string a PropertyEnum, ignorando mayúsculas/minúsculas.
     * @param propertyName nombre de la propiedad
     * @return PropertyEnum correspondiente
     * @throws IllegalArgumentException si no existe
     */
    public static PropertyEnum fromString(String propertyName) {
        for (PropertyEnum property : values()) {
            if (property.name().equalsIgnoreCase(propertyName)) {
                return property;
            }
        }
        throw new IllegalArgumentException("Propiedad no encontrada: " + propertyName);
    }

    /**
     * Verifica si un conjunto de permisos contiene este permiso.
     * Útil para métodos como canBeManagedByRoles.
     */
    public boolean isInSet(Set<PropertyEnum> permissions) {
        return permissions != null && permissions.contains(this);
    }
}

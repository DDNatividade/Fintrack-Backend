package com.apis.fintrack.infrastructure.security.model;

import java.util.Set;

/**
 * Enum representing permission types for Spring Security authorization.
 *
 * <p>This enum is used by JPA entities and Spring Security configuration.
 * It is NOT a domain concept but an infrastructure concern for access control.</p>
 */
public enum PropertyEnum {
    READ,
    WRITE,
    DELETE,
    UPDATE,
    CREATE;

    /**
     * Converts a string to PropertyEnum, ignoring case.
     *
     * @param propertyName the property name
     * @return the corresponding PropertyEnum
     * @throws IllegalArgumentException if not found
     */
    public static PropertyEnum fromString(String propertyName) {
        for (PropertyEnum property : values()) {
            if (property.name().equalsIgnoreCase(propertyName)) {
                return property;
            }
        }
        throw new IllegalArgumentException("Property not found: " + propertyName);
    }

    /**
     * Checks if a set of permissions contains this permission.
     *
     * @param permissions the set of permissions to check
     * @return true if this permission is in the set
     */
    public boolean isInSet(Set<PropertyEnum> permissions) {
        return permissions != null && permissions.contains(this);
    }
}


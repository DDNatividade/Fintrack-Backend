package com.apis.fintrack.domain.user.model;

import lombok.Getter;

/**
 * Enum representing user role types in the system.
 *
 * <p>This enum is part of the User aggregate and defines
 * the authorization level of each user.</p>
 *
 * Available roles:
 * - USER: Standard user with basic permissions
 * - ADMIN: Administrator with full permissions
 * - SUBSCRIBER: User with active subscription
 */
@Getter
public enum RoleType {
    USER("Standard user"),
    ADMIN("Administrator"),
    SUBSCRIBER("Subscriber");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }

    /**
     * Checks if this role has administrator privileges.
     *
     * @return true if ADMIN
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * Checks if this role has an active subscription.
     *
     * @return true if SUBSCRIBER or ADMIN
     */
    public boolean hasSubscription() {
        return this == SUBSCRIBER || this == ADMIN;
    }
}


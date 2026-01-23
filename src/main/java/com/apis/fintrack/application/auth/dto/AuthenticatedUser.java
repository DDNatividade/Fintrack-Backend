package com.apis.fintrack.application.auth.dto;

import lombok.Getter;
import java.util.Set;
import java.util.UUID;

/**
 * Minimal DTO returned by authentication adapters to the application layer.
 */
@Getter
public final class AuthenticatedUser {
    private final UUID userId;
    private final String username;
    private final Set<String> roles;

    public AuthenticatedUser(UUID userId, String username, Set<String> roles) {
        this.userId = userId;
        this.username = username;
        this.roles = roles == null ? Set.of() : Set.copyOf(roles);
    }

}


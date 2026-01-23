package com.apis.fintrack.application.auth.port.output;

import com.apis.fintrack.application.auth.dto.AuthenticatedUser;

import java.util.Optional;

/**
 * Application-level port for authentication operations.
 * Implementations live in the infrastructure layer and adapt framework-specific
 * mechanisms (e.g. Spring Security) to this minimal contract.
 */
public interface AuthenticationPort {

    /**
     * Authenticate credentials and return an AuthenticatedUser when successful.
     * Implementations should NOT perform domain logic; they only translate
     * security provider concepts into a minimal application DTO.
     */
    Optional<AuthenticatedUser> authenticate(String username, String rawPassword);

    /**
     * Lookup a user by username without validating credentials.
     */
    Optional<AuthenticatedUser> findByUsername(String username);

    /**
     * Invalidate a token or perform logout actions if supported by the provider.
     */
    void logout(String token);
}


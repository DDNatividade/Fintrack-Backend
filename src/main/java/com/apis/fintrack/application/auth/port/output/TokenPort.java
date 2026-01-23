package com.apis.fintrack.application.auth.port.output;

import com.apis.fintrack.application.auth.dto.AuthenticatedUser;

import java.util.Optional;

/**
 * Port for token operations. Implementations are responsible for generating,
 * validating and parsing tokens. Kept minimal and provider-agnostic.
 */
public interface TokenPort {

    /**
     * Generate a token for the given authenticated user information.
     */
    String generateToken(AuthenticatedUser user);

    /**
     * Validate token and return username or other identity if valid.
     */
    Optional<String> validateAndExtractSubject(String token);

    /**
     * Check if the token is valid for the given username.
     */
    boolean isTokenValid(String token, String username);
}


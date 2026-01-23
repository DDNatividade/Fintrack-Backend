package com.apis.fintrack.application.auth;

import com.apis.fintrack.application.auth.port.input.AuthenticateUserUseCase;
import com.apis.fintrack.application.auth.port.input.RefreshTokenUseCase;

/**
 * Helper class for building authentication-related results.
 *
 * Centralizes token metadata configuration to avoid duplication
 * across authentication use cases.
 */
public final class AuthResultBuilder {

    // Token expiration time in milliseconds (15 minutes)
    private static final long TOKEN_EXPIRATION_MS = 3600000L / 4L;
    private static final String TOKEN_TYPE = "Bearer";

    private AuthResultBuilder() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Builds an authentication result with standard token metadata.
     *
     * @param accessToken the generated access token
     * @return authentication result with token type and expiration
     */
    public static AuthenticateUserUseCase.AuthenticationResult buildAuthenticationResult(String accessToken) {
        return new AuthenticateUserUseCase.AuthenticationResult(
                accessToken,
                TOKEN_TYPE,
                TOKEN_EXPIRATION_MS / 1000 // Convert to seconds
        );
    }

    /**
     * Builds a refresh token result with standard token metadata.
     *
     * @param accessToken the new generated access token
     * @return refresh token result with token type and expiration
     */
   public static RefreshTokenUseCase.RefreshTokenResult buildRefreshTokenResult(String accessToken) {
        return new RefreshTokenUseCase.RefreshTokenResult(
                accessToken,
                TOKEN_TYPE,
                TOKEN_EXPIRATION_MS / 1000 // Convert to seconds
        );
    }

    /**
     * Gets the token expiration time in seconds.
     * Useful for testing or external configuration.
     */
    static long getTokenExpirationSeconds() {
        return TOKEN_EXPIRATION_MS / 1000;
    }
}


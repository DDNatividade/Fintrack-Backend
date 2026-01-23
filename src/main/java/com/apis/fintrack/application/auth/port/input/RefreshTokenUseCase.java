package com.apis.fintrack.application.auth.port.input;

/**
 * Input port for refreshing access tokens.
 *
 * Allows users to obtain a new access token using a valid refresh token
 * without requiring re-authentication.
 */
public interface RefreshTokenUseCase {

    /**
     * Command that encapsulates the refresh token request.
     */
    record RefreshTokenCommand(
        String refreshToken
    ) {
        public RefreshTokenCommand {
            if (refreshToken == null || refreshToken.isBlank()) {
                throw new IllegalArgumentException("Refresh token cannot be null or empty");
            }
        }
    }

    /**
     * Result containing the new access token.
     */
    record RefreshTokenResult(
        String accessToken,
        String tokenType,
        long expiresIn
    ) {
        public RefreshTokenResult {
            if (accessToken == null || accessToken.isBlank()) {
                throw new IllegalArgumentException("Access token cannot be null or empty");
            }
            if (tokenType == null || tokenType.isBlank()) {
                throw new IllegalArgumentException("Token type cannot be null or empty");
            }
            if (expiresIn <= 0) {
                throw new IllegalArgumentException("Expires in must be positive");
            }
        }
    }

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param command the refresh token command
     * @return the refresh token result with the new access token
     * @throws IllegalArgumentException if the refresh token is invalid
     * @throws com.apis.fintrack.application.auth.exception.AuthenticationFailedException if the refresh token is expired or invalid
     */
    RefreshTokenResult refresh(RefreshTokenCommand command);
}


package com.apis.fintrack.application.auth.port.input;

/**
 * Input port for user authentication use case.
 *
 * Defines the contract that must be implemented by the application layer
 * to authenticate users in the system.
 */
public interface AuthenticateUserUseCase {

    /**
     * Command that encapsulates authentication data. @Todo refactor validation
     */
    record AuthenticationCommand(
        String username,
        String password
    ) {
        public AuthenticationCommand {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
        }
    }

    /**
     * Result of successful authentication containing the access token. @Todo refactor validation
     */
    record AuthenticationResult(
        String accessToken,
        String tokenType,
        long expiresIn
    ) {
        public AuthenticationResult {
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
     * Authenticates a user with the provided credentials.
     *
     * @param command the authentication command containing username and password
     * @return the authentication result with the generated token
     * @throws IllegalArgumentException if credentials are invalid
     * @throws com.apis.fintrack.application.auth.exception.AuthenticationFailedException if authentication fails
     */
    AuthenticationResult authenticate(AuthenticationCommand command);
}


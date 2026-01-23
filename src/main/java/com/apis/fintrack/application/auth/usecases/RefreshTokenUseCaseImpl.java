package com.apis.fintrack.application.auth.usecases;

import com.apis.fintrack.application.auth.AuthResultBuilder;
import com.apis.fintrack.application.auth.dto.AuthenticatedUser;
import com.apis.fintrack.application.auth.exception.AuthenticationFailedException;
import com.apis.fintrack.application.auth.port.input.RefreshTokenUseCase;
import com.apis.fintrack.application.auth.port.output.AuthenticationPort;
import com.apis.fintrack.application.auth.port.output.TokenPort;
import org.springframework.stereotype.Service;

/**
 * Implementation of the refresh token use case.
 *
 * Orchestrates the token refresh process by:
 * 1. Validating the refresh token via TokenPort
 * 2. Extracting user information from the token
 * 3. Loading authenticated user via AuthenticationPort
 * 4. Generating a new access token via TokenPort
 * 5. Returning the new token result
 *
 * This class belongs to the application layer and coordinates
 * infrastructure adapters without containing business rules.
 */
@Service
public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

    private final TokenPort tokenPort;
    private final AuthenticationPort authenticationPort;

    public RefreshTokenUseCaseImpl(
            TokenPort tokenPort,
            AuthenticationPort authenticationPort
    ) {
        this.tokenPort = tokenPort;
        this.authenticationPort = authenticationPort;
    }

    @Override
    public RefreshTokenResult refresh(RefreshTokenCommand command) {
        validateCommand(command);

        String username = extractUsernameFromToken(command.refreshToken());
        AuthenticatedUser authenticatedUser = loadAuthenticatedUser(username);
        String newAccessToken = generateNewAccessToken(authenticatedUser);

        return AuthResultBuilder.buildRefreshTokenResult(newAccessToken);
    }

    /**
     * Validates the refresh token command.
     */
    private void validateCommand(RefreshTokenCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Refresh token command cannot be null");
        }
    }

    /**
     * Extracts the username from the refresh token.
     *
     * @throws AuthenticationFailedException if token is invalid or expired
     */
    private String extractUsernameFromToken(String refreshToken) {
        return tokenPort
                .validateAndExtractSubject(refreshToken)
                .orElseThrow(() -> new AuthenticationFailedException("Invalid or expired refresh token"));
    }

    /**
     * Loads the authenticated user by username.
     *
     * @throws AuthenticationFailedException if user is not found
     */
    private AuthenticatedUser loadAuthenticatedUser(String username) {
        return authenticationPort
                .findByUsername(username)
                .orElseThrow(() -> AuthenticationFailedException.userNotFound(username));
    }

    /**
     * Generates a new access token for the authenticated user.
     */
    private String generateNewAccessToken(AuthenticatedUser user) {
        return tokenPort.generateToken(user);
    }
}


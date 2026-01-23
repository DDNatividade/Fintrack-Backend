package com.apis.fintrack.application.auth.usecases;

import com.apis.fintrack.application.auth.AuthResultBuilder;
import com.apis.fintrack.application.auth.exception.AuthenticationFailedException;
import com.apis.fintrack.application.auth.port.input.AuthenticateUserUseCase;
import com.apis.fintrack.application.auth.dto.AuthenticatedUser;
import com.apis.fintrack.application.auth.port.output.AuthenticationPort;
import com.apis.fintrack.application.auth.port.output.TokenPort;
import org.springframework.stereotype.Service;

/**
 * Implementation of the authenticate user use case.
 *
 * Orchestrates the authentication process by:
 * 1. Validating credentials via AuthenticationPort
 * 2. Generating JWT token via TokenPort
 * 3. Returning authentication result with token
 *
 * This class belongs to the application layer and coordinates
 * infrastructure adapters without containing business rules.
 */
@Service
public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {

    private final AuthenticationPort authenticationPort;
    private final TokenPort tokenPort;

    public AuthenticateUserUseCaseImpl(
            AuthenticationPort authenticationPort,
            TokenPort tokenPort
    ) {
        this.authenticationPort = authenticationPort;
        this.tokenPort = tokenPort;
    }

    @Override
    public AuthenticationResult authenticate(AuthenticationCommand command) {
        validateCommand(command);

        AuthenticatedUser authenticatedUser = performAuthentication(command);
        String accessToken = generateAccessToken(authenticatedUser);

        return AuthResultBuilder.buildAuthenticationResult(accessToken);
    }

    /**
     * Validates the authentication command.
     * The command record already validates null/empty, but we add explicit validation
     * for clarity and to handle any edge cases.
     */
    private void validateCommand(AuthenticationCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Authentication command cannot be null");
        }
    }

    /**
     * Performs the actual authentication using the authentication port.
     *
     * @throws AuthenticationFailedException if credentials are invalid
     */
    private AuthenticatedUser performAuthentication(AuthenticationCommand command) {
        return authenticationPort
                .authenticate(command.username(), command.password())
                .orElseThrow(AuthenticationFailedException::invalidCredentials);
    }

    /**
     * Generates an access token for the authenticated user.
     */
    private String generateAccessToken(AuthenticatedUser user) {
        return tokenPort.generateToken(user);
    }
}


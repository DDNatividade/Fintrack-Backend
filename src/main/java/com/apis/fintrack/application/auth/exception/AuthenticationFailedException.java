package com.apis.fintrack.application.auth.exception;

/**
 * Exception thrown when authentication fails due to invalid credentials
 * or other authentication-related errors.
 */
public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException(String message) {
        super(message);
    }

    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Factory method for invalid credentials error.
     */
    public static AuthenticationFailedException invalidCredentials() {
        return new AuthenticationFailedException("Invalid username or password");
    }

    /**
     * Factory method for user not found error.
     */
    public static AuthenticationFailedException userNotFound(String username) {
        return new AuthenticationFailedException("User not found: " + username);
    }
}


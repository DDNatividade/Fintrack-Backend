package com.apis.fintrack.domain.user.exception;

/**
 * Exception thrown when attempting to register a user with an email
 * that is already in use by another user.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    
    public EmailAlreadyExistsException(String email) {
        super(email);
    }
    
    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}



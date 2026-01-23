package com.apis.fintrack.domain.user.exception;

public class RolesNotFoundException extends RuntimeException {
    public RolesNotFoundException(String message) {
        super(message);
    }
}


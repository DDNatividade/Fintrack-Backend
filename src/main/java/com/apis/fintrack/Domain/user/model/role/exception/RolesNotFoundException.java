package com.apis.fintrack.domain.user.model.role.exception;

public class RolesNotFoundException extends RuntimeException {
    public RolesNotFoundException(String message) {
        super(message);
    }
}


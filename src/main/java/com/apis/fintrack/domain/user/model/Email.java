package com.apis.fintrack.domain.user.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object que representa un email vÃ¡lido.
 * 
 * CaracterÃ­sticas:
 * - Inmutable
 * - ValidaciÃ³n de formato en construcciÃ³n
 * - Igualdad por valor (case-insensitive)
 */
public final class Email {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private final String value;
    
    private Email(String value) {
        this.value = value.toLowerCase().trim();
    }
    
    /**
     * Crea un Email a partir de un String.
     * 
     * @param value el valor del email
     * @return una nueva instancia de Email
     * @throws IllegalArgumentException si el formato es invÃ¡lido
     */
    public static Email of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        String trimmed = value.trim();
        
        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
        
        return new Email(trimmed);
    }
    
    public String getValue() {
        return value;
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}



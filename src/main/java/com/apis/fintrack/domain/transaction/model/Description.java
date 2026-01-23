package com.apis.fintrack.domain.transaction.model;

import java.util.Objects;

/**
 * Value Object que representa la descripciÃ³n de una transacciÃ³n.
 * 
 * CaracterÃ­sticas:
 * - Inmutable
 * - Longitud entre 1 y 100 caracteres
 * - No puede estar vacÃ­a o ser solo espacios
 */
public final class Description {
    
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;
    
    private final String value;
    
    private Description(String value) {
        this.value = value.trim();
    }
    
    /**
     * Crea una Description a partir de un String.
     * 
     * @param value el texto de la descripciÃ³n
     * @return una nueva instancia de Description
     * @throws IllegalArgumentException si la descripciÃ³n es invÃ¡lida
     */
    public static Description of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripciÃ³n no puede estar vacÃ­a");
        }
        
        String trimmed = value.trim();
        
        if (trimmed.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                "La descripciÃ³n debe tener al menos " + MIN_LENGTH + " carÃ¡cter"
            );
        }
        
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                "La descripciÃ³n no puede exceder " + MAX_LENGTH + " caracteres"
            );
        }
        
        return new Description(trimmed);
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * Obtiene la longitud de la descripciÃ³n.
     * 
     * @return nÃºmero de caracteres
     */
    public int length() {
        return value.length();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Description that = (Description) o;
        return Objects.equals(value, that.value);
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



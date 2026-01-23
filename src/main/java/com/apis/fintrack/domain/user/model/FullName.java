package com.apis.fintrack.domain.user.model;

import lombok.Getter;

import java.util.Objects;

/**
 * Value Object que representa el nombre completo de un usuario.
 * 
 * Características:
 * - Inmutable
 * - Compuesto por nombre y apellido
 * - Validación de campos no vacíos
 * - CapitalizaciÃ³n automÃ¡tica
 */
@Getter
public final class FullName {
    
    private static final int MAX_NAME_LENGTH = 50;
    
    private final String name;
    private final String surname;
    
    private FullName(String name, String surname) {
        this.name = capitalize(name.trim());
        this.surname = capitalize(surname.trim());
    }
    
    /**
     * Crea un FullName a partir de nombre y apellido.
     * 
     * @param name el nombre
     * @param surname el apellido
     * @return una nueva instancia de FullName
     * @throws IllegalArgumentException si algÃºn campo es invÃ¡lido
     */
    public static FullName of(String name, String surname) {
        validateName(name, "nombre");
        validateName(surname, "apellido");
        return new FullName(name, surname);
    }
    
    /**
     * Crea un nuevo FullName cambiando solo el nombre.
     * 
     * @param newName el nuevo nombre
     * @return una nueva instancia de FullName
     */
    public FullName withName(String newName) {
        validateName(newName, "nombre");
        return new FullName(newName, this.surname);
    }
    
    /**
     * Crea un nuevo FullName cambiando solo el apellido.
     * 
     * @param newSurname el nuevo apellido
     * @return una nueva instancia de FullName
     */
    public FullName withSurname(String newSurname) {
        validateName(newSurname, "apellido");
        return new FullName(this.name, newSurname);
    }
    
    private static void validateName(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        
        if (value.trim().length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                fieldName + " cannot exceed " + MAX_NAME_LENGTH + " characters"
            );
        }
        
        // \\p{L} matches any Unicode letter (including accented characters)
        if (!value.trim().matches("^[\\p{L}\\s'-]+$")) {
            throw new IllegalArgumentException(
                fieldName + " contains invalid characters"
            );
        }
    }
    
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    public String getName() {
        return name;
    }
    
    public String getSurname() {
        return surname;
    }
    
    /**
     * Obtiene el nombre completo formateado.
     * 
     * @return "Nombre Apellido"
     */
    public String getFullName() {
        return name + " " + surname;
    }
    
    /**
     * Obtiene las iniciales.
     * 
     * @return "NA" (primera letra de nombre y apellido)
     */
    public String getInitials() {
        return String.valueOf(name.charAt(0)) + surname.charAt(0);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullName fullName = (FullName) o;
        return Objects.equals(name, fullName.name) && 
               Objects.equals(surname, fullName.surname);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
    }
    
    @Override
    public String toString() {
        return getFullName();
    }
}



package com.apis.fintrack.domain.user.model;

import java.util.Objects;

/**
 * Value Object que representa una contraseña.
 *
 * Responsabilidades:
 * - Validar requisitos de formato (longitud, complejidad)
 * - Encapsular el valor de forma segura
 *
 * NO es responsable de:
 * - Hashear la contraseña (eso lo hace PasswordEncoder en infraestructura)
 *
 * Características:
 * - Inmutable
 * - No expone el valor en toString() por seguridad
 */
public final class Password {

    private static final int MIN_LENGTH = 8;

    private final String value;

    private Password(String value) {
        this.value = value;
    }

    /**
     * Crea una Password validando los requisitos de seguridad.
     * Usar para contraseñas nuevas (registro, cambio de contraseña).
     *
     * @param plainText la contraseña en texto plano
     * @return una nueva instancia de Password
     * @throws IllegalArgumentException si no cumple los requisitos
     */
    public static Password of(String plainText) {
        validate(plainText);
        return new Password(plainText);
    }

    /**
     * Crea una Password sin validación.
     * Usar SOLO para reconstruir desde BD (el valor ya fue validado al crearse).
     *
     * @param storedValue el valor almacenado (hasheado)
     * @return una nueva instancia de Password
     */
    public static Password fromStorage(String storedValue) {
        if (storedValue == null || storedValue.isBlank()) {
            throw new IllegalArgumentException("Stored password cannot be empty");
        }
        return new Password(storedValue);
    }

    /**
     * Valida que la contraseña cumpla los requisitos de seguridad.
     */
    private static void validate(String plainText) {
        if (plainText == null || plainText.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if (plainText.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                "La contraseña debe tener al menos " + MIN_LENGTH + " caracteres"
            );
        }

        if (!containsUppercase(plainText)) {
            throw new IllegalArgumentException(
                "La contraseña debe contener al menos una letra mayúscula"
            );
        }

        if (!containsLowercase(plainText)) {
            throw new IllegalArgumentException(
                "La contraseña debe contener al menos una letra minúscula"
            );
        }

        if (!containsDigit(plainText)) {
            throw new IllegalArgumentException(
                "Password must contain at least one digit"
            );
        }
    }

    public String getValue() {
        return value;
    }

    private static boolean containsUppercase(String str) {
        return str.chars().anyMatch(Character::isUpperCase);
    }

    private static boolean containsLowercase(String str) {
        return str.chars().anyMatch(Character::isLowerCase);
    }

    private static boolean containsDigit(String str) {
        return str.chars().anyMatch(Character::isDigit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * No expone el valor de la contraseña por seguridad.
     */
    @Override
    public String toString() {
        return "[PROTECTED]";
    }
}

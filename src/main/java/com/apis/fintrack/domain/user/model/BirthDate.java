package com.apis.fintrack.domain.user.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Value Object que representa la fecha de nacimiento de un usuario.
 * 
 * CaracterÃ­sticas:
 * - Inmutable
 * - ValidaciÃ³n de fecha vÃ¡lida (no futura, edad mÃ­nima)
 * - CÃ¡lculo de edad
 */
public final class BirthDate {
    
    private static final int MINIMUM_AGE = 18;
    private static final int MAXIMUM_AGE = 120;
    
    private final LocalDate value;
    
    private BirthDate(LocalDate value) {
        this.value = value;
    }
    
    /**
     * Crea un BirthDate a partir de un LocalDate.
     * 
     * @param date la fecha de nacimiento
     * @return una nueva instancia de BirthDate
     * @throws IllegalArgumentException si la fecha es invÃ¡lida
     */
    public static BirthDate of(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
        
        int age = calculateAge(date);
        
        if (age < MINIMUM_AGE) {
            throw new IllegalArgumentException(
                "El usuario debe tener al menos " + MINIMUM_AGE + " aÃ±os"
            );
        }
        
        if (age > MAXIMUM_AGE) {
            throw new IllegalArgumentException(
                "La fecha de nacimiento no es vÃ¡lida"
            );
        }
        
        return new BirthDate(date);
    }
    
    /**
     * Crea un BirthDate sin validaciÃ³n de edad mÃ­nima (para migraciÃ³n de datos).
     * 
     * @param date la fecha de nacimiento
     * @return una nueva instancia de BirthDate
     */
    public static BirthDate ofUnchecked(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        }
        return new BirthDate(date);
    }
    
    public LocalDate getValue() {
        return value;
    }
    
    /**
     * Calcula la edad actual del usuario.
     * 
     * @return la edad en aÃ±os
     */
    public int getAge() {
        return calculateAge(value);
    }
    
    /**
     * Verifica si el usuario es mayor de edad.
     * 
     * @return true si tiene 18 aÃ±os o mÃ¡s
     */
    public boolean isAdult() {
        return getAge() >= 18;
    }
    
    /**
     * Verifica si hoy es el cumpleaÃ±os del usuario.
     * 
     * @return true si hoy es su cumpleaÃ±os
     */
    public boolean isBirthdayToday() {
        LocalDate today = LocalDate.now();
        return value.getMonth() == today.getMonth() && 
               value.getDayOfMonth() == today.getDayOfMonth();
    }
    
    private static int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BirthDate birthDate = (BirthDate) o;
        return Objects.equals(value, birthDate.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}



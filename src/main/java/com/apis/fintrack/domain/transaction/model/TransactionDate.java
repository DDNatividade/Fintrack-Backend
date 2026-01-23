package com.apis.fintrack.domain.transaction.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Value Object que representa la fecha de una transacciÃ³n.
 * 
 * CaracterÃ­sticas:
 * - Inmutable
 * - No puede ser futura
 */
public final class TransactionDate {
    
    private final LocalDate value;
    
    private TransactionDate(LocalDate value) {
        this.value = value;
    }
    
    /**
     * Crea un TransactionDate a partir de un LocalDate.
     * 
     * @param date la fecha de la transacciÃ³n
     * @return una nueva instancia de TransactionDate
     * @throws IllegalArgumentException si la fecha es nula o futura
     */
    public static TransactionDate of(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La fecha de transacciÃ³n no puede ser nula");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de transacciÃ³n no puede ser futura");
        }
        return new TransactionDate(date);
    }
    
    /**
     * Crea un TransactionDate con la fecha actual.
     * 
     * @return TransactionDate con la fecha de hoy
     */
    public static TransactionDate now() {
        return new TransactionDate(LocalDate.now());
    }
    
    /**
     * Reconstruye un TransactionDate desde almacenamiento.
     * Permite fechas futuras para datos histÃ³ricos migrados.
     * 
     * @param storedDate la fecha almacenada
     * @return una nueva instancia de TransactionDate
     */
    public static TransactionDate fromStorage(LocalDate storedDate) {
        if (storedDate == null) {
            throw new IllegalArgumentException("La fecha almacenada no puede ser nula");
        }
        return new TransactionDate(storedDate);
    }
    
    public LocalDate getValue() {
        return value;
    }
    
    /**
     * Verifica si la transacciÃ³n es de hoy.
     */
    public boolean isToday() {
        return value.equals(LocalDate.now());
    }
    
    /**
     * Verifica si la transacciÃ³n es de este mes.
     */
    public boolean isThisMonth() {
        LocalDate now = LocalDate.now();
        return value.getYear() == now.getYear() && 
               value.getMonth() == now.getMonth();
    }
    
    /**
     * Verifica si la transacciÃ³n es de este aÃ±o.
     */
    public boolean isThisYear() {
        return value.getYear() == LocalDate.now().getYear();
    }
    
    /**
     * Verifica si la fecha estÃ¡ entre dos fechas (inclusivo).
     */
    public boolean isBetween(LocalDate start, LocalDate end) {
        return !value.isBefore(start) && !value.isAfter(end);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDate that = (TransactionDate) o;
        return Objects.equals(value, that.value);
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



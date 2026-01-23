package com.apis.fintrack.domain.transaction.model;

import java.util.Objects;

/**
 * Value Object que representa el identificador Ãºnico de una transacciÃ³n.
 * 
 * CaracterÃ­sticas:
 * - Inmutable
 * - Puede estar vacÃ­o para transacciones nuevas (aÃºn no persistidas)
 */
public final class TransactionId {
    
    private final Long value;
    
    private TransactionId(Long value) {
        this.value = value;
    }
    
    /**
     * Crea un TransactionId a partir de un valor Long.
     * 
     * @param value el valor del ID
     * @return una nueva instancia de TransactionId
     * @throws IllegalArgumentException si el valor es negativo
     */
    public static TransactionId of(Long value) {
        if (value != null && value < 0) {
            throw new IllegalArgumentException("El ID de transacciÃ³n no puede ser negativo");
        }
        return new TransactionId(value);
    }
    
    /**
     * Crea un TransactionId vacÃ­o para transacciones nuevas.
     * 
     * @return un TransactionId sin valor asignado
     */
    public static TransactionId empty() {
        return new TransactionId(null);
    }
    
    public Long getValue() {
        return value;
    }
    
    /**
     * Verifica si el ID estÃ¡ vacÃ­o (transacciÃ³n no persistida).
     * 
     * @return true si no tiene valor asignado
     */
    public boolean isEmpty() {
        return value == null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionId that = (TransactionId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value != null ? value.toString() : "NEW";
    }
}



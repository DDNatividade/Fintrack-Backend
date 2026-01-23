package com.apis.fintrack.domain.transaction.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
public final class TransactionAmount {
    
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    private final BigDecimal value;
    
    private TransactionAmount(BigDecimal value) {
        this.value = value.setScale(SCALE, ROUNDING_MODE);
    }
    
    /**
     * Crea un TransactionAmount para un INGRESO.
     * El valor se almacena como positivo.
     * 
     * @param amount el monto del ingreso (debe ser positivo)
     * @return una nueva instancia de TransactionAmount
     * @throws IllegalArgumentException si el monto es nulo, cero o negativo
     */
    public static TransactionAmount income(BigDecimal amount) {
        validateNotNullOrZero(amount);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto de un ingreso debe ser positivo");
        }
        return new TransactionAmount(amount);
    }
    
    /**
     * Crea un TransactionAmount para un GASTO.
     * El valor se almacena como negativo.
     * 
     * @param amount el monto del gasto (se pasa como positivo, se almacena negativo)
     * @return una nueva instancia de TransactionAmount con valor negativo
     * @throws IllegalArgumentException si el monto es nulo o cero
     */
    public static TransactionAmount expense(BigDecimal amount) {
        validateNotNullOrZero(amount);
        // Convertir a negativo si viene positivo
        BigDecimal negativeAmount = amount.abs().negate();
        return new TransactionAmount(negativeAmount);
    }
    
    /**
     * Crea un TransactionAmount según el tipo de transacción.
     *
     * @param amount el monto (siempre positivo en entrada)
     * @param type el tipo de transacción
     * @return TransactionAmount con el signo correcto según el tipo
     */
    public static TransactionAmount of(BigDecimal amount, TransactionType type) {
        if (type.isIncome()) {
            return income(amount);
        } else {
            return expense(amount);
        }
    }

    public static TransactionAmount of(BigDecimal amount, boolean isIncome) {
        return isIncome ? income(amount) : expense(amount);
    }
    
    /**
     * Reconstruye un TransactionAmount desde almacenamiento.
     * Usado por Mappers para reconstruir desde la base de datos.
     * 
     * @param storedValue el valor tal como está almacenado (ya con signo)
     * @return una nueva instancia de TransactionAmount
     */
    public static TransactionAmount fromStorage(BigDecimal storedValue) {
        if (storedValue == null) {
            throw new IllegalArgumentException("El monto almacenado no puede ser nulo");
        }
        if (storedValue.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("El monto de una transacción no puede ser cero");
        }
        return new TransactionAmount(storedValue);
    }
    
    private static void validateNotNullOrZero(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("El monto de una transacción no puede ser cero");
        }
    }

    /**
     * Obtiene el valor absoluto del monto.
     * 
     * @return el monto sin signo
     */
    public BigDecimal getAbsoluteValue() {
        return value.abs();
    }
    
    /**
     * Indica si es un ingreso (valor positivo).
     * 
     * @return true si el valor es positivo
     */
    public boolean isIncome() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Indica si es un gasto (valor negativo).
     * 
     * @return true si el valor es negativo
     */
    public boolean isExpense() {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * Obtiene el tipo de transacción basado en el signo.
     *
     * @return INCOME si positivo, EXPENSE si negativo
     */
    public TransactionType getType() {
        return isIncome() ? TransactionType.INCOME : TransactionType.EXPENSE;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionAmount that = (TransactionAmount) o;
        return value.compareTo(that.value) == 0;
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

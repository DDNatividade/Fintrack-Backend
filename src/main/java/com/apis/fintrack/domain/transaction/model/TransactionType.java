package com.apis.fintrack.domain.transaction.model;

/**
 * Enum que representa el tipo de transacciÃ³n.
 * 
 * Tipos:
 * - INCOME: Ingreso (valor positivo)
 * - EXPENSE: Gasto (valor negativo en el registro)
 */
public enum TransactionType {
    INCOME("Ingreso", true),
    EXPENSE("Gasto", false);
    
    private final String description;
    private final boolean positive;
    
    TransactionType(String description, boolean positive) {
        this.description = description;
        this.positive = positive;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Indica si este tipo de transacciÃ³n tiene valor positivo.
     * 
     * @return true si es ingreso (positivo), false si es gasto (negativo)
     */
    public boolean isPositive() {
        return positive;
    }
    
    /**
     * Indica si es un ingreso.
     * 
     * @return true si es INCOME
     */
    public boolean isIncome() {
        return this == INCOME;
    }
    
    /**
     * Indica si es un gasto.
     * 
     * @return true si es EXPENSE
     */
    public boolean isExpense() {
        return this == EXPENSE;
    }
    
    /**
     * Crea un TransactionType a partir de un booleano.
     * 
     * @param isIncome true para ingreso, false para gasto
     * @return INCOME o EXPENSE segÃºn corresponda
     */
    public static TransactionType fromBoolean(boolean isIncome) {
        return isIncome ? INCOME : EXPENSE;
    }
}



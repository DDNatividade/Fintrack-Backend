package com.apis.fintrack.domain.transaction.model;

import com.apis.fintrack.domain.user.model.UserId;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad de dominio Transaction.
 * 
 * Representa una transacción financiera del sistema Fintrack.
 * Esta es una entidad PURA sin dependencias de frameworks (JPA, Spring, etc.)
 * 
 * Reglas de negocio:
 * - El valor NO puede ser cero
 * - Los gastos (EXPENSE) tienen valor NEGATIVO
 * - Los ingresos (INCOME) tienen valor POSITIVO
 * - Solo puede tener UNA categoría
 * 
 * Características:
 * - Usa Value Objects para encapsular validaciones
 * - Inmutable en sus identificadores
 * - MÃ©todos de negocio para operaciones del dominio
 */
@Getter
public class Transaction {
    
    private final TransactionId id;
    private Description description;
    private TransactionAmount amount;
    private TransactionDate transactionDate;
    private Category category;
    private final UserId userId;
    
    /**
     * Constructor completo para uso de Mappers.
     * 
     * @param id identificador de la transacción (puede ser null para nuevas)
     * @param description descripción de la transacción
     * @param amount monto (ya con signo correcto)
     * @param transactionDate fecha de la transacción
     * @param category categoría única
     * @param userId ID del usuario propietario
     */
    public Transaction(TransactionId id, Description description, TransactionAmount amount,
                       TransactionDate transactionDate, Category category, UserId userId) {
        this.id = id;
        this.description = Objects.requireNonNull(description, "La descripción no puede ser nula");
        this.amount = Objects.requireNonNull(amount, "El monto no puede ser nulo");
        this.transactionDate = Objects.requireNonNull(transactionDate, "La fecha no puede ser nula");
        this.category = Objects.requireNonNull(category, "La categoría no puede ser nula");
        this.userId = Objects.requireNonNull(userId, "El ID de usuario no puede ser nulo");
    }
    
    /**
     * Crea una nueva transacciÃ³n de INGRESO.
     * El monto se almacena como positivo.
     * 
     * @param description descripciÃ³n
     * @param amount monto (positivo)
     * @param category categorÃ­a
     * @param userId ID del usuario
     * @return nueva transacciÃ³n de ingreso
     */
    public static Transaction createIncome(String description, BigDecimal amount,
                                           TransactionCategoryEnum category, Long userId) {
        return new Transaction(
            TransactionId.empty(),
            Description.of(description),
            TransactionAmount.income(amount),
            TransactionDate.now(),
            Category.of(category),
            UserId.of(userId)
        );
    }
    
    /**
     * Crea una nueva transacciÃ³n de GASTO.
     * El monto se almacena como negativo.
     * 
     * @param description descripciÃ³n
     * @param amount monto (se pasa positivo, se almacena negativo)
     * @param category categorÃ­a
     * @param userId ID del usuario
     * @return nueva transacciÃ³n de gasto
     */
    public static Transaction createExpense(String description, BigDecimal amount,
                                            TransactionCategoryEnum category, Long userId) {
        return new Transaction(
            TransactionId.empty(),
            Description.of(description),
            TransactionAmount.expense(amount),
            TransactionDate.now(),
            Category.of(category),
            UserId.of(userId)
        );
    }
    
    /**
     * Crea una nueva transacciÃ³n segÃºn el tipo.
     * 
     * @param description descripciÃ³n
     * @param amount monto (siempre positivo en entrada)
     * @param isIncome true para ingreso, false para gasto
     * @param category categorÃ­a
     * @param userId ID del usuario
     * @return nueva transacciÃ³n
     */
    public static Transaction create(String description, BigDecimal amount, boolean isIncome,
                                     TransactionCategoryEnum category, Long userId) {
        TransactionType type = TransactionType.fromBoolean(isIncome);
        return new Transaction(
            TransactionId.empty(),
            Description.of(description),
            TransactionAmount.of(amount, type),
            TransactionDate.now(),
            Category.of(category),
            UserId.of(userId)
        );
    }
    
    /**
     * Indica si es un ingreso.
     */
    public boolean isIncome() {
        return amount.isIncome();
    }
    
    /**
     * Indica si es un gasto.
     */
    public boolean isExpense() {
        return amount.isExpense();
    }
    
    /**
     * Obtiene el tipo de transacciÃ³n.
     */
    public TransactionType getType() {
        return amount.getType();
    }
    
    // ==================== MÃ‰TODOS DE NEGOCIO ====================
    
    /**
     * Cambia la descripciÃ³n de la transacciÃ³n.
     * 
     * @param newDescription la nueva descripciÃ³n
     */
    public void changeDescription(String newDescription) {
        this.description = Description.of(newDescription);
    }
    
    /**
     * Cambia el monto de la transacciÃ³n.
     * Mantiene el tipo (ingreso/gasto) actual.
     * 
     * @param newAmount el nuevo monto (siempre positivo)
     * @throws IllegalArgumentException si el monto es cero
     */
    public void changeAmount(BigDecimal newAmount) {
        TransactionType currentType = this.amount.getType();
        this.amount = TransactionAmount.of(newAmount, currentType);
    }
    
    /**
     * Cambia el tipo de transacciÃ³n (ingreso â†” gasto).
     * Invierte el signo del monto.
     * 
     * @param isIncome true para convertir a ingreso, false para gasto
     */
    public void changeType(boolean isIncome) {
        TransactionType newType = TransactionType.fromBoolean(isIncome);
        if (newType != this.amount.getType()) {
            // Cambiar el signo del monto
            this.amount = TransactionAmount.of(this.amount.getAbsoluteValue(), newType);
        }
    }
    
    /**
     * Cambia la categorÃ­a de la transacciÃ³n.
     * Solo puede tener UNA categorÃ­a.
     * 
     * @param newCategory la nueva categorÃ­a
     */
    public void changeCategory(TransactionCategoryEnum newCategory) {
        this.category = Category.of(newCategory);
    }
    
    /**
     * Cambia la fecha de la transacciÃ³n.
     * 
     * @param newDate la nueva fecha
     */
    public void changeDate(LocalDate newDate) {
        this.transactionDate = TransactionDate.of(newDate);
    }
    
    // ==================== EQUALS, HASHCODE, TOSTRING ====================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        // Dos transacciones son iguales si tienen el mismo ID
        if (id.isEmpty() && that.id.isEmpty()) {
            // Si ambas no tienen ID, comparar por contenido
            return Objects.equals(description, that.description) &&
                   Objects.equals(amount, that.amount) &&
                   Objects.equals(transactionDate, that.transactionDate) &&
                   Objects.equals(userId, that.userId);
        }
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return id.isEmpty() 
            ? Objects.hash(description, amount, transactionDate, userId) 
            : Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", description=" + description +
                ", amount=" + amount +
                ", type=" + getType() +
                ", category=" + category +
                ", date=" + transactionDate +
                ", userId=" + userId +
                '}';
    }
}



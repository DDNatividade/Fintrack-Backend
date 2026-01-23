package com.apis.fintrack.domain.shared.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Value Object que representa una cantidad monetaria.
 * 
 * CaracterÃ­sticas:
 * - Inmutable
 * - PrecisiÃ³n de 2 decimales
 * - Operaciones aritmÃ©ticas seguras
 * - Soporte para mÃºltiples monedas (por defecto EUR)
 */
public final class Money {
    
    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("EUR");
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    private final BigDecimal amount;
    private final Currency currency;
    
    private Money(BigDecimal amount, Currency currency) {
        this.amount = amount.setScale(SCALE, ROUNDING_MODE);
        this.currency = currency;
    }
    
    /**
     * Crea Money con la moneda por defecto (EUR).
     * 
     * @param amount la cantidad
     * @return una nueva instancia de Money
     */
    public static Money of(BigDecimal amount) {
        return of(amount, DEFAULT_CURRENCY);
    }
    
    /**
     * Crea Money con una moneda especÃ­fica.
     * 
     * @param amount la cantidad
     * @param currency la moneda
     * @return una nueva instancia de Money
     */
    public static Money of(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new IllegalArgumentException("La cantidad no puede ser nula");
        }
        if (currency == null) {
            throw new IllegalArgumentException("La moneda no puede ser nula");
        }
        return new Money(amount, currency);
    }
    
    /**
     * Crea Money a partir de un double.
     * 
     * @param amount la cantidad
     * @return una nueva instancia de Money
     */
    public static Money of(double amount) {
        return of(BigDecimal.valueOf(amount));
    }
    
    /**
     * Crea Money con valor cero.
     * 
     * @return Money con cantidad 0.00
     */
    public static Money zero() {
        return of(BigDecimal.ZERO);
    }
    
    /**
     * Crea Money con valor cero en una moneda especÃ­fica.
     * 
     * @param currency la moneda
     * @return Money con cantidad 0.00
     */
    public static Money zero(Currency currency) {
        return of(BigDecimal.ZERO, currency);
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public Currency getCurrency() {
        return currency;
    }
    
    /**
     * Suma otra cantidad de Money.
     * 
     * @param other la cantidad a sumar
     * @return un nuevo Money con el resultado
     * @throws IllegalArgumentException si las monedas son diferentes
     */
    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }
    
    /**
     * Resta otra cantidad de Money.
     * 
     * @param other la cantidad a restar
     * @return un nuevo Money con el resultado
     * @throws IllegalArgumentException si las monedas son diferentes
     */
    public Money subtract(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }
    
    /**
     * Multiplica por un factor.
     * 
     * @param factor el multiplicador
     * @return un nuevo Money con el resultado
     */
    public Money multiply(BigDecimal factor) {
        return new Money(this.amount.multiply(factor), this.currency);
    }
    
    /**
     * Multiplica por un factor entero.
     * 
     * @param factor el multiplicador
     * @return un nuevo Money con el resultado
     */
    public Money multiply(int factor) {
        return multiply(BigDecimal.valueOf(factor));
    }
    
    /**
     * Divide por un divisor.
     * 
     * @param divisor el divisor
     * @return un nuevo Money con el resultado
     * @throws ArithmeticException si el divisor es cero
     */
    public Money divide(BigDecimal divisor) {
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("No se puede dividir por cero");
        }
        return new Money(this.amount.divide(divisor, SCALE, ROUNDING_MODE), this.currency);
    }
    
    /**
     * Verifica si la cantidad es positiva.
     * 
     * @return true si es mayor que cero
     */
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Verifica si la cantidad es negativa.
     * 
     * @return true si es menor que cero
     */
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * Verifica si la cantidad es cero.
     * 
     * @return true si es igual a cero
     */
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Verifica si esta cantidad es mayor que otra.
     * 
     * @param other la cantidad a comparar
     * @return true si esta cantidad es mayor
     */
    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }
    
    /**
     * Verifica si esta cantidad es menor que otra.
     * 
     * @param other la cantidad a comparar
     * @return true si esta cantidad es menor
     */
    public boolean isLessThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }
    
    /**
     * Verifica si esta cantidad es mayor o igual que otra.
     * 
     * @param other la cantidad a comparar
     * @return true si esta cantidad es mayor o igual
     */
    public boolean isGreaterThanOrEqual(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }
    
    /**
     * Devuelve el valor absoluto.
     * 
     * @return un nuevo Money con el valor absoluto
     */
    public Money abs() {
        return new Money(this.amount.abs(), this.currency);
    }
    
    /**
     * Niega el valor (cambia el signo).
     * 
     * @return un nuevo Money con el signo cambiado
     */
    public Money negate() {
        return new Money(this.amount.negate(), this.currency);
    }
    
    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                "No se pueden operar cantidades con diferentes monedas: " + 
                this.currency + " vs " + other.currency
            );
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 && 
               Objects.equals(currency, money.currency);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
    
    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency.getCurrencyCode();
    }
    
    /**
     * Formatea el valor con el sÃ­mbolo de la moneda.
     * 
     * @return el valor formateado (ej: "â‚¬100.00")
     */
    public String toFormattedString() {
        return currency.getSymbol() + amount.toPlainString();
    }
}



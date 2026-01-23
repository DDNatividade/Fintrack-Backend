package com.apis.fintrack.domain.transaction.port.input;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Puerto de entrada para el caso de uso de actualizaciÃ³n de transacciÃ³n.
 * 
 * Define el contrato que debe implementar la capa de aplicaciÃ³n
 * para actualizar transacciones existentes.
 */
public interface UpdateTransactionUseCase {
    
    /**
     * Comando para actualizaciÃ³n completa de una transacciÃ³n.
     */
    record UpdateTransactionCommand(
        Long transactionId,
        String description,
        BigDecimal amount,
        boolean isIncome,
        TransactionCategoryEnum category,
        LocalDate transactionDate
    ) {}
    
    /**
     * Actualiza completamente una transacciÃ³n (PUT).
     * 
     * @param command los nuevos datos de la transacciÃ³n
     * @return la transacciÃ³n actualizada
     * @throws com.apis.fintrack.Domain.transaction.exception.TransactionNotFoundException si no existe
     * @throws IllegalArgumentException si los datos no son vÃ¡lidos
     */
    Transaction update(UpdateTransactionCommand command);
    
    /**
     * Actualiza la descripciÃ³n de una transacciÃ³n.
     * 
     * @param transactionId ID de la transacciÃ³n
     * @param newDescription nueva descripciÃ³n
     * @return la transacciÃ³n actualizada
     */
    Transaction updateDescription(Long transactionId, String newDescription);
    
    /**
     * Actualiza el monto de una transacciÃ³n.
     * Mantiene el tipo (ingreso/gasto) actual.
     * 
     * @param transactionId ID de la transacciÃ³n
     * @param newAmount nuevo monto (siempre positivo)
     * @return la transacciÃ³n actualizada
     * @throws IllegalArgumentException si el monto es cero
     */
    Transaction updateAmount(Long transactionId, BigDecimal newAmount);
    
    /**
     * Cambia el tipo de transacciÃ³n (ingreso â†” gasto).
     * Invierte el signo del monto.
     * 
     * @param transactionId ID de la transacciÃ³n
     * @param isIncome true para convertir a ingreso, false para gasto
     * @return la transacciÃ³n actualizada
     */
    Transaction updateType(Long transactionId, boolean isIncome);
    
    /**
     * Actualiza la categorÃ­a de una transacciÃ³n.
     * Solo puede tener UNA categorÃ­a.
     * 
     * @param transactionId ID de la transacciÃ³n
     * @param newCategory nueva categorÃ­a
     * @return la transacciÃ³n actualizada
     */
    Transaction updateCategory(Long transactionId, TransactionCategoryEnum newCategory);
    
    /**
     * Actualiza la fecha de una transacciÃ³n.
     * 
     * @param transactionId ID de la transacciÃ³n
     * @param newDate nueva fecha
     * @return la transacciÃ³n actualizada
     */
    Transaction updateDate(Long transactionId, LocalDate newDate);
}



package com.apis.fintrack.domain.transaction.port.input;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Puerto de entrada para el caso de uso de creaciÃ³n de transacciÃ³n.
 * 
 * Define el contrato que debe implementar la capa de aplicaciÃ³n
 * para crear nuevas transacciones en el sistema.
 */
public interface CreateTransactionUseCase {
    
    /**
     * Comando que encapsula los datos necesarios para crear una transacciÃ³n.
     * 
     * @param description descripción de la transacciÃ³n
     * @param amount monto (siempre positivo, el signo se determina por isIncome)
     * @param isIncome true para ingreso, false para gasto
     * @param transactionDate fecha de la transacciÃ³n (opcional, default: hoy)
     * @param userId ID del usuario propietario
     */
    record CreateTransactionCommand(
        String description,
        BigDecimal amount,
        boolean isIncome,
        TransactionCategoryEnum category,
        LocalDate transactionDate,
        Long userId
    ) {
        /**
         * Constructor alternativo sin fecha (usa fecha actual).
         */
        public CreateTransactionCommand(String description, BigDecimal amount,
                                        boolean isIncome, TransactionCategoryEnum category, Long userId) {
            this(description, amount, isIncome, category, LocalDate.now(), userId);
        }
    }
    
    /**
     * Crea una nueva transacciÃ³n en el sistema.
     * 
     * Reglas de negocio aplicadas:
     * - El monto no puede ser cero
     * - Si es gasto (isIncome=false), el monto se almacena como negativo
     * - Si es ingreso (isIncome=true), el monto se almacena como positivo
     * - Solo puede tener una categorÃ­a
     * 
     * @param command los datos de la transacciÃ³n a crear
     * @return la transacciÃ³n creada con su ID asignado
     * @throws IllegalArgumentException si los datos no son vÃ¡lidos
     */
    Transaction execute(CreateTransactionCommand command);
}



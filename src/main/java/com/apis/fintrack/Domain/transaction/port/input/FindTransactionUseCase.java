package com.apis.fintrack.domain.transaction.port.input;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Puerto de entrada para el caso de uso de bÃºsqueda de transacciones.
 * 
 * Define el contrato que debe implementar la capa de aplicaciÃ³n
 * para buscar transacciones en el sistema.
 */
public interface FindTransactionUseCase {
    
    /**
     * Busca una transacciÃ³n por su ID.
     * 
     * @param transactionId el ID de la transacciÃ³n
     * @return la transacciÃ³n encontrada
     * @throws com.apis.fintrack.Domain.transaction.exception.TransactionNotFoundException si no existe
     */
    Transaction findById(Long transactionId);
    
    /**
     * Obtiene todas las transacciones paginadas.
     * 
     * @param page nÃºmero de pÃ¡gina (0-indexed)
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones
     */
    List<Transaction> findAll(int page, int size);
    
    /**
     * Busca transacciones por categorÃ­a.
     * 
     * @param category la categorÃ­a a buscar
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones de esa categorÃ­a
     */
    List<Transaction> findByCategory(TransactionCategoryEnum category, int page, int size);
    
    /**
     * Busca transacciones entre dos fechas.
     * 
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones en ese rango
     */
    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate, int page, int size);
    
    /**
     * Busca transacciones entre dos montos.
     * 
     * @param minAmount monto mÃ­nimo
     * @param maxAmount monto mÃ¡ximo
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones en ese rango de montos
     */
    List<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, int page, int size);
    
    /**
     * Busca solo ingresos o solo gastos.
     * 
     * @param isIncome true para ingresos, false para gastos
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones del tipo especificado
     */
    List<Transaction> findByType(boolean isIncome, int page, int size);
    
    /**
     * Busca transacciones de un usuario especÃ­fico.
     * 
     * @param userId ID del usuario
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones del usuario
     */
    List<Transaction> findByUserId(Long userId, int page, int size);
    
    /**
     * Obtiene todas las transacciones ordenadas por monto.
     * 
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones ordenadas por monto
     */
    List<Transaction> findAllOrderByAmount(int page, int size);
    
    /**
     * Obtiene todas las transacciones ordenadas por fecha.
     * 
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones ordenadas por fecha
     */
    List<Transaction> findAllOrderByDate(int page, int size);
}



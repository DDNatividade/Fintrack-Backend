package com.apis.fintrack.domain.transaction.port.input;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Puerto de entrada para el caso de uso de búsqueda de transacciones.
 *
 * Define el contrato que debe implementar la capa de aplicación
 * para buscar transacciones en el sistema.
 */
public interface FindTransactionUseCase {
    /**
     * Busca una transacción por su ID.
     *
     * @param transactionId el ID de la transacción
     * @return la transacción encontrada
     */
    Transaction findById(Long transactionId);

    /**
     * Obtiene todas las transacciones paginadas.
     * 
     * @param pageable objeto Pageable que contiene información de paginación
     * @return página de transacciones
     */
    Page<Transaction> findAll(Pageable pageable);

    /**
     * Busca transacciones por categoría.
     *
     * @param category la categoría a buscar
     * @param pageable objeto Pageable que contiene información de paginación
     * @return página de transacciones de esa categoría
     */
    Page<Transaction> findByCategory(TransactionCategoryEnum category, Pageable pageable);

    /**
     * Busca transacciones entre dos fechas.
     * 
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @param pageable objeto Pageable que contiene información de paginación
     * @return página de transacciones en ese rango
     */
    Page<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Busca transacciones entre dos montos.
     * 
     * @param minAmount monto mínimo
     * @param maxAmount monto máximo
     * @param pageable objeto Pageable que contiene información de paginación
     * @return página de transacciones en ese rango de montos
     */
    Page<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);

    /**
     * Busca solo ingresos o solo gastos.
     * 
     * @param isIncome true para ingresos, false para gastos
     * @param pageable objeto Pageable que contiene información de paginación
     * @return página de transacciones del tipo especificado
     */
    Page<Transaction> findByType(boolean isIncome, Pageable pageable);

    /**
     * Busca transacciones de un usuario específico.
     *
     * @param userId ID del usuario
     * @param pageable objeto Pageable que contiene información de paginación
     * @return página de transacciones del usuario
     */
    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    /**
     * Obtiene todas las transacciones ordenadas por monto.
     * 
     * @param pageable objeto Pageable que contiene información de paginación
     * @return página de transacciones ordenadas por monto
     */
    Page<Transaction> findAllOrderByAmount(Pageable pageable);

    /**
     * Obtiene todas las transacciones ordenadas por fecha.
     * 
     * @param pageable objeto Pageable que contiene información de paginación
     * @return página de transacciones ordenadas por fecha
     */
    Page<Transaction> findAllOrderByDate(Pageable pageable);
}

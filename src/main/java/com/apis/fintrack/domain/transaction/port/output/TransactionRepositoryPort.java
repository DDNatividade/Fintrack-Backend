package com.apis.fintrack.domain.transaction.port.output;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia de transacciones.
 * 
 * Define el contrato que debe implementar la capa de infraestructura
 * para persistir y recuperar transacciones.
 * 
 * Esta interfaz es PURA del dominio, sin dependencias de frameworks.
 */
public interface TransactionRepositoryPort {
    /**
     * Guarda una transacciÃ³n (nueva o existente).
     * 
     * @param transaction la transacciÃ³n a guardar
     * @return la transacciÃ³n guardada con ID asignado
     */
    Transaction save(Transaction transaction);
    
    /**
     * Busca una transacciÃ³n por su ID.
     * 
     * @param id el ID de la transacciÃ³n
     * @return Optional con la transacciÃ³n si existe
     */
    Optional<Transaction> findById(Long id);
    
    /**
     * Obtiene todas las transacciones paginadas.
     * 
     * @param pageable objeto Pageable con informaciÃ³n de pÃ¡gina y tamaÃ±o
     * @return Page con la lista de transacciones
     */
    Page<Transaction> findAll(Pageable pageable);

    /**
     * Busca transacciones por categorÃ­a.
     * 
     * @param category la categorÃ­a
     * @param pageable objeto Pageable con informaciÃ³n de pÃ¡gina y tamaÃ±o
     * @return Page con la lista de transacciones
     */
    Page<Transaction> findByCategory(TransactionCategoryEnum category, Pageable pageable);

    /**
     * Busca transacciones entre dos fechas.
     * 
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @param pageable objeto Pageable con informaciÃ³n de pÃ¡gina y tamaÃ±o
     * @return Page con la lista de transacciones
     */
    Page<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Busca transacciones entre dos montos.
     * 
     * @param minAmount monto mÃ­nimo
     * @param maxAmount monto mÃ¡ximo
     * @param pageable objeto Pageable con informaciÃ³n de pÃ¡gina y tamaÃ±o
     * @return Page con la lista de transacciones
     */
    Page<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);

    /**
     * Busca transacciones por tipo (ingreso/gasto).
     * 
     * @param isIncome true para ingresos, false para gastos
     * @param pageable objeto Pageable con información de página y tamaño
     * @return Page con la lista de transacciones
     */
    Page<Transaction> findByType(boolean isIncome, Pageable pageable);

    /**
     * Busca transacciones de un usuario.
     * 
     * @param userId ID del usuario
     * @param pageable objeto Pageable con informaciÃ³n de pÃ¡gina y tamaÃ±o
     * @return Page con la lista de transacciones del usuario
     */
    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    /**
     * Obtiene transacciones ordenadas por monto.
     * 
     * @param pageable objeto Pageable con informaciÃ³n de pÃ¡gina y tamaÃ±o
     * @return Page con la lista ordenada por monto
     */
    Page<Transaction> findAllOrderByAmount(Pageable pageable);

    /**
     * Obtiene transacciones ordenadas por fecha.
     * 
     * @param pageable objeto Pageable con informaciÃ³n de pÃ¡gina y tamaÃ±o
     * @return Page con la lista ordenada por fecha
     */
    Page<Transaction> findAllOrderByDate(Pageable pageable);

    /**
     * Elimina una transacciÃ³n por su ID.
     * 
     * @param id el ID de la transacciÃ³n
     */
    void deleteById(Long id);
    
    /**
     * Elimina todas las transacciones de un usuario.
     * 
     * @param userId el ID del usuario
     */
    void deleteAllByUserId(Long userId);
    
    /**
     * Verifica si existe una transacciÃ³n con el ID dado.
     * 
     * @param id el ID a verificar
     * @return true si existe
     */
    boolean existsById(Long id);
}

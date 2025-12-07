package com.apis.fintrack.domain.transaction.port.output;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
     * @param page nÃºmero de pÃ¡gina (0-indexed)
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones
     */
    List<Transaction> findAll(int page, int size);
    
    /**
     * Busca transacciones por categorÃ­a.
     * 
     * @param category la categorÃ­a
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones
     */
    List<Transaction> findByCategory(TransactionCategoryEnum category, int page, int size);
    
    /**
     * Busca transacciones entre dos fechas.
     * 
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones
     */
    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate, int page, int size);
    
    /**
     * Busca transacciones entre dos montos.
     * 
     * @param minAmount monto mÃ­nimo
     * @param maxAmount monto mÃ¡ximo
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones
     */
    List<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, int page, int size);
    
    /**
     * Busca transacciones por tipo (ingreso/gasto).
     * 
     * @param isIncome true para ingresos, false para gastos
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones
     */
    List<Transaction> findByType(boolean isIncome, int page, int size);
    
    /**
     * Busca transacciones de un usuario.
     * 
     * @param userId ID del usuario
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista de transacciones del usuario
     */
    List<Transaction> findByUserId(Long userId, int page, int size);
    
    /**
     * Obtiene transacciones ordenadas por monto.
     * 
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista ordenada por monto
     */
    List<Transaction> findAllOrderByAmount(int page, int size);
    
    /**
     * Obtiene transacciones ordenadas por fecha.
     * 
     * @param page nÃºmero de pÃ¡gina
     * @param size tamaÃ±o de pÃ¡gina
     * @return lista ordenada por fecha
     */
    List<Transaction> findAllOrderByDate(int page, int size);
    
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



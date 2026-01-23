package com.apis.fintrack.domain.transaction.port.input;

/**
 * Puerto de entrada para el caso de uso de eliminaciÃ³n de transacciÃ³n.
 * 
 * Define el contrato que debe implementar la capa de aplicaciÃ³n
 * para eliminar transacciones del sistema.
 */
public interface DeleteTransactionUseCase {
    
    /**
     * Elimina una transacciÃ³n por su ID.
     * 
     * @param transactionId el ID de la transacciÃ³n a eliminar
     * @throws com.apis.fintrack.Domain.transaction.exception.TransactionNotFoundException si no existe
     */
    void deleteById(Long transactionId);
    
    /**
     * Elimina todas las transacciones de un usuario.
     * 
     * @param userId el ID del usuario
     */
    void deleteAllByUserId(Long userId);
}



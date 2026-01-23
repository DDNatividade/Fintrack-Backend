package com.apis.fintrack.application.transaction.usecases;

import com.apis.fintrack.domain.transaction.exception.TransactionNotFoundException;
import com.apis.fintrack.domain.transaction.port.input.DeleteTransactionUseCase;
import com.apis.fintrack.domain.transaction.port.output.TransactionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ImplementaciÃ³n del caso de uso de eliminaciÃ³n de transacciÃ³n.
 */
@Service
@Transactional
public class DeleteTransactionUseCaseImpl implements DeleteTransactionUseCase {
    
    private final TransactionRepositoryPort transactionRepository;
    
    public DeleteTransactionUseCaseImpl(TransactionRepositoryPort transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    @Override
    public void deleteById(Long transactionId) {
        // Verificar que existe antes de eliminar
        if (!transactionRepository.existsById(transactionId)) {
            throw new TransactionNotFoundException(
                "No existe transacciÃ³n con ID: " + transactionId
            );
        }
        transactionRepository.deleteById(transactionId);
    }
    
    @Override
    public void deleteAllByUserId(Long userId) {
        transactionRepository.deleteAllByUserId(userId);
    }
}




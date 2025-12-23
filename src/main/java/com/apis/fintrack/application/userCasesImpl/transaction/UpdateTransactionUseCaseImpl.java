package com.apis.fintrack.application.transaction;

import com.apis.fintrack.domain.transaction.exception.TransactionNotFoundException;
import com.apis.fintrack.domain.transaction.model.*;
import com.apis.fintrack.domain.transaction.port.input.UpdateTransactionUseCase;
import com.apis.fintrack.domain.transaction.port.output.TransactionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ImplementaciÃ³n del caso de uso de actualizaciÃ³n de transacciÃ³n.
 * 
 * Aplica las reglas de negocio:
 * - El monto no puede ser cero
 * - Los gastos se almacenan con valor negativo
 * - Los ingresos se almacenan con valor positivo
 * - Solo puede tener una categorÃ­a
 */
@Service
@Transactional
public class UpdateTransactionUseCaseImpl implements UpdateTransactionUseCase {
    
    private final TransactionRepositoryPort transactionRepository;
    
    public UpdateTransactionUseCaseImpl(TransactionRepositoryPort transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    @Override
    public Transaction update(UpdateTransactionCommand command) {
        Transaction transaction = findTransactionOrThrow(command.transactionId());
        
        // Actualizar todos los campos
        transaction.changeDescription(command.description());
        transaction.changeType(command.isIncome());
        transaction.changeAmount(command.amount());
        transaction.changeCategory(command.category());
        
        if (command.transactionDate() != null) {
            transaction.changeDate(command.transactionDate());
        }
        
        return transactionRepository.save(transaction);
    }
    
    @Override
    public Transaction updateDescription(Long transactionId, String newDescription) {
        Transaction transaction = findTransactionOrThrow(transactionId);
        transaction.changeDescription(newDescription);
        return transactionRepository.save(transaction);
    }
    
    @Override
    public Transaction updateAmount(Long transactionId, BigDecimal newAmount) {
        Transaction transaction = findTransactionOrThrow(transactionId);
        transaction.changeAmount(newAmount);
        return transactionRepository.save(transaction);
    }
    
    @Override
    public Transaction updateType(Long transactionId, boolean isIncome) {
        Transaction transaction = findTransactionOrThrow(transactionId);
        transaction.changeType(isIncome);
        return transactionRepository.save(transaction);
    }
    
    @Override
    public Transaction updateCategory(Long transactionId, TransactionCategoryEnum newCategory) {
        Transaction transaction = findTransactionOrThrow(transactionId);
        transaction.changeCategory(newCategory);
        return transactionRepository.save(transaction);
    }
    
    @Override
    public Transaction updateDate(Long transactionId, LocalDate newDate) {
        Transaction transaction = findTransactionOrThrow(transactionId);
        transaction.changeDate(newDate);
        return transactionRepository.save(transaction);
    }
    
    private Transaction findTransactionOrThrow(Long transactionId) {
        return transactionRepository.findById(transactionId)
            .orElseThrow(() -> new TransactionNotFoundException(
                "No existe transacciÃ³n con ID: " + transactionId
            ));
    }
}




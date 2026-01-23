package com.apis.fintrack.application.transaction.usecases;

import com.apis.fintrack.application.transaction.mapper.TransactionUpdateCommandMapper;
import com.apis.fintrack.domain.transaction.exception.TransactionNotFoundException;
import com.apis.fintrack.domain.transaction.model.*;
import com.apis.fintrack.domain.transaction.port.input.UpdateTransactionUseCase;
import com.apis.fintrack.domain.transaction.port.output.TransactionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Implementación del caso de uso de actualización de transacción.
 * 
 * Aplica las reglas de negocio:
 * - El monto no puede ser cero
 * - Los gastos se almacenan con valor negativo
 * - Los ingresos se almacenan con valor positivo
 * - Solo puede tener una categoría
 */
@Service
@Transactional
public class UpdateTransactionUseCaseImpl implements UpdateTransactionUseCase {
    
    private final TransactionRepositoryPort transactionRepository;
    private final TransactionUpdateCommandMapper transactionUpdateCommandMapper;

    public UpdateTransactionUseCaseImpl(TransactionRepositoryPort transactionRepository, TransactionUpdateCommandMapper transactionUpdateCommandMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionUpdateCommandMapper = transactionUpdateCommandMapper;
    }
    
    @Override
    public Transaction update(UpdateTransactionCommand command) {
        Transaction transaction = findTransactionOrThrow(command.transactionId());
        transactionUpdateCommandMapper.applyUpdateCommand(transaction, command);
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
                "No existe transacción con ID: " + transactionId
            ));
    }
}

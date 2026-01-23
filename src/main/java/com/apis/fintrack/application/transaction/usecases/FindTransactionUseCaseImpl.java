package com.apis.fintrack.application.transaction.usecases;

import com.apis.fintrack.domain.transaction.exception.TransactionNotFoundException;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;
import com.apis.fintrack.domain.transaction.port.input.FindTransactionUseCase;
import com.apis.fintrack.domain.transaction.port.output.TransactionRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Implementación del caso de uso de búsqueda de transacciones.
 */
@Service
@Transactional(readOnly = true)
public class FindTransactionUseCaseImpl implements FindTransactionUseCase {
    
    private final TransactionRepositoryPort transactionRepository;
    
    public FindTransactionUseCaseImpl(TransactionRepositoryPort transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    @Override
    public Transaction findById(Long transactionId) {
        return transactionRepository.findById(transactionId)
            .orElseThrow(() -> new TransactionNotFoundException(
                "No existe transacción con ID: " + transactionId
            ));
    }
    
    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        Pageable effective = pageable != null ? pageable : Pageable.unpaged();
        return transactionRepository.findAll(effective);
    }
    
    @Override
    public Page<Transaction> findByCategory(TransactionCategoryEnum category, Pageable pageable) {
        Pageable effective = pageable != null ? pageable : Pageable.unpaged();
        return transactionRepository.findByCategory(category, effective);
    }
    
    @Override
    public Page<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable effective = pageable != null ? pageable : Pageable.unpaged();
        return transactionRepository.findByDateBetween(startDate, endDate, effective);
    }
    
    @Override
    public Page<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable) {
        Pageable effective = pageable != null ? pageable : Pageable.unpaged();
        return transactionRepository.findByAmountBetween(minAmount, maxAmount, effective);
    }
    
    @Override
    public Page<Transaction> findByType(boolean isIncome, Pageable pageable) {
        Pageable effective = pageable != null ? pageable : Pageable.unpaged();
        return transactionRepository.findByType(isIncome, effective);
    }
    
    @Override
    public Page<Transaction> findByUserId(Long userId, Pageable pageable) {
        Pageable effective = pageable != null ? pageable : Pageable.unpaged();
        return transactionRepository.findByUserId(userId, effective);
    }
    
    @Override
    public Page<Transaction> findAllOrderByAmount(Pageable pageable) {
        Pageable effective = pageable != null ? pageable : Pageable.unpaged();
        return transactionRepository.findAllOrderByAmount(effective);
    }
    
    @Override
    public Page<Transaction> findAllOrderByDate(Pageable pageable) {
        Pageable effective = pageable != null ? pageable : Pageable.unpaged();
        return transactionRepository.findAllOrderByDate(effective);
    }
}

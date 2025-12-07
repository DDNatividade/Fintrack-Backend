package com.apis.fintrack.application.transaction;

import com.apis.fintrack.domain.transaction.exception.TransactionNotFoundException;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;
import com.apis.fintrack.domain.transaction.port.input.FindTransactionUseCase;
import com.apis.fintrack.domain.transaction.port.output.TransactionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * ImplementaciÃ³n del caso de uso de bÃºsqueda de transacciones.
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
                "No existe transacciÃ³n con ID: " + transactionId
            ));
    }
    
    @Override
    public List<Transaction> findAll(int page, int size) {
        return transactionRepository.findAll(page, size);
    }
    
    @Override
    public List<Transaction> findByCategory(TransactionCategoryEnum category, int page, int size) {
        return transactionRepository.findByCategory(category, page, size);
    }
    
    @Override
    public List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate, 
                                                int page, int size) {
        return transactionRepository.findByDateBetween(startDate, endDate, page, size);
    }
    
    @Override
    public List<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, 
                                                  int page, int size) {
        return transactionRepository.findByAmountBetween(minAmount, maxAmount, page, size);
    }
    
    @Override
    public List<Transaction> findByType(boolean isIncome, int page, int size) {
        return transactionRepository.findByType(isIncome, page, size);
    }
    
    @Override
    public List<Transaction> findByUserId(Long userId, int page, int size) {
        return transactionRepository.findByUserId(userId, page, size);
    }
    
    @Override
    public List<Transaction> findAllOrderByAmount(int page, int size) {
        return transactionRepository.findAllOrderByAmount(page, size);
    }
    
    @Override
    public List<Transaction> findAllOrderByDate(int page, int size) {
        return transactionRepository.findAllOrderByDate(page, size);
    }
}




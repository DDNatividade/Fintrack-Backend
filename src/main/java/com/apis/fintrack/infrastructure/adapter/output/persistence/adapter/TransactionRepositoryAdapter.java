package com.apis.fintrack.infrastructure.adapter.output.persistence.adapter;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;
import com.apis.fintrack.domain.transaction.port.output.TransactionRepositoryPort;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.TransactionJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.mapper.TransactionPersistenceMapper;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa el puerto de salida TransactionRepositoryPort.
 * 
 * Traduce las operaciones del dominio a operaciones de Spring Data JPA.
 */
@Component
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {
    
    private final TransactionRepository transactionRepository;
    private final TransactionPersistenceMapper mapper;
    
    public TransactionRepositoryAdapter(TransactionRepository transactionRepository,
                                         TransactionPersistenceMapper mapper) {
        this.transactionRepository = transactionRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Transaction save(Transaction transaction) {
        TransactionJPAEntity jpaEntity;
        
        if (transaction.getId().isEmpty()) {
            // Nueva transacciÃ³n
            jpaEntity = mapper.toJpaEntity(transaction);
        } else {
            // ActualizaciÃ³n
            jpaEntity = transactionRepository.findById(transaction.getId().getValue())
                .orElseGet(() -> mapper.toJpaEntity(transaction));
            mapper.updateJpaEntity(transaction, jpaEntity);
        }
        
        TransactionJPAEntity savedEntity = transactionRepository.save(jpaEntity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id)
            .map(mapper::toDomain);
    }
    
    @Override
    public List<Transaction> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.shoeAllTransactions(pageable)
            .map(this::toTransactionList)
            .orElse(Collections.emptyList());
    }
    
    @Override
    public List<Transaction> findByCategory(TransactionCategoryEnum category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findAllByCategory(category, pageable)
            .map(this::toTransactionList)
            .orElse(Collections.emptyList());
    }
    
    @Override
    public List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate, 
                                                int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findTransactionEntitiesByTransaction_dateBetween(
                startDate, endDate, pageable)
            .map(this::toTransactionList)
            .orElse(Collections.emptyList());
    }
    
    @Override
    public List<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, 
                                                  int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findTransactionEntitiesByAmountBetween(
                minAmount, maxAmount, pageable)
            .map(this::toTransactionList)
            .orElse(Collections.emptyList());
    }
    
    @Override
    public List<Transaction> findByType(boolean isIncome, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findAllIncome(isIncome, pageable)
            .map(this::toTransactionList)
            .orElse(Collections.emptyList());
    }
    
    @Override
    public List<Transaction> findByUserId(Long userId, int page, int size) {
        // Note: You will need to add this method to the TransactionRepository
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findAll(pageable)
            .stream()
            .filter(t -> t.getUser().getUserId().equals(userId))
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Transaction> findAllOrderByAmount(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findAllOrderByAmount(pageable)
            .map(this::toTransactionList)
            .orElse(Collections.emptyList());
    }
    
    @Override
    public List<Transaction> findAllOrderByDate(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findAllOrderByTransactionDate(pageable)
            .map(this::toTransactionList)
            .orElse(Collections.emptyList());
    }
    
    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }
    
    @Override
    public void deleteAllByUserId(Long userId) {
        // Note: You will need to add this method to the TransactionRepository
        transactionRepository.findAll().stream()
            .filter(t -> t.getUser().getUserId().equals(userId))
            .forEach(transactionRepository::delete);
    }
    
    @Override
    public boolean existsById(Long id) {
        return transactionRepository.existsById(id);
    }
    
    /**
     * Convierte una pÃ¡gina de entidades JPA a lista de entidades de dominio.
     */
    private List<Transaction> toTransactionList(Page<TransactionJPAEntity> page) {
        return page.getContent().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
}



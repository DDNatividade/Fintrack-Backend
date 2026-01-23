package com.apis.fintrack.infrastructure.adapter.output.persistence.adapter;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;
import com.apis.fintrack.domain.transaction.port.output.TransactionRepositoryPort;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.TransactionJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.mapper.TransactionPersistenceMapper;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

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
            // Nueva transacción
            jpaEntity = mapper.toJpaEntity(transaction);
        } else {
            // Actualización
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
    public Page<Transaction> findAll(Pageable pageable) {
        Page<TransactionJPAEntity> entitiesPage = transactionRepository.findAll(pageable);
        return entitiesPage.map(mapper::toDomain);
    }

    @Override
    public Page<Transaction> findByCategory(TransactionCategoryEnum category, Pageable pageable) {
        Page<TransactionJPAEntity> page = transactionRepository.findAllByCategory(category, pageable);
        return page.map(mapper::toDomain);
    }

    @Override
    public Page<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<TransactionJPAEntity> page = transactionRepository.findTransactionEntitiesByTransaction_dateBetween(startDate, endDate, pageable);
        return page.map(mapper::toDomain);
    }

    @Override
    public Page<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable) {
        Page<TransactionJPAEntity> page = transactionRepository.findTransactionEntitiesByAmountBetween(minAmount, maxAmount, pageable);
        return page.map(mapper::toDomain);
    }

    @Override
    public Page<Transaction> findByType(boolean isIncome, Pageable pageable) {
        Page<TransactionJPAEntity> page = transactionRepository.findAllIncome(isIncome, pageable);
        return page.map(mapper::toDomain);
    }

    @Override
    public Page<Transaction> findByUserId(Long userId, Pageable pageable) {
        Page<TransactionJPAEntity> page = transactionRepository.findByUser_UserId(userId, pageable);
        return page.map(mapper::toDomain);
    }

    @Override
    public Page<Transaction> findAllOrderByAmount(Pageable pageable) {
        Page<TransactionJPAEntity> page = transactionRepository.findAllOrderByAmount(pageable);
        return page.map(mapper::toDomain);
    }

    @Override
    public Page<Transaction> findAllOrderByDate(Pageable pageable) {
        return transactionRepository.findAllOrderByTransactionDate(pageable)
            .map(mapper::toDomain);
    }
    
    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }
    
    @Override
    public void deleteAllByUserId(Long userId) {
        transactionRepository.findByUser_UserId(userId, Pageable.unpaged())
            .forEach(transactionRepository::delete);
    }
    
    @Override
    public boolean existsById(Long id) {
        return transactionRepository.existsById(id);
    }
}

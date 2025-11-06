package com.apis.fintrack.Service;

import com.apis.fintrack.Entity.CategoryEnum;
import com.apis.fintrack.Entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionService {
    void addTransaction(TransactionEntity transaction);
    TransactionEntity findTransactionById(Long id);
    Page<TransactionEntity> findAll(Pageable pageable);
    Page<TransactionEntity> findAllByCategory(CategoryEnum category, Pageable pageable);
    Page<TransactionEntity> findAllBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<TransactionEntity> findAllBetweenAmounts(BigDecimal firstAmount, BigDecimal secondAmount, Pageable pageable);
    Page<TransactionEntity> findAllOrderByAmount(Pageable pageable);
    Page<TransactionEntity> findAllOrderByTransactionDate(Pageable pageable);
    Page<TransactionEntity> findAllIfIncome(Boolean isIncome, Pageable pageable);
    void analizeTransaction(TransactionEntity transaction);
}

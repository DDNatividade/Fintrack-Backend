package com.apis.fintrack.infrastructure.adapter.output.persistence.repository;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.TransactionJPAEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionJPAEntity, Long> {

    @Query(value = "SELECT t from TransactionJPAEntity t")
    Page<TransactionJPAEntity> shoeAllTransactions(Pageable pageable);

    @Query(value = "SELECT t FROM TransactionJPAEntity t WHERE t.isIncome=?1")
    Page<TransactionJPAEntity> findAllIncome(Boolean isIncome , Pageable pageable);

    @Query(value ="SELECT t FROM TransactionJPAEntity t WHERE t.category=?1")
    Page<TransactionJPAEntity> findAllByCategory(TransactionCategoryEnum category, Pageable pageable);

    @Query(value = "SELECT t FROM TransactionJPAEntity" +
            " t WHERE t.transaction_date BETWEEN :startDate and :endDate")
    Page<TransactionJPAEntity>
    findTransactionEntitiesByTransaction_dateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(value ="SELECT t FROM TransactionJPAEntity t WHERE t.amount " +
            "BETWEEN :firstAmount and :secondAmount")
    Page<TransactionJPAEntity>
    findTransactionEntitiesByAmountBetween(BigDecimal firstAmount, BigDecimal secondAmount, Pageable pageable);

    @Query(value = "SELECT t FROM TransactionJPAEntity t ORDER BY t.amount")
    Page<TransactionJPAEntity> findAllOrderByAmount(Pageable pageable);

    @Query(value ="SELECT t FROM TransactionJPAEntity t ORDER BY t.transaction_date")
    Page<TransactionJPAEntity> findAllOrderByTransactionDate(Pageable pageable);

    // Buscar transacciones por userId
    Page<TransactionJPAEntity> findByUser_UserId(Long userId, Pageable pageable);

}

package com.apis.fintrack.DAO;

import com.apis.fintrack.Entity.CategoryEnum;
import com.apis.fintrack.Entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(value = "SELECT t from TransactionEntity t")
    Optional<Page<TransactionEntity>> shoeAllTransactions(Pageable pageable);

    @Query(value = "SELECT t FROM TransactionEntity t WHERE t.isIncome=?1")
    Optional<Page<TransactionEntity>> findAllIncome(Boolean isIncome ,Pageable pageable);

    @Query(value ="SELECT t FROM TransactionEntity t WHERE t.category=?1")
    Optional<Page<TransactionEntity>> findAllByCategory(CategoryEnum category, Pageable pageable);

    @Query(value = "SELECT t FROM TransactionEntity" +
            " t WHERE t.transaction_date BETWEEN :startDate and :endDate")
    Optional<Page<TransactionEntity>>
    findTransactionEntitiesByTransaction_dateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(value ="SELECT t FROM TransactionEntity t WHERE t.amount " +
            "BETWEEN :firstAmount and :secondAmount")
    Optional<Page<TransactionEntity>>
    findTransactionEntitiesByAmountBetween(Double firstAmount, Double secondAmount, Pageable pageable);

    @Query(value = "SELECT t FROM TransactionEntity t ORDER BY T.amount")
    Optional<Page<TransactionEntity>> findAllOrderByAmount(Pageable pageable);

    @Query(value ="SELECT t FROM TransactionEntity t ORDER BY t.transaction_date")
    Optional<Page<TransactionEntity>> findAllOrderByTransactionDate(Pageable pageable);

}

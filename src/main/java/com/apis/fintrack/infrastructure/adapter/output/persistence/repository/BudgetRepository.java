package com.apis.fintrack.infrastructure.adapter.output.persistence.repository;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.BudgetJPAEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetJPAEntity, Long> {

    @Query("SELECT b FROM BudgetJPAEntity b WHERE b.user.userId = :userId")
    List<BudgetJPAEntity> findByUserId(Long userId);

    @Query("SELECT b FROM BudgetJPAEntity b WHERE b.user.userId = :userId")
    Page<BudgetJPAEntity> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT b FROM BudgetJPAEntity b WHERE b.user.userId = :userId AND b.category = :category")
    Optional<BudgetJPAEntity> findByUserIdAndCategory(Long userId, TransactionCategoryEnum category);

    @Query("SELECT b FROM BudgetJPAEntity b WHERE b.user.userId = :userId AND b.startDate >= :startDate AND b.endDate <= :endDate")
    List<BudgetJPAEntity> findByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT b FROM BudgetJPAEntity b WHERE b.user.userId = :userId AND b.category = :category AND b.startDate <= :currentDate AND b.endDate >= :currentDate")
    Optional<BudgetJPAEntity> findCurrentBudget(Long userId, TransactionCategoryEnum category, LocalDate currentDate);

    @Query("SELECT COUNT(b) > 0 FROM BudgetJPAEntity b WHERE b.user.userId = :userId AND b.category = :category")
    boolean existsByUserIdAndCategory(Long userId, TransactionCategoryEnum category);

    @Modifying
    @Query("DELETE FROM BudgetJPAEntity b WHERE b.user.userId = :userId")
    void deleteAllByUserId(Long userId);
}


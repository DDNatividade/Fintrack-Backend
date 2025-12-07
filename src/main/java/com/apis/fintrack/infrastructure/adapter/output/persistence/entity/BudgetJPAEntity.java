package com.apis.fintrack.infrastructure.adapter.output.persistence.entity;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    @NotNull
    private BigDecimal limitAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionCategoryEnum category;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserJPAEntity user;
}


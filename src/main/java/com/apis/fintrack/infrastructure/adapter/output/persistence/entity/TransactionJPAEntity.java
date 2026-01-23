package com.apis.fintrack.infrastructure.adapter.output.persistence.entity;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionJPAEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 100)
    private String description;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate transaction_date;

    @NotNull
    private TransactionCategoryEnum category;

    @NotNull
    private boolean isIncome;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserJPAEntity user;

}


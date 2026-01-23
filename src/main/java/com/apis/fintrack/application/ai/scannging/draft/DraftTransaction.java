package com.apis.fintrack.application.ai.scannging.draft;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * DRAFT model representing a transaction proposed by an AI engine
 * that must be reviewed/confirmed by the user. It belongs to the
 * application layer (it is not a domain entity) and contains no
 * invariants or business logic.
 */
@Getter
@Setter
public class DraftTransaction {

    private final UUID userId;
    private String source;
    private BigDecimal amount;
    private String currency;
    private LocalDate suggestedDate;
    private String description;
    private String merchant;
    private String category;
    private DraftStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public DraftTransaction(
                            UUID userId,
                            String source,
                            BigDecimal amount,
                            String currency,
                            LocalDate suggestedDate,
                            String description,
                            String merchant,
                            String category,
                            DraftStatus status,
                            Instant createdAt,
                            Instant updatedAt) {
        this.userId = userId;
        this.source = source;
        this.amount = amount;
        this.currency = currency;
        this.suggestedDate = suggestedDate;
        this.description = description;
        this.merchant = merchant;
        this.category = category;
        this.status = status == null ? DraftStatus.NEW : status;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.updatedAt = updatedAt == null ? this.createdAt : updatedAt;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DraftTransaction that = (DraftTransaction) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getSource(), that.getSource()) && Objects.equals(getAmount(), that.getAmount()) && Objects.equals(getCurrency(), that.getCurrency()) && Objects.equals(getSuggestedDate(), that.getSuggestedDate()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getMerchant(), that.getMerchant()) && Objects.equals(getCategory(), that.getCategory()) && getStatus() == that.getStatus() && Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getUpdatedAt(), that.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getSource(), getAmount(), getCurrency(), getSuggestedDate(), getDescription(), getMerchant(), getCategory(), getStatus(), getCreatedAt(), getUpdatedAt());
    }

    @Override
    public String toString() {
        return "DraftTransaction{" +
                ", userId=" + userId +
                ", source='" + source + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", suggestedDate=" + suggestedDate +
                ", merchant='" + merchant + '\'' +
                ", category='" + category + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public DraftTransaction withStatus(DraftStatus draftStatus) {
        return new DraftTransaction(
                this.userId,
                this.source,
                this.amount,
                this.currency,
                this.suggestedDate,
                this.description,
                this.merchant,
                this.category,
                draftStatus,
                this.createdAt,
                Instant.now()
        );
    }
}


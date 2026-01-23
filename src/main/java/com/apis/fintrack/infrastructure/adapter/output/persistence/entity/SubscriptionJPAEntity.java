package com.apis.fintrack.infrastructure.adapter.output.persistence.entity;

import com.apis.fintrack.domain.subscription.model.SubscriptionType;
import com.apis.fintrack.domain.payment.model.PaymentMethodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;

    private LocalDateTime subscriptionDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionType status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserJPAEntity costumer;

    private boolean isActive;

    // PaymentMethod como campos embebidos (Value Object)
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method_type")
    private PaymentMethodType paymentMethodType;

    @Column(name = "payment_external_token")
    private String paymentExternalToken;

    @Column(name = "payment_last_four_digits", length = 4)
    private String paymentLastFourDigits;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentJPAEntity> payments;

}


package com.apis.fintrack.domain.subscription;

import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.payment.model.PaymentDate;
import com.apis.fintrack.domain.payment.model.PaymentMethod;
import com.apis.fintrack.domain.payment.model.PaymentMethodType;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.model.SubscriptionDate;
import com.apis.fintrack.domain.subscription.model.SubscriptionId;
import com.apis.fintrack.domain.subscription.model.SubscriptionType;
import com.apis.fintrack.domain.user.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Subscription} domain entity.
 *
 * <p>Tests cover factory method, domain behavior, state changes,
 * and query methods following specification-based testing approaches.</p>
 */
@DisplayName("Subscription Domain Entity Tests")
class SubscriptionTest {

    private SubscriptionDate validSubscriptionDate;
    private UserId validUserId;
    private PaymentMethod validPaymentMethod;
    private Money validAmount;

    @BeforeEach
    void setUp() {
        validSubscriptionDate = SubscriptionDate.now();
        validUserId = UserId.of(1L);
        validPaymentMethod = PaymentMethod.of(
                PaymentMethodType.CREDIT_CARD,
                "tok_visa_123",
                "4242"
        );
        validAmount = Money.of(new BigDecimal("9.99"));
    }

    // ==================== FACTORY METHOD: create() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: create()")
    class CreateFactoryTests {

        /**
         * Verifies that create() returns a new Subscription with correct initial state.
         */
        @Test
        @DisplayName("Should create Subscription with correct initial state")
        void create_withValidParams_shouldCreateSubscription() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );

            assertNotNull(subscription);
            assertTrue(subscription.getId().isEmpty());
            assertEquals(validSubscriptionDate, subscription.getSubscriptionDate());
            assertEquals(SubscriptionType.MONTHLY, subscription.getType());
            assertEquals(validUserId, subscription.getUserId());
            assertTrue(subscription.isActive());
            assertEquals(validPaymentMethod, subscription.getPaymentMethod());
            assertTrue(subscription.getPayments().isEmpty());
        }

        /**
         * Verifies that create() sets isActive to true by default.
         */
        @Test
        @DisplayName("Should create active subscription by default")
        void create_shouldBeActiveByDefault() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.ANNUAL,
                    validUserId,
                    validPaymentMethod
            );

            assertTrue(subscription.isActive());
        }

        /**
         * Verifies that create() assigns empty SubscriptionId.
         */
        @Test
        @DisplayName("Should assign empty SubscriptionId")
        void create_shouldAssignEmptyId() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );

            assertTrue(subscription.getId().isEmpty());
        }
    }

    // ==================== CONSTRUCTOR - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        /**
         * Verifies that constructor creates subscription with all parameters.
         */
        @Test
        @DisplayName("Should create Subscription with all parameters")
        void constructor_withAllParams_shouldCreateSubscription() {
            SubscriptionId id = SubscriptionId.of(1L);
            List<Payment> payments = new ArrayList<>();

            Subscription subscription = new Subscription(
                    id,
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    true,
                    validPaymentMethod,
                    payments
            );

            assertNotNull(subscription);
            assertEquals(id, subscription.getId());
            assertTrue(subscription.isActive());
        }

        /**
         * Verifies that constructor handles null payments list.
         */
        @Test
        @DisplayName("Should handle null payments list")
        void constructor_withNullPayments_shouldInitializeEmptyList() {
            Subscription subscription = new Subscription(
                    SubscriptionId.of(1L),
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    true,
                    validPaymentMethod,
                    null
            );

            assertNotNull(subscription.getPayments());
            assertTrue(subscription.getPayments().isEmpty());
        }
    }

    // ==================== registerPaymentSucceeded() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Method: registerPaymentSucceeded()")
    class RegisterPaymentSucceededTests {

        /**
         * Verifies that registerPaymentSucceeded() adds payment and marks as succeeded.
         */
        @Test
        @DisplayName("Should add payment and mark as succeeded")
        void registerPaymentSucceeded_shouldAddPaymentAndMarkSucceeded() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );
            Payment payment = Payment.create(
                    PaymentDate.now(),
                    validUserId,
                    SubscriptionId.of(1L),
                    validAmount
            );

            subscription.registerPaymentSucceeded(payment);

            assertEquals(1, subscription.getPayments().size());
            assertTrue(payment.isPaid());
        }

        /**
         * Verifies that registerPaymentSucceeded() keeps subscription active.
         */
        @Test
        @DisplayName("Should keep subscription active after successful payment")
        void registerPaymentSucceeded_shouldKeepActive() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );
            Payment payment = Payment.create(
                    PaymentDate.now(),
                    validUserId,
                    SubscriptionId.of(1L),
                    validAmount
            );

            subscription.registerPaymentSucceeded(payment);

            assertTrue(subscription.isActive());
        }

        /**
         * Verifies that null payment throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException for null payment")
        void registerPaymentSucceeded_nullPayment_shouldThrowException() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );

            assertThrows(NullPointerException.class,
                    () -> subscription.registerPaymentSucceeded(null));
        }

        /**
         * Verifies idempotency: duplicate payments are ignored.
         */
        @Test
        @DisplayName("Should ignore duplicate payments (idempotency)")
        void registerPaymentSucceeded_duplicatePayment_shouldBeIgnored() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );
            PaymentDate paymentDate = PaymentDate.now();
            Payment payment1 = Payment.create(paymentDate, validUserId, SubscriptionId.of(1L), validAmount);
            Payment payment2 = Payment.create(paymentDate, validUserId, SubscriptionId.of(1L), validAmount);

            subscription.registerPaymentSucceeded(payment1);
            subscription.registerPaymentSucceeded(payment2);

            // Only one payment should be added due to idempotency
            assertEquals(1, subscription.getPayments().size());
        }
    }

    // ==================== registerPaymentFailed() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Method: registerPaymentFailed()")
    class RegisterPaymentFailedTests {

        /**
         * Verifies that registerPaymentFailed() adds payment and marks as failed.
         */
        @Test
        @DisplayName("Should add payment and mark as failed")
        void registerPaymentFailed_shouldAddPaymentAndMarkFailed() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );
            Payment payment = Payment.create(
                    PaymentDate.now(),
                    validUserId,
                    SubscriptionId.of(1L),
                    validAmount
            );

            subscription.registerPaymentFailed(payment);

            assertEquals(1, subscription.getPayments().size());
            assertTrue(payment.isFailed());
        }

        /**
         * Verifies that null payment throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException for null payment")
        void registerPaymentFailed_nullPayment_shouldThrowException() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );

            assertThrows(NullPointerException.class,
                    () -> subscription.registerPaymentFailed(null));
        }
    }

    // ==================== changeType() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Method: changeType()")
    class ChangeTypeTests {

        /**
         * Verifies that changeType() changes subscription type.
         */
        @Test
        @DisplayName("Should change subscription type")
        void changeType_shouldChangeType() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );

            subscription.changeType(SubscriptionType.ANNUAL);

            assertEquals(SubscriptionType.ANNUAL, subscription.getType());
        }

        /**
         * Verifies that null type throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException for null type")
        void changeType_nullType_shouldThrowException() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );

            assertThrows(NullPointerException.class,
                    () -> subscription.changeType(null));
        }

        /**
         * Verifies that changing ANNUAL with payments throws exception.
         */
        @Test
        @DisplayName("Should throw exception when changing ANNUAL with existing payments")
        void changeType_annualWithPayments_shouldThrowException() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.ANNUAL,
                    validUserId,
                    validPaymentMethod
            );
            Payment payment = Payment.create(
                    PaymentDate.now(),
                    validUserId,
                    SubscriptionId.of(1L),
                    validAmount
            );
            subscription.registerPaymentSucceeded(payment);

            assertThrows(IllegalArgumentException.class,
                    () -> subscription.changeType(SubscriptionType.MONTHLY));
        }
    }

    // ==================== deactivateSubscription() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Method: deactivateSubscription()")
    class DeactivateSubscriptionTests {

        /**
         * Verifies that deactivateSubscription() sets isActive to false.
         */
        @Test
        @DisplayName("Should deactivate subscription")
        void deactivateSubscription_shouldSetInactive() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );

            subscription.deactivateSubscription();

            assertFalse(subscription.isActive());
        }

        /**
         * Verifies that deactivating with pending payments throws exception.
         */
        @Test
        @DisplayName("Should throw exception when there are pending payments")
        void deactivateSubscription_withPendingPayments_shouldThrowException() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );
            // Add pending payment directly to simulate pending state
            Payment pendingPayment = Payment.create(
                    PaymentDate.of(LocalDate.now().plusDays(5)),
                    validUserId,
                    SubscriptionId.of(1L),
                    validAmount
            );
            subscription.getPayments().add(pendingPayment);

            assertThrows(IllegalArgumentException.class,
                    () -> subscription.deactivateSubscription());
        }
    }

    // ==================== activateSubscription() - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Method: activateSubscription()")
    class ActivateSubscriptionTests {

        /**
         * Verifies that activateSubscription() sets isActive to true.
         */
        @Test
        @DisplayName("Should activate subscription")
        void activateSubscription_shouldSetActive() {
            Subscription subscription = new Subscription(
                    SubscriptionId.of(1L),
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    false, // inactive
                    validPaymentMethod,
                    new ArrayList<>()
            );

            subscription.activateSubscription();

            assertTrue(subscription.isActive());
        }
    }

    // ==================== changePaymentMethod() - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Method: changePaymentMethod()")
    class ChangePaymentMethodTests {

        /**
         * Verifies that changePaymentMethod() updates payment method.
         */
        @Test
        @DisplayName("Should change payment method")
        void changePaymentMethod_shouldUpdate() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );
            PaymentMethod newMethod = PaymentMethod.of(
                    PaymentMethodType.DEBIT_CARD,
                    "tok_debit_456",
                    "1234"
            );

            subscription.changePaymentMethod(newMethod);

            assertEquals(newMethod, subscription.getPaymentMethod());
        }

        /**
         * Verifies that null payment method throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException for null payment method")
        void changePaymentMethod_null_shouldThrowException() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );

            assertThrows(NullPointerException.class,
                    () -> subscription.changePaymentMethod(null));
        }
    }

    // ==================== QUERY METHODS - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Query Methods")
    class QueryMethodsTests {

        /**
         * Verifies hasPendingPayments() returns false when no pending payments.
         */
        @Test
        @DisplayName("hasPendingPayments() should return false when no pending payments")
        void hasPendingPayments_noPending_shouldReturnFalse() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );

            assertFalse(subscription.hasPendingPayments());
        }

        /**
         * Verifies hasPendingPayments() returns true when there are pending payments.
         */
        @Test
        @DisplayName("hasPendingPayments() should return true when there are pending payments")
        void hasPendingPayments_withPending_shouldReturnTrue() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );
            Payment pendingPayment = Payment.create(
                    PaymentDate.of(LocalDate.now().plusDays(5)),
                    validUserId,
                    SubscriptionId.of(1L),
                    validAmount
            );
            subscription.getPayments().add(pendingPayment);

            assertTrue(subscription.hasPendingPayments());
        }

        /**
         * Verifies hasPaymentMethod() returns true when payment method exists.
         */
        @Test
        @DisplayName("hasPaymentMethod() should return true when payment method exists")
        void hasPaymentMethod_withMethod_shouldReturnTrue() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );

            assertTrue(subscription.hasPaymentMethod());
        }

        /**
         * Verifies getTotalPaid() returns correct sum of paid payments.
         */
        @Test
        @DisplayName("getTotalPaid() should return sum of paid payments")
        void getTotalPaid_withPaidPayments_shouldReturnSum() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );
            Payment payment1 = Payment.create(
                    PaymentDate.of(LocalDate.now().minusDays(1)),
                    validUserId,
                    SubscriptionId.of(1L),
                    Money.of(new BigDecimal("10.00"))
            );
            Payment payment2 = Payment.create(
                    PaymentDate.of(LocalDate.now().minusDays(2)),
                    validUserId,
                    SubscriptionId.of(1L),
                    Money.of(new BigDecimal("20.00"))
            );

            subscription.registerPaymentSucceeded(payment1);
            subscription.registerPaymentSucceeded(payment2);

            Money total = subscription.getTotalPaid();

            assertEquals(new BigDecimal("30.00"), total.getAmount());
        }

        /**
         * Verifies getTotalPaid() returns zero when no payments.
         */
        @Test
        @DisplayName("getTotalPaid() should return zero when no payments")
        void getTotalPaid_noPayments_shouldReturnZero() {
            Subscription subscription = Subscription.create(
                    validSubscriptionDate,
                    SubscriptionType.MONTHLY,
                    validUserId,
                    validPaymentMethod
            );

            Money total = subscription.getTotalPaid();

            assertTrue(total.isZero());
        }
    }
}


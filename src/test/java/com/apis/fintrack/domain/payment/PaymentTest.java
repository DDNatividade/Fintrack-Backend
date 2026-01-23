package com.apis.fintrack.domain.payment;

import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.payment.model.PaymentDate;
import com.apis.fintrack.domain.payment.model.PaymentStatus;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.subscription.model.SubscriptionId;
import com.apis.fintrack.domain.user.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Payment} domain entity.
 *
 * <p>Tests cover the factory method, state transitions, and query methods
 * following specification-based and structural testing approaches.</p>
 */
@DisplayName("Payment Domain Entity Tests")
class PaymentTest {

    private PaymentDate futureDate;
    private PaymentDate pastDate;
    private PaymentDate today;
    private UserId validUserId;
    private SubscriptionId validSubscriptionId;
    private Money validAmount;

    @BeforeEach
    void setUp() {
        futureDate = PaymentDate.of(LocalDate.now().plusDays(10));
        pastDate = PaymentDate.of(LocalDate.now().minusDays(5));
        today = PaymentDate.of(LocalDate.now());
        validUserId = UserId.of(1L);
        validSubscriptionId = SubscriptionId.of(1L);
        validAmount = Money.of(new BigDecimal("99.99"));
    }

    // ==================== FACTORY TESTS (HIGH PRIORITY) ====================

    @Nested
    @DisplayName("Factory Method: create()")
    class FactoryTests {

        /**
         * Verifies that a newly created Payment has PENDING status.
         * This is a fundamental invariant: all new payments start as pending.
         */
        @Test
        @DisplayName("Should create payment with PENDING status")
        void create_shouldReturnPaymentWithPendingStatus() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);

            assertEquals(PaymentStatus.PENDING, payment.getStatus());
        }

        /**
         * Verifies that the factory assigns an empty PaymentId.
         * Empty IDs indicate the payment has not been persisted yet.
         */
        @Test
        @DisplayName("Should assign empty PaymentId to new payment")
        void create_shouldAssignEmptyPaymentId() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);

            assertTrue(payment.getId().isEmpty());
        }
    }

    // ==================== STATE TRANSITIONS: markAsSucceeded() (HIGH PRIORITY) ====================

    @Nested
    @DisplayName("State Transition: markAsSucceeded()")
    class MarkAsSucceededTests {

        /**
         * Verifies the valid transition from PENDING to SUCCEEDED.
         * This is the happy path for payment completion.
         */
        @Test
        @DisplayName("Should change status from PENDING to SUCCEEDED")
        void markAsSucceeded_fromPending_shouldChangeStatusToSucceeded() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);

            payment.markAsSucceeded();

            assertEquals(PaymentStatus.SUCCEEDED, payment.getStatus());
        }

        /**
         * Verifies idempotency: calling markAsSucceeded() on an already
         * succeeded payment should not throw an exception.
         */
        @Test
        @DisplayName("Should be idempotent when already SUCCEEDED")
        void markAsSucceeded_fromSucceeded_shouldBeIdempotent() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsSucceeded();

            assertDoesNotThrow(() -> payment.markAsSucceeded());
            assertEquals(PaymentStatus.SUCCEEDED, payment.getStatus());
        }

        /**
         * Verifies that transitioning from FAILED to SUCCEEDED is not allowed.
         * A failed payment cannot be marked as successful without a new attempt.
         */
        @Test
        @DisplayName("Should throw exception when transitioning from FAILED to SUCCEEDED")
        void markAsSucceeded_fromFailed_shouldThrowException() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsFailed();

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> payment.markAsSucceeded()
            );

            assertTrue(exception.getMessage().contains("failed"));
        }
    }

    // ==================== STATE TRANSITIONS: markAsFailed() (HIGH PRIORITY) ====================

    @Nested
    @DisplayName("State Transition: markAsFailed()")
    class MarkAsFailedTests {

        /**
         * Verifies the valid transition from PENDING to FAILED.
         * This represents a payment attempt that did not succeed.
         */
        @Test
        @DisplayName("Should change status from PENDING to FAILED")
        void markAsFailed_fromPending_shouldChangeStatusToFailed() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);

            payment.markAsFailed();

            assertEquals(PaymentStatus.FAILED, payment.getStatus());
        }

        /**
         * Verifies idempotency: calling markAsFailed() on an already
         * failed payment should not throw an exception.
         */
        @Test
        @DisplayName("Should be idempotent when already FAILED")
        void markAsFailed_fromFailed_shouldBeIdempotent() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsFailed();

            assertDoesNotThrow(() -> payment.markAsFailed());
            assertEquals(PaymentStatus.FAILED, payment.getStatus());
        }

        /**
         * Verifies that transitioning from SUCCEEDED to FAILED is not allowed.
         * A successful payment cannot be marked as failed (would require refund logic).
         */
        @Test
        @DisplayName("Should throw exception when transitioning from SUCCEEDED to FAILED")
        void markAsFailed_fromSucceeded_shouldThrowException() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsSucceeded();

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> payment.markAsFailed()
            );

            assertTrue(exception.getMessage().contains("succeeded"));
        }
    }

    // ==================== QUERY METHODS: Status Checks (MEDIUM PRIORITY) ====================

    @Nested
    @DisplayName("Query Methods: Status Checks")
    class StatusQueryTests {

        /**
         * Verifies that isPaid() returns true only when status is SUCCEEDED.
         */
        @Test
        @DisplayName("isPaid should return true when status is SUCCEEDED")
        void isPaid_whenSucceeded_shouldReturnTrue() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsSucceeded();

            assertTrue(payment.isPaid());
        }

        /**
         * Verifies that isPaid() returns false when status is PENDING.
         */
        @Test
        @DisplayName("isPaid should return false when status is PENDING")
        void isPaid_whenPending_shouldReturnFalse() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);

            assertFalse(payment.isPaid());
        }

        /**
         * Verifies that isPaid() returns false when status is FAILED.
         */
        @Test
        @DisplayName("isPaid should return false when status is FAILED")
        void isPaid_whenFailed_shouldReturnFalse() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsFailed();

            assertFalse(payment.isPaid());
        }

        /**
         * Verifies that isPending() returns true when status is PENDING.
         */
        @Test
        @DisplayName("isPending should return true when status is PENDING")
        void isPending_whenPending_shouldReturnTrue() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);

            assertTrue(payment.isPending());
        }

        /**
         * Verifies that isPending() returns false when status is SUCCEEDED.
         */
        @Test
        @DisplayName("isPending should return false when status is SUCCEEDED")
        void isPending_whenSucceeded_shouldReturnFalse() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsSucceeded();

            assertFalse(payment.isPending());
        }

        /**
         * Verifies that isFailed() returns true when status is FAILED.
         */
        @Test
        @DisplayName("isFailed should return true when status is FAILED")
        void isFailed_whenFailed_shouldReturnTrue() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsFailed();

            assertTrue(payment.isFailed());
        }

        /**
         * Verifies that isFailed() returns false when status is PENDING.
         */
        @Test
        @DisplayName("isFailed should return false when status is PENDING")
        void isFailed_whenPending_shouldReturnFalse() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);

            assertFalse(payment.isFailed());
        }
    }

    // ==================== QUERY METHODS: Overdue Logic (MEDIUM PRIORITY) ====================

    @Nested
    @DisplayName("Query Methods: Overdue Logic")
    class OverdueQueryTests {

        /**
         * Verifies that a PENDING payment with a past due date is considered overdue.
         */
        @Test
        @DisplayName("isOverdue should return true when PENDING and date is in the past")
        void isOverdue_whenPendingAndPastDate_shouldReturnTrue() {
            Payment payment = Payment.create(pastDate, validUserId, validSubscriptionId, validAmount);

            assertTrue(payment.isOverdue());
        }

        /**
         * Verifies that a PENDING payment with a future due date is not overdue.
         */
        @Test
        @DisplayName("isOverdue should return false when PENDING and date is in the future")
        void isOverdue_whenPendingAndFutureDate_shouldReturnFalse() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);

            assertFalse(payment.isOverdue());
        }

        /**
         * Verifies that a PENDING payment due today is not considered overdue.
         * The payment is only overdue if the due date has passed.
         */
        @Test
        @DisplayName("isOverdue should return false when PENDING and due today")
        void isOverdue_whenPendingAndDueToday_shouldReturnFalse() {
            Payment payment = Payment.create(today, validUserId, validSubscriptionId, validAmount);

            assertFalse(payment.isOverdue());
        }

        /**
         * Verifies that a SUCCEEDED payment is never considered overdue,
         * regardless of the due date.
         */
        @Test
        @DisplayName("isOverdue should return false when SUCCEEDED")
        void isOverdue_whenSucceeded_shouldReturnFalse() {
            Payment payment = Payment.create(pastDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsSucceeded();

            assertFalse(payment.isOverdue());
        }

        /**
         * Verifies that a FAILED payment is never considered overdue,
         * regardless of the due date.
         */
        @Test
        @DisplayName("isOverdue should return false when FAILED")
        void isOverdue_whenFailed_shouldReturnFalse() {
            Payment payment = Payment.create(pastDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsFailed();

            assertFalse(payment.isOverdue());
        }
    }

    // ==================== QUERY METHODS: daysOverdue() (MEDIUM PRIORITY) ====================

    @Nested
    @DisplayName("Query Methods: daysOverdue()")
    class DaysOverdueTests {

        /**
         * Verifies that daysOverdue() returns the correct number of days
         * when the payment is overdue.
         */
        @Test
        @DisplayName("Should return correct number of days when overdue")
        void daysOverdue_whenOverdue_shouldReturnCorrectDays() {
            PaymentDate fiveDaysAgo = PaymentDate.of(LocalDate.now().minusDays(5));
            Payment payment = Payment.create(fiveDaysAgo, validUserId, validSubscriptionId, validAmount);

            assertEquals(5, payment.daysOverdue());
        }

        /**
         * Verifies that daysOverdue() returns 0 when the payment is not overdue.
         */
        @Test
        @DisplayName("Should return 0 when payment is not overdue")
        void daysOverdue_whenNotOverdue_shouldReturnZero() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);

            assertEquals(0, payment.daysOverdue());
        }

        /**
         * Verifies that daysOverdue() returns 0 when the payment is due today.
         */
        @Test
        @DisplayName("Should return 0 when payment is due today")
        void daysOverdue_whenDueToday_shouldReturnZero() {
            Payment payment = Payment.create(today, validUserId, validSubscriptionId, validAmount);

            assertEquals(0, payment.daysOverdue());
        }

        /**
         * Verifies that daysOverdue() returns 0 for SUCCEEDED payments
         * even if the date is in the past.
         */
        @Test
        @DisplayName("Should return 0 when payment is SUCCEEDED")
        void daysOverdue_whenSucceeded_shouldReturnZero() {
            Payment payment = Payment.create(pastDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsSucceeded();

            assertEquals(0, payment.daysOverdue());
        }
    }

    // ==================== QUERY METHODS: daysUntilDue() (MEDIUM PRIORITY) ====================

    @Nested
    @DisplayName("Query Methods: daysUntilDue()")
    class DaysUntilDueTests {

        /**
         * Verifies that daysUntilDue() returns the correct number of days
         * for a PENDING payment with a future due date.
         */
        @Test
        @DisplayName("Should return correct days when PENDING and future date")
        void daysUntilDue_whenPendingAndFutureDate_shouldReturnCorrectDays() {
            PaymentDate tenDaysFromNow = PaymentDate.of(LocalDate.now().plusDays(10));
            Payment payment = Payment.create(tenDaysFromNow, validUserId, validSubscriptionId, validAmount);

            assertEquals(10, payment.daysUntilDue());
        }

        /**
         * Verifies that daysUntilDue() returns 0 when the payment is overdue.
         */
        @Test
        @DisplayName("Should return 0 when payment is overdue")
        void daysUntilDue_whenOverdue_shouldReturnZero() {
            Payment payment = Payment.create(pastDate, validUserId, validSubscriptionId, validAmount);

            assertEquals(0, payment.daysUntilDue());
        }

        /**
         * Verifies that daysUntilDue() returns 0 when the payment is due today.
         */
        @Test
        @DisplayName("Should return 0 when payment is due today")
        void daysUntilDue_whenDueToday_shouldReturnZero() {
            Payment payment = Payment.create(today, validUserId, validSubscriptionId, validAmount);

            assertEquals(0, payment.daysUntilDue());
        }

        /**
         * Verifies that daysUntilDue() returns 0 for non-PENDING payments.
         */
        @Test
        @DisplayName("Should return 0 when payment is SUCCEEDED")
        void daysUntilDue_whenSucceeded_shouldReturnZero() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsSucceeded();

            assertEquals(0, payment.daysUntilDue());
        }

        /**
         * Verifies that daysUntilDue() returns 0 for FAILED payments.
         */
        @Test
        @DisplayName("Should return 0 when payment is FAILED")
        void daysUntilDue_whenFailed_shouldReturnZero() {
            Payment payment = Payment.create(futureDate, validUserId, validSubscriptionId, validAmount);
            payment.markAsFailed();

            assertEquals(0, payment.daysUntilDue());
        }
    }
}


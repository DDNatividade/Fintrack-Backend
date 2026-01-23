package com.apis.fintrack.domain.payment;

import com.apis.fintrack.domain.payment.model.PaymentDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PaymentDate} value object.
 *
 * <p>Tests cover factory methods, equality contract, and edge cases
 * following specification-based and structural testing approaches.</p>
 */
@DisplayName("PaymentDate Value Object Tests")
class PaymentDateTest {

    // ==================== FACTORY METHOD: of() (HIGH PRIORITY) ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid PaymentDate
         * when provided with a valid LocalDate.
         */
        @Test
        @DisplayName("Should return PaymentDate when given a valid date")
        void of_withValidDate_shouldReturnPaymentDate() {
            LocalDate validDate = LocalDate.of(2024, 6, 15);

            PaymentDate result = PaymentDate.of(validDate);

            assertNotNull(result);
            assertEquals(validDate, result.getValue());
        }

        /**
         * Verifies that the factory method throws IllegalArgumentException
         * when provided with a null value. This ensures fail-fast behavior.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when date is null")
        void of_withNullDate_shouldThrowIllegalArgumentException() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentDate.of(null)
            );

            assertNotNull(exception.getMessage());
            assertFalse(exception.getMessage().isEmpty());
        }

        /**
         * Verifies that past dates are accepted by the factory method.
         * Business rule: payments can reference historical dates.
         */
        @Test
        @DisplayName("Should accept past dates")
        void of_withPastDate_shouldReturnPaymentDate() {
            LocalDate pastDate = LocalDate.of(2020, 1, 1);

            PaymentDate result = PaymentDate.of(pastDate);

            assertEquals(pastDate, result.getValue());
        }

        /**
         * Verifies that future dates are accepted by the factory method.
         * Business rule: payments can have scheduled future due dates.
         */
        @Test
        @DisplayName("Should accept future dates")
        void of_withFutureDate_shouldReturnPaymentDate() {
            LocalDate futureDate = LocalDate.of(2030, 12, 31);

            PaymentDate result = PaymentDate.of(futureDate);

            assertEquals(futureDate, result.getValue());
        }

        /**
         * Verifies that leap year dates (February 29) are handled correctly.
         */
        @Test
        @DisplayName("Should accept leap year date (Feb 29)")
        void of_withLeapYearDate_shouldReturnPaymentDate() {
            LocalDate leapYearDate = LocalDate.of(2024, Month.FEBRUARY, 29);

            PaymentDate result = PaymentDate.of(leapYearDate);

            assertEquals(leapYearDate, result.getValue());
        }
    }

    // ==================== FACTORY METHOD: now() (HIGH PRIORITY) ====================

    @Nested
    @DisplayName("Factory Method: now()")
    class NowFactoryTests {

        /**
         * Verifies that the now() factory method returns a PaymentDate
         * with the current system date.
         */
        @Test
        @DisplayName("Should return PaymentDate with current date")
        void now_shouldReturnPaymentDateWithCurrentDate() {
            LocalDate before = LocalDate.now();

            PaymentDate result = PaymentDate.now();

            LocalDate after = LocalDate.now();

            assertNotNull(result);
            // The result should be between before and after (inclusive)
            assertFalse(result.getValue().isBefore(before));
            assertFalse(result.getValue().isAfter(after));
        }
    }

    // ==================== FACTORY METHOD: fromStorage() (HIGH PRIORITY) ====================

    @Nested
    @DisplayName("Factory Method: fromStorage()")
    class FromStorageFactoryTests {

        /**
         * Verifies that fromStorage() creates a valid PaymentDate
         * when reconstructing from persisted data.
         */
        @Test
        @DisplayName("Should return PaymentDate when given valid stored date")
        void fromStorage_withValidDate_shouldReturnPaymentDate() {
            LocalDate storedDate = LocalDate.of(2023, 3, 20);

            PaymentDate result = PaymentDate.fromStorage(storedDate);

            assertNotNull(result);
            assertEquals(storedDate, result.getValue());
        }

        /**
         * Verifies that fromStorage() throws IllegalArgumentException
         * when the stored date is null. Ensures data integrity on rehydration.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when stored date is null")
        void fromStorage_withNullDate_shouldThrowIllegalArgumentException() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentDate.fromStorage(null)
            );

            assertNotNull(exception.getMessage());
            assertFalse(exception.getMessage().isEmpty());
        }

        /**
         * Verifies that of() and fromStorage() produce equivalent objects
         * when given the same date. Ensures consistency across factory methods.
         */
        @Test
        @DisplayName("of() and fromStorage() with same date should produce equal objects")
        void of_andFromStorage_withSameDate_shouldBeEqual() {
            LocalDate date = LocalDate.of(2024, 7, 10);

            PaymentDate fromOf = PaymentDate.of(date);
            PaymentDate fromStorage = PaymentDate.fromStorage(date);

            assertEquals(fromOf, fromStorage);
            assertEquals(fromOf.hashCode(), fromStorage.hashCode());
        }
    }

    // ==================== GETTER METHOD (MEDIUM PRIORITY) ====================

    @Nested
    @DisplayName("Getter: getValue()")
    class GetterTests {

        /**
         * Verifies that getValue() returns the exact LocalDate
         * that was used to create the PaymentDate.
         */
        @Test
        @DisplayName("Should return the internal LocalDate value")
        void getValue_shouldReturnInternalDate() {
            LocalDate expected = LocalDate.of(2025, 5, 5);
            PaymentDate paymentDate = PaymentDate.of(expected);

            LocalDate result = paymentDate.getValue();

            assertSame(expected, result);
        }
    }

    // ==================== EQUALITY CONTRACT (MEDIUM PRIORITY) ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two PaymentDate instances with the same date are equal.
         */
        @Test
        @DisplayName("equals() should return true for same date")
        void equals_withSameDate_shouldReturnTrue() {
            LocalDate date = LocalDate.of(2024, 8, 15);
            PaymentDate date1 = PaymentDate.of(date);
            PaymentDate date2 = PaymentDate.of(date);

            assertEquals(date1, date2);
        }

        /**
         * Verifies that two PaymentDate instances with different dates are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different dates")
        void equals_withDifferentDate_shouldReturnFalse() {
            PaymentDate date1 = PaymentDate.of(LocalDate.of(2024, 1, 1));
            PaymentDate date2 = PaymentDate.of(LocalDate.of(2024, 1, 2));

            assertNotEquals(date1, date2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         * This prevents NullPointerException.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            PaymentDate paymentDate = PaymentDate.of(LocalDate.of(2024, 1, 1));

            assertNotEquals(null, paymentDate);
        }

        /**
         * Verifies that equals() returns false when compared with a different type.
         * This prevents ClassCastException.
         */
        @Test
        @DisplayName("equals() should return false when compared with different type")
        void equals_withDifferentType_shouldReturnFalse() {
            PaymentDate paymentDate = PaymentDate.of(LocalDate.of(2024, 1, 1));
            String differentType = "2024-01-01";

            assertNotEquals(differentType, paymentDate);
        }

        /**
         * Verifies reflexivity: an object must equal itself.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            PaymentDate paymentDate = PaymentDate.of(LocalDate.of(2024, 1, 1));

            assertEquals(paymentDate, paymentDate);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         * This is required by the equals/hashCode contract.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_withSameDate_shouldReturnSameHash() {
            LocalDate date = LocalDate.of(2024, 9, 20);
            PaymentDate date1 = PaymentDate.of(date);
            PaymentDate date2 = PaymentDate.of(date);

            assertEquals(date1.hashCode(), date2.hashCode());
        }

        /**
         * Verifies that different objects produce different hashCodes (not guaranteed,
         * but expected for well-distributed hash functions).
         */
        @Test
        @DisplayName("hashCode() should likely differ for different dates")
        void hashCode_withDifferentDate_shouldReturnDifferentHash() {
            PaymentDate date1 = PaymentDate.of(LocalDate.of(2024, 1, 1));
            PaymentDate date2 = PaymentDate.of(LocalDate.of(2024, 12, 31));

            // Not strictly required, but expected for good hash distribution
            assertNotEquals(date1.hashCode(), date2.hashCode());
        }

        /**
         * Verifies hashCode consistency: multiple calls return the same value.
         */
        @Test
        @DisplayName("hashCode() should be consistent across multiple calls")
        void hashCode_multipleCalls_shouldBeConsistent() {
            PaymentDate paymentDate = PaymentDate.of(LocalDate.of(2024, 6, 15));

            int hash1 = paymentDate.hashCode();
            int hash2 = paymentDate.hashCode();
            int hash3 = paymentDate.hashCode();

            assertEquals(hash1, hash2);
            assertEquals(hash2, hash3);
        }
    }
}


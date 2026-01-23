package com.apis.fintrack.domain.transaction;

import com.apis.fintrack.domain.transaction.model.TransactionDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TransactionDate} value object.
 *
 * <p>Tests cover factory methods, validation rules (no future dates),
 * and equality contract following specification-based testing approaches.</p>
 */
@DisplayName("TransactionDate Value Object Tests")
class TransactionDateTest {

    // ==================== FACTORY METHOD: of() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid TransactionDate
         * with a past date.
         */
        @Test
        @DisplayName("Should return TransactionDate when given a past date")
        void of_withPastDate_shouldReturnTransactionDate() {
            LocalDate pastDate = LocalDate.of(2023, 6, 15);

            TransactionDate result = TransactionDate.of(pastDate);

            assertNotNull(result);
            assertEquals(pastDate, result.getValue());
        }

        /**
         * Verifies that today's date is accepted.
         */
        @Test
        @DisplayName("Should accept today's date")
        void of_withTodayDate_shouldReturnTransactionDate() {
            LocalDate today = LocalDate.now();

            TransactionDate result = TransactionDate.of(today);

            assertEquals(today, result.getValue());
        }

        /**
         * Verifies that null date throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when date is null")
        void of_withNullDate_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> TransactionDate.of(null)
            );
        }

        /**
         * Verifies that future dates throw IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when date is in the future")
        void of_withFutureDate_shouldThrowIllegalArgumentException() {
            LocalDate futureDate = LocalDate.now().plusDays(1);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> TransactionDate.of(futureDate)
            );
        }

        /**
         * Verifies that a date far in the past is accepted.
         */
        @Test
        @DisplayName("Should accept dates far in the past")
        void of_withVeryOldDate_shouldReturnTransactionDate() {
            LocalDate oldDate = LocalDate.of(1990, 1, 1);

            TransactionDate result = TransactionDate.of(oldDate);

            assertEquals(oldDate, result.getValue());
        }
    }

    // ==================== FACTORY METHOD: now() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: now()")
    class NowFactoryTests {

        /**
         * Verifies that now() returns a TransactionDate with the current date.
         */
        @Test
        @DisplayName("Should return TransactionDate with current date")
        void now_shouldReturnTransactionDateWithCurrentDate() {
            LocalDate before = LocalDate.now();

            TransactionDate result = TransactionDate.now();

            LocalDate after = LocalDate.now();

            assertNotNull(result);
            assertFalse(result.getValue().isBefore(before));
            assertFalse(result.getValue().isAfter(after));
        }
    }

    // ==================== FACTORY METHOD: fromStorage() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: fromStorage()")
    class FromStorageFactoryTests {

        /**
         * Verifies that fromStorage() creates a valid TransactionDate.
         */
        @Test
        @DisplayName("Should return TransactionDate when given valid stored date")
        void fromStorage_withValidDate_shouldReturnTransactionDate() {
            LocalDate storedDate = LocalDate.of(2023, 3, 20);

            TransactionDate result = TransactionDate.fromStorage(storedDate);

            assertNotNull(result);
            assertEquals(storedDate, result.getValue());
        }

        /**
         * Verifies that fromStorage() throws IllegalArgumentException for null.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when stored date is null")
        void fromStorage_withNullDate_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> TransactionDate.fromStorage(null)
            );
        }

        /**
         * Verifies that fromStorage() accepts future dates (for migrated data).
         */
        @Test
        @DisplayName("Should accept future dates from storage (migrated data)")
        void fromStorage_withFutureDate_shouldReturnTransactionDate() {
            LocalDate futureDate = LocalDate.now().plusYears(1);

            TransactionDate result = TransactionDate.fromStorage(futureDate);

            assertEquals(futureDate, result.getValue());
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two TransactionDate with same date are equal.
         */
        @Test
        @DisplayName("equals() should return true for same date")
        void equals_withSameDate_shouldReturnTrue() {
            LocalDate date = LocalDate.of(2024, 8, 15);
            TransactionDate date1 = TransactionDate.of(date);
            TransactionDate date2 = TransactionDate.of(date);

            assertEquals(date1, date2);
        }

        /**
         * Verifies that two TransactionDate with different dates are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different dates")
        void equals_withDifferentDate_shouldReturnFalse() {
            TransactionDate date1 = TransactionDate.of(LocalDate.of(2024, 1, 1));
            TransactionDate date2 = TransactionDate.of(LocalDate.of(2024, 1, 2));

            assertNotEquals(date1, date2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            TransactionDate transactionDate = TransactionDate.now();

            assertNotEquals(null, transactionDate);
        }

        /**
         * Verifies reflexivity.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            TransactionDate transactionDate = TransactionDate.now();

            assertEquals(transactionDate, transactionDate);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_equalObjects_shouldReturnSameHash() {
            LocalDate date = LocalDate.of(2024, 9, 20);
            TransactionDate date1 = TransactionDate.of(date);
            TransactionDate date2 = TransactionDate.of(date);

            assertEquals(date1.hashCode(), date2.hashCode());
        }

        /**
         * Verifies hashCode consistency.
         */
        @Test
        @DisplayName("hashCode() should be consistent across multiple calls")
        void hashCode_multipleCalls_shouldBeConsistent() {
            TransactionDate transactionDate = TransactionDate.now();

            int hash1 = transactionDate.hashCode();
            int hash2 = transactionDate.hashCode();

            assertEquals(hash1, hash2);
        }
    }
}


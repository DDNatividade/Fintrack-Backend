package com.apis.fintrack.domain.subscription;

import com.apis.fintrack.domain.subscription.model.SubscriptionDate;
import com.apis.fintrack.domain.subscription.model.SubscriptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SubscriptionDate} value object.
 *
 * <p>Tests cover factory methods, expiration logic, renewal calculations,
 * and equality contract following specification-based testing approaches.</p>
 */
@DisplayName("SubscriptionDate Value Object Tests")
class SubscriptionDateTest {

    // ==================== FACTORY METHODS - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid SubscriptionDate.
         */
        @Test
        @DisplayName("Should return SubscriptionDate when given a valid date")
        void of_withValidDate_shouldReturnSubscriptionDate() {
            LocalDate validDate = LocalDate.of(2024, 6, 15);

            SubscriptionDate result = SubscriptionDate.of(validDate);

            assertNotNull(result);
            assertEquals(validDate, result.getValue());
        }

        /**
         * Verifies that null date throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when date is null")
        void of_withNullDate_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> SubscriptionDate.of(null)
            );
        }

        /**
         * Verifies that past dates are accepted.
         */
        @Test
        @DisplayName("Should accept past dates")
        void of_withPastDate_shouldReturnSubscriptionDate() {
            LocalDate pastDate = LocalDate.of(2020, 1, 1);

            SubscriptionDate result = SubscriptionDate.of(pastDate);

            assertEquals(pastDate, result.getValue());
        }

        /**
         * Verifies that future dates are accepted.
         */
        @Test
        @DisplayName("Should accept future dates")
        void of_withFutureDate_shouldReturnSubscriptionDate() {
            LocalDate futureDate = LocalDate.of(2030, 12, 31);

            SubscriptionDate result = SubscriptionDate.of(futureDate);

            assertEquals(futureDate, result.getValue());
        }
    }

    @Nested
    @DisplayName("Factory Method: now()")
    class NowFactoryTests {

        /**
         * Verifies that now() returns a SubscriptionDate with the current date.
         */
        @Test
        @DisplayName("Should return SubscriptionDate with current date")
        void now_shouldReturnSubscriptionDateWithCurrentDate() {
            LocalDate before = LocalDate.now();

            SubscriptionDate result = SubscriptionDate.now();

            LocalDate after = LocalDate.now();

            assertNotNull(result);
            assertFalse(result.getValue().isBefore(before));
            assertFalse(result.getValue().isAfter(after));
        }
    }

    @Nested
    @DisplayName("Factory Method: fromStorage()")
    class FromStorageFactoryTests {

        /**
         * Verifies that fromStorage() creates a valid SubscriptionDate.
         */
        @Test
        @DisplayName("Should return SubscriptionDate when given valid stored date")
        void fromStorage_withValidDate_shouldReturnSubscriptionDate() {
            LocalDate storedDate = LocalDate.of(2023, 3, 20);

            SubscriptionDate result = SubscriptionDate.fromStorage(storedDate);

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
                    () -> SubscriptionDate.fromStorage(null)
            );
        }

        /**
         * Verifies that of() and fromStorage() produce equivalent objects.
         */
        @Test
        @DisplayName("of() and fromStorage() with same date should produce equal objects")
        void of_andFromStorage_withSameDate_shouldBeEqual() {
            LocalDate date = LocalDate.of(2024, 7, 10);

            SubscriptionDate fromOf = SubscriptionDate.of(date);
            SubscriptionDate fromStorage = SubscriptionDate.fromStorage(date);

            assertEquals(fromOf, fromStorage);
        }
    }

    // ==================== EXPIRATION LOGIC - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Expiration Methods")
    class ExpirationTests {

        /**
         * Verifies that getExpirationDate() returns correct date for MONTHLY.
         */
        @Test
        @DisplayName("getExpirationDate() should return +1 month for MONTHLY")
        void getExpirationDate_monthly_shouldReturnPlusOneMonth() {
            LocalDate startDate = LocalDate.of(2024, 1, 15);
            SubscriptionDate subscriptionDate = SubscriptionDate.of(startDate);

            LocalDate expiration = subscriptionDate.getExpirationDate(SubscriptionType.MONTHLY);

            assertEquals(LocalDate.of(2024, 2, 15), expiration);
        }

        /**
         * Verifies that getExpirationDate() returns correct date for ANNUAL.
         */
        @Test
        @DisplayName("getExpirationDate() should return +12 months for ANNUAL")
        void getExpirationDate_annual_shouldReturnPlusTwelveMonths() {
            LocalDate startDate = LocalDate.of(2024, 1, 15);
            SubscriptionDate subscriptionDate = SubscriptionDate.of(startDate);

            LocalDate expiration = subscriptionDate.getExpirationDate(SubscriptionType.ANNUAL);

            assertEquals(LocalDate.of(2025, 1, 15), expiration);
        }

        /**
         * Verifies that getExpirationDate() handles end of month correctly.
         */
        @Test
        @DisplayName("getExpirationDate() should handle end of month dates")
        void getExpirationDate_endOfMonth_shouldHandleCorrectly() {
            LocalDate startDate = LocalDate.of(2024, 1, 31);
            SubscriptionDate subscriptionDate = SubscriptionDate.of(startDate);

            LocalDate expiration = subscriptionDate.getExpirationDate(SubscriptionType.MONTHLY);

            // February doesn't have 31 days, so it should be Feb 29 (2024 is leap year)
            assertEquals(LocalDate.of(2024, 2, 29), expiration);
        }

        /**
         * Verifies that getExpirationDate() throws exception for null type.
         */
        @Test
        @DisplayName("getExpirationDate() should throw exception for null type")
        void getExpirationDate_nullType_shouldThrowException() {
            SubscriptionDate subscriptionDate = SubscriptionDate.of(LocalDate.now());

            assertThrows(NullPointerException.class,
                    () -> subscriptionDate.getExpirationDate(null));
        }

        /**
         * Verifies that isExpired() returns true for expired subscription.
         */
        @Test
        @DisplayName("isExpired() should return true for expired subscription")
        void isExpired_expiredSubscription_shouldReturnTrue() {
            LocalDate oldDate = LocalDate.now().minusMonths(2);
            SubscriptionDate subscriptionDate = SubscriptionDate.of(oldDate);

            assertTrue(subscriptionDate.isExpired(SubscriptionType.MONTHLY));
        }

        /**
         * Verifies that isExpired() returns false for active subscription.
         */
        @Test
        @DisplayName("isExpired() should return false for active subscription")
        void isExpired_activeSubscription_shouldReturnFalse() {
            LocalDate recentDate = LocalDate.now();
            SubscriptionDate subscriptionDate = SubscriptionDate.of(recentDate);

            assertFalse(subscriptionDate.isExpired(SubscriptionType.MONTHLY));
        }

        /**
         * Verifies that daysUntilExpiration() returns positive for active subscription.
         */
        @Test
        @DisplayName("daysUntilExpiration() should return positive for active subscription")
        void daysUntilExpiration_activeSubscription_shouldReturnPositive() {
            LocalDate recentDate = LocalDate.now();
            SubscriptionDate subscriptionDate = SubscriptionDate.of(recentDate);

            long days = subscriptionDate.daysUntilExpiration(SubscriptionType.MONTHLY);

            assertTrue(days > 0);
        }

        /**
         * Verifies that daysUntilExpiration() returns negative for expired subscription.
         */
        @Test
        @DisplayName("daysUntilExpiration() should return negative for expired subscription")
        void daysUntilExpiration_expiredSubscription_shouldReturnNegative() {
            LocalDate oldDate = LocalDate.now().minusMonths(2);
            SubscriptionDate subscriptionDate = SubscriptionDate.of(oldDate);

            long days = subscriptionDate.daysUntilExpiration(SubscriptionType.MONTHLY);

            assertTrue(days < 0);
        }
    }

    // ==================== ABOUT TO EXPIRE - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("About to Expire Methods")
    class AboutToExpireTests {

        /**
         * Verifies isAboutToExpire() returns true when within threshold.
         */
        @Test
        @DisplayName("isAboutToExpire() should return true when within threshold")
        void isAboutToExpire_withinThreshold_shouldReturnTrue() {
            // Start date such that expiration is 5 days from now
            LocalDate startDate = LocalDate.now().minusDays(25); // Monthly = +30 days, so ~5 days left
            SubscriptionDate subscriptionDate = SubscriptionDate.of(startDate);

            boolean result = subscriptionDate.isAboutToExpire(SubscriptionType.MONTHLY, 10);

            assertTrue(result);
        }

        /**
         * Verifies isAboutToExpire() returns false when already expired.
         */
        @Test
        @DisplayName("isAboutToExpire() should return false when already expired")
        void isAboutToExpire_alreadyExpired_shouldReturnFalse() {
            LocalDate oldDate = LocalDate.now().minusMonths(2);
            SubscriptionDate subscriptionDate = SubscriptionDate.of(oldDate);

            assertFalse(subscriptionDate.isAboutToExpire(SubscriptionType.MONTHLY, 10));
        }
    }

    // ==================== RENEWAL DATE - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Renewal Date Methods")
    class RenewalDateTests {

        /**
         * Verifies getNextRenewalDate() returns expiration date for active subscription.
         */
        @Test
        @DisplayName("getNextRenewalDate() should return expiration date for active subscription")
        void getNextRenewalDate_activeSubscription_shouldReturnExpirationDate() {
            LocalDate recentDate = LocalDate.now();
            SubscriptionDate subscriptionDate = SubscriptionDate.of(recentDate);

            LocalDate renewalDate = subscriptionDate.getNextRenewalDate(SubscriptionType.MONTHLY);
            LocalDate expectedExpiration = subscriptionDate.getExpirationDate(SubscriptionType.MONTHLY);

            assertEquals(expectedExpiration, renewalDate);
        }

        /**
         * Verifies getNextRenewalDate() returns today for expired subscription.
         */
        @Test
        @DisplayName("getNextRenewalDate() should return today for expired subscription")
        void getNextRenewalDate_expiredSubscription_shouldReturnToday() {
            LocalDate oldDate = LocalDate.now().minusMonths(2);
            SubscriptionDate subscriptionDate = SubscriptionDate.of(oldDate);

            LocalDate renewalDate = subscriptionDate.getNextRenewalDate(SubscriptionType.MONTHLY);

            assertEquals(LocalDate.now(), renewalDate);
        }
    }

    // ==================== COMPLETED PERIODS - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Completed Periods Methods")
    class CompletedPeriodsTests {

        /**
         * Verifies getCompletedPeriods() for MONTHLY subscription.
         */
        @Test
        @DisplayName("getCompletedPeriods() should return correct count for MONTHLY")
        void getCompletedPeriods_monthly_shouldReturnCorrectCount() {
            LocalDate startDate = LocalDate.now().minusMonths(5);
            SubscriptionDate subscriptionDate = SubscriptionDate.of(startDate);

            long periods = subscriptionDate.getCompletedPeriods(SubscriptionType.MONTHLY);

            assertEquals(5, periods);
        }

        /**
         * Verifies getCompletedPeriods() for ANNUAL subscription.
         */
        @Test
        @DisplayName("getCompletedPeriods() should return correct count for ANNUAL")
        void getCompletedPeriods_annual_shouldReturnCorrectCount() {
            LocalDate startDate = LocalDate.now().minusYears(2).minusMonths(6);
            SubscriptionDate subscriptionDate = SubscriptionDate.of(startDate);

            long periods = subscriptionDate.getCompletedPeriods(SubscriptionType.ANNUAL);

            assertEquals(2, periods);
        }

        /**
         * Verifies getCompletedPeriods() returns 0 for new subscription.
         */
        @Test
        @DisplayName("getCompletedPeriods() should return 0 for new subscription")
        void getCompletedPeriods_newSubscription_shouldReturnZero() {
            SubscriptionDate subscriptionDate = SubscriptionDate.now();

            long periods = subscriptionDate.getCompletedPeriods(SubscriptionType.MONTHLY);

            assertEquals(0, periods);
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two SubscriptionDate with same date are equal.
         */
        @Test
        @DisplayName("equals() should return true for same date")
        void equals_withSameDate_shouldReturnTrue() {
            LocalDate date = LocalDate.of(2024, 8, 15);
            SubscriptionDate date1 = SubscriptionDate.of(date);
            SubscriptionDate date2 = SubscriptionDate.of(date);

            assertEquals(date1, date2);
        }

        /**
         * Verifies that two SubscriptionDate with different dates are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different dates")
        void equals_withDifferentDate_shouldReturnFalse() {
            SubscriptionDate date1 = SubscriptionDate.of(LocalDate.of(2024, 1, 1));
            SubscriptionDate date2 = SubscriptionDate.of(LocalDate.of(2024, 1, 2));

            assertNotEquals(date1, date2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            SubscriptionDate subscriptionDate = SubscriptionDate.of(LocalDate.now());

            assertNotEquals(null, subscriptionDate);
        }

        /**
         * Verifies reflexivity.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            SubscriptionDate subscriptionDate = SubscriptionDate.of(LocalDate.now());

            assertEquals(subscriptionDate, subscriptionDate);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_equalObjects_shouldReturnSameHash() {
            LocalDate date = LocalDate.of(2024, 9, 20);
            SubscriptionDate date1 = SubscriptionDate.of(date);
            SubscriptionDate date2 = SubscriptionDate.of(date);

            assertEquals(date1.hashCode(), date2.hashCode());
        }

        /**
         * Verifies hashCode consistency.
         */
        @Test
        @DisplayName("hashCode() should be consistent across multiple calls")
        void hashCode_multipleCalls_shouldBeConsistent() {
            SubscriptionDate subscriptionDate = SubscriptionDate.of(LocalDate.now());

            int hash1 = subscriptionDate.hashCode();
            int hash2 = subscriptionDate.hashCode();

            assertEquals(hash1, hash2);
        }
    }
}


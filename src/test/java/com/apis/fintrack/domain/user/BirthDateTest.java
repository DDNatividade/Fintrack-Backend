package com.apis.fintrack.domain.user;

import com.apis.fintrack.domain.user.model.BirthDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link BirthDate} value object.
 *
 * <p>Tests cover factory methods, age validation (minimum 18, maximum 120),
 * age calculation, and equality contract following specification-based testing approaches.</p>
 */
@DisplayName("BirthDate Value Object Tests")
class BirthDateTest {

    // ==================== FACTORY METHOD: of() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid BirthDate for adult.
         */
        @Test
        @DisplayName("Should return BirthDate for valid adult date")
        void of_withValidAdultDate_shouldReturnBirthDate() {
            LocalDate adultDate = LocalDate.now().minusYears(25);

            BirthDate result = BirthDate.of(adultDate);

            assertNotNull(result);
            assertEquals(adultDate, result.getValue());
        }

        /**
         * Verifies that null date throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when date is null")
        void of_withNullDate_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> BirthDate.of(null)
            );
        }

        /**
         * Verifies that future date throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when date is in the future")
        void of_withFutureDate_shouldThrowIllegalArgumentException() {
            LocalDate futureDate = LocalDate.now().plusDays(1);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> BirthDate.of(futureDate)
            );
        }

        /**
         * Verifies that under 18 years old throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when age is under 18")
        void of_underMinimumAge_shouldThrowIllegalArgumentException() {
            LocalDate underageDate = LocalDate.now().minusYears(17);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> BirthDate.of(underageDate)
            );
        }

        /**
         * Verifies that exactly 18 years old is accepted.
         */
        @Test
        @DisplayName("Should accept exactly 18 years old")
        void of_exactly18YearsOld_shouldReturnBirthDate() {
            LocalDate eighteenthBirthday = LocalDate.now().minusYears(18);

            BirthDate result = BirthDate.of(eighteenthBirthday);

            assertNotNull(result);
        }

        /**
         * Verifies that over 120 years old throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when age is over 120")
        void of_overMaximumAge_shouldThrowIllegalArgumentException() {
            LocalDate tooOldDate = LocalDate.now().minusYears(121);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> BirthDate.of(tooOldDate)
            );
        }

        /**
         * Verifies that exactly 120 years old is accepted.
         */
        @Test
        @DisplayName("Should accept exactly 120 years old")
        void of_exactly120YearsOld_shouldReturnBirthDate() {
            LocalDate oldDate = LocalDate.now().minusYears(120);

            BirthDate result = BirthDate.of(oldDate);

            assertNotNull(result);
        }
    }

    // ==================== FACTORY METHOD: ofUnchecked() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: ofUnchecked()")
    class OfUncheckedFactoryTests {

        /**
         * Verifies that ofUnchecked() allows underage dates (for migration).
         */
        @Test
        @DisplayName("Should allow underage dates for migration purposes")
        void ofUnchecked_underageDate_shouldReturnBirthDate() {
            LocalDate underageDate = LocalDate.now().minusYears(10);

            BirthDate result = BirthDate.ofUnchecked(underageDate);

            assertNotNull(result);
            assertEquals(underageDate, result.getValue());
        }

        /**
         * Verifies that ofUnchecked() throws for null.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when date is null")
        void ofUnchecked_nullDate_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> BirthDate.ofUnchecked(null)
            );
        }
    }

    // ==================== AGE CALCULATION - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Age Calculation: getAge()")
    class AgeCalculationTests {

        /**
         * Verifies that getAge() returns correct age.
         */
        @Test
        @DisplayName("Should return correct age in years")
        void getAge_shouldReturnCorrectAge() {
            LocalDate thirtyYearsAgo = LocalDate.now().minusYears(30);
            BirthDate birthDate = BirthDate.of(thirtyYearsAgo);

            int age = birthDate.getAge();

            assertEquals(30, age);
        }

        /**
         * Verifies that getAge() handles birthday that hasn't occurred yet this year.
         */
        @Test
        @DisplayName("Should calculate age correctly before birthday this year")
        void getAge_beforeBirthdayThisYear_shouldReturnCorrectAge() {
            // If today is January 15, and birthday is December 25 (25 years ago)
            // The person is still 24 until December 25
            LocalDate birthDateValue = LocalDate.now().minusYears(25).plusMonths(6);

            // Only test if the date is in the future this year (birthday hasn't happened)
            if (birthDateValue.getMonthValue() > LocalDate.now().getMonthValue() ||
                (birthDateValue.getMonthValue() == LocalDate.now().getMonthValue() &&
                 birthDateValue.getDayOfMonth() > LocalDate.now().getDayOfMonth())) {

                BirthDate birthDate = BirthDate.ofUnchecked(birthDateValue);
                int age = birthDate.getAge();

                assertEquals(24, age);
            }
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two BirthDate with same date are equal.
         */
        @Test
        @DisplayName("equals() should return true for same date")
        void equals_withSameDate_shouldReturnTrue() {
            LocalDate date = LocalDate.of(1990, 5, 15);
            BirthDate date1 = BirthDate.of(date);
            BirthDate date2 = BirthDate.of(date);

            assertEquals(date1, date2);
        }

        /**
         * Verifies that two BirthDate with different dates are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different dates")
        void equals_withDifferentDate_shouldReturnFalse() {
            BirthDate date1 = BirthDate.of(LocalDate.of(1990, 5, 15));
            BirthDate date2 = BirthDate.of(LocalDate.of(1990, 5, 16));

            assertNotEquals(date1, date2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            BirthDate birthDate = BirthDate.of(LocalDate.of(1990, 5, 15));

            assertNotEquals(null, birthDate);
        }

        /**
         * Verifies reflexivity.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            BirthDate birthDate = BirthDate.of(LocalDate.of(1990, 5, 15));

            assertEquals(birthDate, birthDate);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_equalObjects_shouldReturnSameHash() {
            LocalDate date = LocalDate.of(1990, 5, 15);
            BirthDate date1 = BirthDate.of(date);
            BirthDate date2 = BirthDate.of(date);

            assertEquals(date1.hashCode(), date2.hashCode());
        }

        /**
         * Verifies hashCode consistency.
         */
        @Test
        @DisplayName("hashCode() should be consistent across multiple calls")
        void hashCode_multipleCalls_shouldBeConsistent() {
            BirthDate birthDate = BirthDate.of(LocalDate.of(1990, 5, 15));

            int hash1 = birthDate.hashCode();
            int hash2 = birthDate.hashCode();

            assertEquals(hash1, hash2);
        }
    }
}


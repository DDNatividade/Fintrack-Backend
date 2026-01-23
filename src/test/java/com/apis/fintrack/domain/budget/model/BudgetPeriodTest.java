package com.apis.fintrack.domain.budget.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BudgetPeriod Value Object.
 *
 * Tests follow specification-based testing approach, covering:
 * - Constructor with LocalDate validation
 * - Handling of months with different number of days
 * - Default constructor (current month)
 * - nextPeriod() immutability and correctness
 * - Equality and hashCode contracts
 *
 * BudgetPeriod is an immutable Value Object that represents a monthly budget period.
 * It calculates the start (first day) and end (last day) of a month based on a reference date.
 */
@DisplayName("BudgetPeriod Value Object Tests")
class BudgetPeriodTest {

    // ========================================
    // PRIORITY CRITICAL - Constructor with LocalDate
    // ========================================

    @Nested
    @DisplayName("Constructor with LocalDate")
    class ConstructorWithLocalDateTests {

        /**
         * Verifies that creating a BudgetPeriod with null reference throws NullPointerException.
         * <p>
         * Business Rule: A budget period must always have a valid reference date.
         * <p>
         * Bug it could reveal: Missing null validation in constructor.
         */
        @Test
        @DisplayName("constructor with null reference throws NullPointerException")
        void constructor_nullReference_throwsNPE() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new BudgetPeriod(null)
            );

            assertEquals("reference date cannot be null", exception.getMessage(),
                    "Exception message should indicate null reference date");
        }

        /**
         * Verifies that a mid-month date correctly sets startDate to first day of month.
         * <p>
         * Example: reference = 2026-01-15 → startDate = 2026-01-01
         * <p>
         * Bug it could reveal: startDate not calculated correctly from reference.
         */
        @Test
        @DisplayName("constructor with mid-month date sets startDate to first day")
        void constructor_midMonth_setsStartToFirstDay() {
            LocalDate midMonth = LocalDate.of(2026, 1, 15);

            BudgetPeriod period = new BudgetPeriod(midMonth);

            assertEquals(LocalDate.of(2026, 1, 1), period.getStartDate(),
                    "Start date should be first day of the month");
        }

        /**
         * Verifies that a mid-month date correctly sets endDate to last day of month.
         * <p>
         * Example: reference = 2026-01-15 → endDate = 2026-01-31
         * <p>
         * Bug it could reveal: endDate not calculated correctly from reference.
         */
        @Test
        @DisplayName("constructor with mid-month date sets endDate to last day")
        void constructor_midMonth_setsEndToLastDay() {
            LocalDate midMonth = LocalDate.of(2026, 1, 15);

            BudgetPeriod period = new BudgetPeriod(midMonth);

            assertEquals(LocalDate.of(2026, 1, 31), period.getEndDate(),
                    "End date should be last day of January (31)");
        }

        /**
         * Verifies correct period when reference is first day of month.
         * <p>
         * Boundary test: first day should still produce correct period.
         * <p>
         * Bug it could reveal: Off-by-one error at month start.
         */
        @Test
        @DisplayName("constructor with first day of month sets correct period")
        void constructor_firstDayOfMonth_setsCorrectPeriod() {
            LocalDate firstDay = LocalDate.of(2026, 3, 1);

            BudgetPeriod period = new BudgetPeriod(firstDay);

            assertEquals(LocalDate.of(2026, 3, 1), period.getStartDate(),
                    "Start date should be first day");
            assertEquals(LocalDate.of(2026, 3, 31), period.getEndDate(),
                    "End date should be March 31");
        }

        /**
         * Verifies correct period when reference is last day of month.
         * <p>
         * Boundary test: last day should still produce correct period.
         * <p>
         * Bug it could reveal: Off-by-one error at month end.
         */
        @Test
        @DisplayName("constructor with last day of month sets correct period")
        void constructor_lastDayOfMonth_setsCorrectPeriod() {
            LocalDate lastDay = LocalDate.of(2026, 3, 31);

            BudgetPeriod period = new BudgetPeriod(lastDay);

            assertEquals(LocalDate.of(2026, 3, 1), period.getStartDate(),
                    "Start date should be first day");
            assertEquals(LocalDate.of(2026, 3, 31), period.getEndDate(),
                    "End date should be March 31");
        }
    }

    // ========================================
    // PRIORITY CRITICAL - Months with Different Days
    // ========================================

    @Nested
    @DisplayName("Constructor - Months with Different Number of Days")
    class MonthsWithDifferentDaysTests {

        /**
         * Verifies correct end date for a 31-day month (January).
         * <p>
         * Bug it could reveal: Incorrect calculation for 31-day months.
         */
        @Test
        @DisplayName("constructor with January (31 days) ends on day 31")
        void constructor_january31Days_endsOnDay31() {
            LocalDate january = LocalDate.of(2026, 1, 10);

            BudgetPeriod period = new BudgetPeriod(january);

            assertEquals(LocalDate.of(2026, 1, 31), period.getEndDate(),
                    "January should end on day 31");
        }

        /**
         * Verifies correct end date for a 30-day month (April).
         * <p>
         * Bug it could reveal: Incorrect calculation for 30-day months.
         */
        @Test
        @DisplayName("constructor with April (30 days) ends on day 30")
        void constructor_april30Days_endsOnDay30() {
            LocalDate april = LocalDate.of(2026, 4, 15);

            BudgetPeriod period = new BudgetPeriod(april);

            assertEquals(LocalDate.of(2026, 4, 30), period.getEndDate(),
                    "April should end on day 30");
        }

        /**
         * Verifies correct end date for February in a non-leap year (28 days).
         * <p>
         * 2026 is not a leap year.
         * <p>
         * Bug it could reveal: Incorrect calculation for February non-leap.
         */
        @Test
        @DisplayName("constructor with February non-leap year ends on day 28")
        void constructor_februaryNonLeap_endsOnDay28() {
            LocalDate februaryNonLeap = LocalDate.of(2026, 2, 10);

            BudgetPeriod period = new BudgetPeriod(februaryNonLeap);

            assertEquals(LocalDate.of(2026, 2, 28), period.getEndDate(),
                    "February 2026 (non-leap) should end on day 28");
        }

        /**
         * Verifies correct end date for February in a leap year (29 days).
         * <p>
         * 2024 is a leap year.
         * <p>
         * Bug it could reveal: Incorrect calculation for February leap year.
         */
        @Test
        @DisplayName("constructor with February leap year ends on day 29")
        void constructor_februaryLeapYear_endsOnDay29() {
            LocalDate februaryLeap = LocalDate.of(2024, 2, 15);

            BudgetPeriod period = new BudgetPeriod(februaryLeap);

            assertEquals(LocalDate.of(2024, 2, 29), period.getEndDate(),
                    "February 2024 (leap year) should end on day 29");
        }
    }

    // ========================================
    // PRIORITY HIGH - Default Constructor
    // ========================================

    @Nested
    @DisplayName("Default Constructor (Current Month)")
    class DefaultConstructorTests {

        /**
         * Verifies that default constructor uses current month.
         * <p>
         * Note: This test may be fragile if run exactly at midnight on month transition.
         * <p>
         * Bug it could reveal: Default constructor doesn't use LocalDate.now().
         */
        @Test
        @DisplayName("default constructor uses current month")
        void defaultConstructor_usesCurrentMonth() {
            LocalDate now = LocalDate.now();
            LocalDate expectedStart = now.withDayOfMonth(1);

            BudgetPeriod period = new BudgetPeriod();

            assertEquals(expectedStart, period.getStartDate(),
                    "Default constructor should use current month");
        }

        /**
         * Verifies that default constructor starts on first day of current month.
         * <p>
         * Bug it could reveal: Start date not set to first day.
         */
        @Test
        @DisplayName("default constructor starts on first day of current month")
        void defaultConstructor_startsOnFirstDayOfCurrentMonth() {
            BudgetPeriod period = new BudgetPeriod();

            assertEquals(1, period.getStartDate().getDayOfMonth(),
                    "Start date should be day 1");
        }

        /**
         * Verifies that default constructor ends on last day of current month.
         * <p>
         * Bug it could reveal: End date not calculated correctly.
         */
        @Test
        @DisplayName("default constructor ends on last day of current month")
        void defaultConstructor_endsOnLastDayOfCurrentMonth() {
            LocalDate now = LocalDate.now();
            LocalDate expectedEnd = now.withDayOfMonth(1).plusMonths(1).minusDays(1);

            BudgetPeriod period = new BudgetPeriod();

            assertEquals(expectedEnd, period.getEndDate(),
                    "End date should be last day of current month");
        }
    }

    // ========================================
    // PRIORITY HIGH - nextPeriod()
    // ========================================

    @Nested
    @DisplayName("nextPeriod() - Period Advancement")
    class NextPeriodTests {

        /**
         * Verifies that nextPeriod() returns a new instance (immutability).
         * <p>
         * Bug it could reveal: Returns same instance or mutates original.
         */
        @Test
        @DisplayName("nextPeriod returns new instance")
        void nextPeriod_returnsNewInstance() {
            BudgetPeriod original = new BudgetPeriod(LocalDate.of(2026, 1, 15));

            BudgetPeriod next = original.nextPeriod();

            assertNotSame(original, next, "nextPeriod should return a new instance");
        }

        /**
         * Verifies that nextPeriod() advances exactly one month.
         * <p>
         * Example: January → February
         * <p>
         * Bug it could reveal: Doesn't advance or advances wrong amount.
         */
        @Test
        @DisplayName("nextPeriod advances one month")
        void nextPeriod_advancesOneMonth() {
            BudgetPeriod january = new BudgetPeriod(LocalDate.of(2026, 1, 15));

            BudgetPeriod february = january.nextPeriod();

            assertEquals(LocalDate.of(2026, 2, 1), february.getStartDate(),
                    "Next period should start on February 1");
            assertEquals(LocalDate.of(2026, 2, 28), february.getEndDate(),
                    "Next period should end on February 28 (2026 non-leap)");
        }

        /**
         * Verifies that nextPeriod() correctly handles year transition.
         * <p>
         * Example: December 2026 → January 2027
         * <p>
         * Bug it could reveal: Year doesn't increment on December → January.
         */
        @Test
        @DisplayName("nextPeriod from December advances to January of next year")
        void nextPeriod_decemberToJanuary() {
            BudgetPeriod december = new BudgetPeriod(LocalDate.of(2026, 12, 15));

            BudgetPeriod january = december.nextPeriod();

            assertEquals(LocalDate.of(2027, 1, 1), january.getStartDate(),
                    "Next period should be January 2027");
            assertEquals(LocalDate.of(2027, 1, 31), january.getEndDate(),
                    "January should end on day 31");
        }

        /**
         * Verifies that original period is unchanged after calling nextPeriod().
         * <p>
         * Immutability test: original must not be mutated.
         * <p>
         * Bug it could reveal: nextPeriod() mutates the original instance.
         */
        @Test
        @DisplayName("nextPeriod does not modify original period")
        void nextPeriod_originalUnchanged() {
            LocalDate originalStart = LocalDate.of(2026, 3, 1);
            LocalDate originalEnd = LocalDate.of(2026, 3, 31);
            BudgetPeriod original = new BudgetPeriod(LocalDate.of(2026, 3, 15));

            original.nextPeriod(); // Call but ignore result

            assertEquals(originalStart, original.getStartDate(),
                    "Original start date should be unchanged");
            assertEquals(originalEnd, original.getEndDate(),
                    "Original end date should be unchanged");
        }

        /**
         * Verifies that multiple nextPeriod() calls chain correctly.
         * <p>
         * Example: January → February → March → April
         * <p>
         * Bug it could reveal: Only works once or accumulates incorrectly.
         */
        @Test
        @DisplayName("nextPeriod multiple calls chain correctly")
        void nextPeriod_multipleCalls_chainCorrectly() {
            BudgetPeriod january = new BudgetPeriod(LocalDate.of(2026, 1, 15));

            BudgetPeriod february = january.nextPeriod();
            BudgetPeriod march = february.nextPeriod();
            BudgetPeriod april = march.nextPeriod();

            assertEquals(LocalDate.of(2026, 2, 1), february.getStartDate(),
                    "Second period should be February");
            assertEquals(LocalDate.of(2026, 3, 1), march.getStartDate(),
                    "Third period should be March");
            assertEquals(LocalDate.of(2026, 4, 1), april.getStartDate(),
                    "Fourth period should be April");
        }
    }

    // ========================================
    // PRIORITY MEDIUM - Getters
    // ========================================

    @Nested
    @DisplayName("Getters")
    class GetterTests {

        /**
         * Verifies that getStartDate() returns the correct start date.
         * <p>
         * Bug it could reveal: Getter returns wrong field.
         */
        @Test
        @DisplayName("getStartDate returns correct date")
        void getStartDate_returnsCorrectDate() {
            BudgetPeriod period = new BudgetPeriod(LocalDate.of(2026, 5, 20));

            LocalDate startDate = period.getStartDate();

            assertEquals(LocalDate.of(2026, 5, 1), startDate,
                    "getStartDate should return first day of month");
        }

        /**
         * Verifies that getEndDate() returns the correct end date.
         * <p>
         * Bug it could reveal: Getter returns wrong field.
         */
        @Test
        @DisplayName("getEndDate returns correct date")
        void getEndDate_returnsCorrectDate() {
            BudgetPeriod period = new BudgetPeriod(LocalDate.of(2026, 5, 20));

            LocalDate endDate = period.getEndDate();

            assertEquals(LocalDate.of(2026, 5, 31), endDate,
                    "getEndDate should return last day of May (31)");
        }
    }

    // ========================================
    // PRIORITY MEDIUM - Equality and HashCode
    // ========================================

    @Nested
    @DisplayName("Equality and HashCode")
    class EqualityTests {

        /**
         * Verifies that two BudgetPeriods with the same dates are equal.
         * <p>
         * Bug it could reveal: equals() compares references instead of values.
         */
        @Test
        @DisplayName("equals with same period returns true")
        void equals_samePeriod_returnsTrue() {
            BudgetPeriod period1 = new BudgetPeriod(LocalDate.of(2026, 6, 10));
            BudgetPeriod period2 = new BudgetPeriod(LocalDate.of(2026, 6, 25));

            assertEquals(period1, period2,
                    "Periods from same month should be equal");
        }

        /**
         * Verifies that two BudgetPeriods with different months are not equal.
         * <p>
         * Bug it could reveal: equals() always returns true.
         */
        @Test
        @DisplayName("equals with different period returns false")
        void equals_differentPeriod_returnsFalse() {
            BudgetPeriod january = new BudgetPeriod(LocalDate.of(2026, 1, 15));
            BudgetPeriod february = new BudgetPeriod(LocalDate.of(2026, 2, 15));

            assertNotEquals(january, february,
                    "Periods from different months should not be equal");
        }

        /**
         * Verifies reflexivity: a period equals itself.
         * <p>
         * Bug it could reveal: Reflexivity contract violated.
         */
        @Test
        @DisplayName("equals with same instance returns true (reflexivity)")
        void equals_sameInstance_returnsTrue() {
            BudgetPeriod period = new BudgetPeriod(LocalDate.of(2026, 7, 15));

            assertEquals(period, period, "Period should equal itself");
        }

        /**
         * Verifies that comparing with null returns false.
         * <p>
         * Bug it could reveal: NPE when comparing with null.
         */
        @Test
        @DisplayName("equals with null returns false")
        void equals_null_returnsFalse() {
            BudgetPeriod period = new BudgetPeriod(LocalDate.of(2026, 8, 15));

            assertNotEquals(null, period, "Period should not equal null");
        }

        /**
         * Verifies that comparing with different type returns false.
         * <p>
         * Bug it could reveal: ClassCastException.
         */
        @Test
        @DisplayName("equals with different type returns false")
        void equals_differentType_returnsFalse() {
            BudgetPeriod period = new BudgetPeriod(LocalDate.of(2026, 9, 15));
            String differentType = "2026-09";

            assertNotEquals(differentType, period,
                    "Period should not equal different type");
        }

        /**
         * Verifies that equal periods have the same hashCode.
         * <p>
         * This is required by the hashCode contract.
         * <p>
         * Bug it could reveal: hashCode contract violated.
         */
        @Test
        @DisplayName("hashCode same for equal periods")
        void hashCode_equalPeriods_sameHash() {
            BudgetPeriod period1 = new BudgetPeriod(LocalDate.of(2026, 10, 5));
            BudgetPeriod period2 = new BudgetPeriod(LocalDate.of(2026, 10, 20));

            assertEquals(period1.hashCode(), period2.hashCode(),
                    "Equal periods must have same hashCode");
        }

        /**
         * Verifies that different periods likely have different hashCodes.
         * <p>
         * Note: Different values CAN have same hash (collision), but shouldn't always.
         * <p>
         * Bug it could reveal: Poor hash distribution (constant hash).
         */
        @Test
        @DisplayName("hashCode different for different periods (likely)")
        void hashCode_differentPeriods_likelyDifferentHash() {
            BudgetPeriod period1 = new BudgetPeriod(LocalDate.of(2026, 11, 15));
            BudgetPeriod period2 = new BudgetPeriod(LocalDate.of(2026, 12, 15));

            assertNotEquals(period1.hashCode(), period2.hashCode(),
                    "Different periods should likely have different hashCodes");
        }
    }

    // ========================================
    // PRIORITY MEDIUM - ToString
    // ========================================

    @Nested
    @DisplayName("ToString")
    class ToStringTests {

        /**
         * Verifies that toString() contains the start date.
         * <p>
         * Bug it could reveal: toString() doesn't include startDate.
         */
        @Test
        @DisplayName("toString contains startDate")
        void toString_containsStartDate() {
            BudgetPeriod period = new BudgetPeriod(LocalDate.of(2026, 6, 15));

            String result = period.toString();

            assertTrue(result.contains("2026-06-01"),
                    "toString should contain startDate");
        }

        /**
         * Verifies that toString() contains the end date.
         * <p>
         * Bug it could reveal: toString() doesn't include endDate.
         */
        @Test
        @DisplayName("toString contains endDate")
        void toString_containsEndDate() {
            BudgetPeriod period = new BudgetPeriod(LocalDate.of(2026, 6, 15));

            String result = period.toString();

            assertTrue(result.contains("2026-06-30"),
                    "toString should contain endDate");
        }
    }
}


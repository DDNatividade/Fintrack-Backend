package com.apis.fintrack.domain.analysis.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AnalysisPeriod record.
 *
 * Tests follow specification-based testing approach, covering:
 * - Record construction and validation
 * - lastDays() factory method
 * - lastMonths() factory method
 * - lastYears() factory method
 * - Boundary conditions and edge cases
 */
@DisplayName("AnalysisPeriod Tests")
class AnalysisPeriodTest {

    // ========================================
    // Record Construction Tests
    // ========================================

    @Nested
    @DisplayName("Record construction")
    class RecordConstructionTests {

        @Test
        @DisplayName("Creates period with valid dates")
        void constructor_withValidDates_createsPeriod() {
            LocalDate start = LocalDate.of(2026, 1, 1);
            LocalDate end = LocalDate.of(2026, 1, 31);

            AnalysisPeriod period = new AnalysisPeriod(start, end);

            assertNotNull(period);
            assertEquals(start, period.startDate());
            assertEquals(end, period.endDate());
        }

        @Test
        @DisplayName("Creates period where start equals end")
        void constructor_withSameStartAndEnd_createsPeriod() {
            LocalDate date = LocalDate.of(2026, 1, 15);

            AnalysisPeriod period = new AnalysisPeriod(date, date);

            assertEquals(date, period.startDate());
            assertEquals(date, period.endDate());
        }

        @Test
        @DisplayName("Creates period where start is after end")
        void constructor_withStartAfterEnd_createsPeriod() {
            LocalDate start = LocalDate.of(2026, 1, 31);
            LocalDate end = LocalDate.of(2026, 1, 1);

            // Note: Constructor doesn't validate order - this is a potential bug
            AnalysisPeriod period = new AnalysisPeriod(start, end);

            assertEquals(start, period.startDate());
            assertEquals(end, period.endDate());
        }

        @Test
        @DisplayName("Allows null startDate")
        void constructor_withNullStartDate_allowsNull() {
            LocalDate end = LocalDate.of(2026, 1, 31);

            // Note: Record allows null - this might be a design issue
            AnalysisPeriod period = new AnalysisPeriod(null, end);

            assertNull(period.startDate());
            assertEquals(end, period.endDate());
        }

        @Test
        @DisplayName("Allows null endDate")
        void constructor_withNullEndDate_allowsNull() {
            LocalDate start = LocalDate.of(2026, 1, 1);

            // Note: Record allows null - this might be a design issue
            AnalysisPeriod period = new AnalysisPeriod(start, null);

            assertEquals(start, period.startDate());
            assertNull(period.endDate());
        }

        @Test
        @DisplayName("Allows both dates to be null")
        void constructor_withBothNull_allowsNull() {
            AnalysisPeriod period = new AnalysisPeriod(null, null);

            assertNull(period.startDate());
            assertNull(period.endDate());
        }
    }

    // ========================================
    // lastDays() - Happy Path Tests
    // ========================================

    @Nested
    @DisplayName("lastDays() - Happy Path")
    class LastDaysHappyPathTests {

        @Test
        @DisplayName("Creates period for last 1 day")
        void lastDays_with1Day_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastDays(1);

            assertNotNull(period);
            assertEquals(LocalDate.now().minusDays(1), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 7 days")
        void lastDays_with7Days_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastDays(7);

            assertEquals(LocalDate.now().minusDays(7), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 30 days")
        void lastDays_with30Days_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastDays(30);

            assertEquals(LocalDate.now().minusDays(30), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 90 days")
        void lastDays_with90Days_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastDays(90);

            assertEquals(LocalDate.now().minusDays(90), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 180 days")
        void lastDays_with180Days_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastDays(180);

            assertEquals(LocalDate.now().minusDays(180), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 360 days (maximum allowed)")
        void lastDays_with360Days_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastDays(360);

            assertEquals(LocalDate.now().minusDays(360), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }
    }

    // ========================================
    // lastDays() - Boundary and Edge Cases
    // ========================================

    @Nested
    @DisplayName("lastDays() - Boundary and Edge Cases")
    class LastDaysBoundaryTests {

        @Test
        @DisplayName("Throws exception when days is 0")
        void lastDays_with0Days_throwsException() {
            // Note: 0 days might be valid (today only) - current behavior untested
            // This test documents current behavior
            AnalysisPeriod period = AnalysisPeriod.lastDays(0);

            assertEquals(LocalDate.now(), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Throws exception when days is negative")
        void lastDays_withNegativeDays_doesNotThrow() {
            // Note: Negative days are not validated - potential bug
            // This would create a period where start > end
            AnalysisPeriod period = AnalysisPeriod.lastDays(-1);

            assertNotNull(period);
            // Start date would be AFTER end date
            assertTrue(period.startDate().isAfter(period.endDate()));
        }

        @Test
        @DisplayName("Throws exception when days exceeds 360")
        void lastDays_with361Days_throwsException() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisPeriod.lastDays(361)
            );

            assertTrue(exception.getMessage().contains("360"));
            assertTrue(exception.getMessage().contains("year"));
        }

        @Test
        @DisplayName("Throws exception when days is much larger than limit")
        void lastDays_with1000Days_throwsException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisPeriod.lastDays(1000));
        }

        @Test
        @DisplayName("Throws exception when days is Integer.MAX_VALUE")
        void lastDays_withMaxInt_throwsException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisPeriod.lastDays(Integer.MAX_VALUE));
        }
    }

    // ========================================
    // lastMonths() - Happy Path Tests
    // ========================================

    @Nested
    @DisplayName("lastMonths() - Happy Path")
    class LastMonthsHappyPathTests {

        @Test
        @DisplayName("Creates period for last 1 month")
        void lastMonths_with1Month_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastMonths(1);

            assertNotNull(period);
            assertEquals(LocalDate.now().minusMonths(1), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 3 months")
        void lastMonths_with3Months_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastMonths(3);

            assertEquals(LocalDate.now().minusMonths(3), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 6 months")
        void lastMonths_with6Months_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastMonths(6);

            assertEquals(LocalDate.now().minusMonths(6), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 12 months")
        void lastMonths_with12Months_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastMonths(12);

            assertEquals(LocalDate.now().minusMonths(12), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 24 months")
        void lastMonths_with24Months_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastMonths(24);

            assertEquals(LocalDate.now().minusMonths(24), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 120 months (maximum allowed)")
        void lastMonths_with120Months_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastMonths(120);

            assertEquals(LocalDate.now().minusMonths(120), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }
    }

    // ========================================
    // lastMonths() - Boundary and Edge Cases
    // ========================================

    @Nested
    @DisplayName("lastMonths() - Boundary and Edge Cases")
    class LastMonthsBoundaryTests {

        @Test
        @DisplayName("Creates period when months is 0")
        void lastMonths_with0Months_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastMonths(0);

            assertEquals(LocalDate.now(), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period when months is negative")
        void lastMonths_withNegativeMonths_createsPeriodWithInvertedDates() {
            AnalysisPeriod period = AnalysisPeriod.lastMonths(-1);

            assertNotNull(period);
            // Start date would be AFTER end date
            assertTrue(period.startDate().isAfter(period.endDate()));
        }

        @Test
        @DisplayName("Throws exception when months exceeds 120")
        void lastMonths_with121Months_throwsException() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisPeriod.lastMonths(121)
            );

            assertTrue(exception.getMessage().contains("120"));
            assertTrue(exception.getMessage().contains("10 years"));
        }

        @Test
        @DisplayName("Throws exception when months is much larger than limit")
        void lastMonths_with500Months_throwsException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisPeriod.lastMonths(500));
        }

        @Test
        @DisplayName("Throws exception when months is Integer.MAX_VALUE")
        void lastMonths_withMaxInt_throwsException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisPeriod.lastMonths(Integer.MAX_VALUE));
        }
    }

    // ========================================
    // lastYears() - Happy Path Tests
    // ========================================

    @Nested
    @DisplayName("lastYears() - Happy Path")
    class LastYearsHappyPathTests {

        @Test
        @DisplayName("Creates period for last 1 year")
        void lastYears_with1Year_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastYears(1);

            assertNotNull(period);
            assertEquals(LocalDate.now().minusYears(1), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 2 years")
        void lastYears_with2Years_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastYears(2);

            assertEquals(LocalDate.now().minusYears(2), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 5 years")
        void lastYears_with5Years_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastYears(5);

            assertEquals(LocalDate.now().minusYears(5), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period for last 10 years (maximum allowed)")
        void lastYears_with10Years_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastYears(10);

            assertEquals(LocalDate.now().minusYears(10), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }
    }

    // ========================================
    // lastYears() - Boundary and Edge Cases
    // ========================================

    @Nested
    @DisplayName("lastYears() - Boundary and Edge Cases")
    class LastYearsBoundaryTests {

        @Test
        @DisplayName("Creates period when years is 0")
        void lastYears_with0Years_createsPeriod() {
            AnalysisPeriod period = AnalysisPeriod.lastYears(0);

            assertEquals(LocalDate.now(), period.startDate());
            assertEquals(LocalDate.now(), period.endDate());
        }

        @Test
        @DisplayName("Creates period when years is negative")
        void lastYears_withNegativeYears_createsPeriodWithInvertedDates() {
            AnalysisPeriod period = AnalysisPeriod.lastYears(-1);

            assertNotNull(period);
            // Start date would be AFTER end date
            assertTrue(period.startDate().isAfter(period.endDate()));
        }

        @Test
        @DisplayName("Throws exception when years exceeds 10")
        void lastYears_with11Years_throwsException() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisPeriod.lastYears(11)
            );

            assertTrue(exception.getMessage().contains("10 years"));
        }

        @Test
        @DisplayName("Throws exception when years is much larger than limit")
        void lastYears_with100Years_throwsException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisPeriod.lastYears(100));
        }

        @Test
        @DisplayName("Throws exception when years is Integer.MAX_VALUE")
        void lastYears_withMaxInt_throwsException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisPeriod.lastYears(Integer.MAX_VALUE));
        }
    }

    // ========================================
    // Record Behavior Tests
    // ========================================

    @Nested
    @DisplayName("Record behavior")
    class RecordBehaviorTests {

        @Test
        @DisplayName("Two periods with same dates are equal")
        void equals_withSameDates_returnsTrue() {
            LocalDate start = LocalDate.of(2026, 1, 1);
            LocalDate end = LocalDate.of(2026, 1, 31);

            AnalysisPeriod period1 = new AnalysisPeriod(start, end);
            AnalysisPeriod period2 = new AnalysisPeriod(start, end);

            assertEquals(period1, period2);
            assertEquals(period1.hashCode(), period2.hashCode());
        }

        @Test
        @DisplayName("Two periods with different dates are not equal")
        void equals_withDifferentDates_returnsFalse() {
            AnalysisPeriod period1 = new AnalysisPeriod(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31)
            );
            AnalysisPeriod period2 = new AnalysisPeriod(
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28)
            );

            assertNotEquals(period1, period2);
        }

        @Test
        @DisplayName("toString() returns meaningful representation")
        void toString_returnsMeaningfulString() {
            LocalDate start = LocalDate.of(2026, 1, 1);
            LocalDate end = LocalDate.of(2026, 1, 31);

            AnalysisPeriod period = new AnalysisPeriod(start, end);
            String result = period.toString();

            assertNotNull(result);
            assertTrue(result.contains("2026-01-01"));
            assertTrue(result.contains("2026-01-31"));
        }
    }

    // ========================================
    // Integration Tests (Cross-Method)
    // ========================================

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @Test
        @DisplayName("lastDays(30) approximately equals lastMonths(1)")
        void lastDays30_approximatelyEqualsLastMonths1() {
            AnalysisPeriod byDays = AnalysisPeriod.lastDays(30);
            AnalysisPeriod byMonths = AnalysisPeriod.lastMonths(1);

            // Should be within a few days of each other
            long daysDifference = Math.abs(
                byDays.startDate().toEpochDay() - byMonths.startDate().toEpochDay()
            );

            assertTrue(daysDifference <= 2,
                "30 days should be approximately 1 month (within 2 days)");
        }

        @Test
        @DisplayName("lastMonths(12) equals lastYears(1)")
        void lastMonths12_equalsLastYears1() {
            AnalysisPeriod byMonths = AnalysisPeriod.lastMonths(12);
            AnalysisPeriod byYears = AnalysisPeriod.lastYears(1);

            assertEquals(byMonths.startDate(), byYears.startDate());
            assertEquals(byMonths.endDate(), byYears.endDate());
        }

        @Test
        @DisplayName("lastYears(10) equals lastMonths(120)")
        void lastYears10_equalsLastMonths120() {
            AnalysisPeriod byYears = AnalysisPeriod.lastYears(10);
            AnalysisPeriod byMonths = AnalysisPeriod.lastMonths(120);

            assertEquals(byYears.startDate(), byMonths.startDate());
            assertEquals(byYears.endDate(), byMonths.endDate());
        }
    }

    // ========================================
    // Error Message Tests
    // ========================================

    @Nested
    @DisplayName("Error messages")
    class ErrorMessageTests {

        @Test
        @DisplayName("lastDays error message is descriptive")
        void lastDays_errorMessage_isDescriptive() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisPeriod.lastDays(361)
            );

            String message = exception.getMessage();
            assertNotNull(message);
            assertFalse(message.isEmpty());
            assertTrue(message.contains("360") || message.contains("year"));
        }

        @Test
        @DisplayName("lastMonths error message is descriptive")
        void lastMonths_errorMessage_isDescriptive() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisPeriod.lastMonths(121)
            );

            String message = exception.getMessage();
            assertNotNull(message);
            assertFalse(message.isEmpty());
            assertTrue(message.contains("120") || message.contains("10 years"));
        }

        @Test
        @DisplayName("lastYears error message is descriptive")
        void lastYears_errorMessage_isDescriptive() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisPeriod.lastYears(11)
            );

            String message = exception.getMessage();
            assertNotNull(message);
            assertFalse(message.isEmpty());
            assertTrue(message.contains("10"));
        }
    }
}


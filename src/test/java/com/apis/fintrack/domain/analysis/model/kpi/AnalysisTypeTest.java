package com.apis.fintrack.domain.analysis.model.kpi;

import com.apis.fintrack.domain.analysis.model.metric.MetricUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AnalysisType enum.
 *
 * Tests follow specification-based testing approach, covering:
 * - Enum structure and values
 * - Unit associations
 * - fromString() method with valid inputs
 * - fromString() method with invalid inputs
 * - Edge cases and error messages
 */
@DisplayName("AnalysisType Tests")
class AnalysisTypeTest {

    // ========================================
    // Enum Structure Tests
    // ========================================

    @Nested
    @DisplayName("Enum structure")
    class EnumStructureTests {

        @Test
        @DisplayName("Enum has exactly three values")
        void enum_hasExactlyThreeValues() {
            AnalysisType[] values = AnalysisType.values();

            assertEquals(3, values.length,
                "AnalysisType should have exactly 3 values");
        }

        @Test
        @DisplayName("Enum contains SAVINGS_RATE")
        void enum_containsSavingsRate() {
            assertNotNull(AnalysisType.SAVINGS_RATE);
        }

        @Test
        @DisplayName("Enum contains MONTHLY_AVERAGE_EXPENSES")
        void enum_containsMonthlyAverageExpenses() {
            assertNotNull(AnalysisType.MONTHLY_AVERAGE_EXPENSES);
        }

        @Test
        @DisplayName("Enum contains SPENDING_TREND")
        void enum_containsSpendingTrend() {
            assertNotNull(AnalysisType.SPENDING_TREND);
        }

        @Test
        @DisplayName("All enum values have associated unit")
        void enum_allValuesHaveUnit() {
            for (AnalysisType type : AnalysisType.values()) {
                assertNotNull(type.getUnit(),
                    "Enum value " + type + " should have a MetricUnit");
            }
        }
    }

    // ========================================
    // Unit Association Tests
    // ========================================

    @Nested
    @DisplayName("Unit associations")
    class UnitAssociationTests {

        @Test
        @DisplayName("SAVINGS_RATE has PERCENT unit")
        void savingsRate_hasPercentUnit() {
            assertEquals(MetricUnit.PERCENT, AnalysisType.SAVINGS_RATE.getUnit());
        }

        @Test
        @DisplayName("MONTHLY_AVERAGE_EXPENSES has CURRENCY unit")
        void monthlyAverageExpenses_hasCurrencyUnit() {
            assertEquals(MetricUnit.CURRENCY, AnalysisType.MONTHLY_AVERAGE_EXPENSES.getUnit());
        }

        @Test
        @DisplayName("SPENDING_TREND has PERCENT unit")
        void spendingTrend_hasPercentUnit() {
            assertEquals(MetricUnit.PERCENT, AnalysisType.SPENDING_TREND.getUnit());
        }

        @Test
        @DisplayName("unit() method returns same as getUnit()")
        void unitMethod_returnsSameAsGetUnit() {
            for (AnalysisType type : AnalysisType.values()) {
                assertEquals(type.getUnit(), type.unit(),
                    "unit() and getUnit() should return the same value for " + type);
            }
        }
    }

    // ========================================
    // fromString() - Happy Path Tests
    // ========================================

    @Nested
    @DisplayName("fromString() - Happy Path")
    class FromStringHappyPathTests {

        @Test
        @DisplayName("Parses exact uppercase name correctly")
        void fromString_withExactUppercase_returnsCorrectEnum() {
            assertEquals(AnalysisType.SAVINGS_RATE,
                AnalysisType.fromString("SAVINGS_RATE"));
            assertEquals(AnalysisType.MONTHLY_AVERAGE_EXPENSES,
                AnalysisType.fromString("MONTHLY_AVERAGE_EXPENSES"));
            assertEquals(AnalysisType.SPENDING_TREND,
                AnalysisType.fromString("SPENDING_TREND"));
        }

        @Test
        @DisplayName("Parses lowercase name correctly")
        void fromString_withLowercase_returnsCorrectEnum() {
            assertEquals(AnalysisType.SAVINGS_RATE,
                AnalysisType.fromString("savings_rate"));
            assertEquals(AnalysisType.MONTHLY_AVERAGE_EXPENSES,
                AnalysisType.fromString("monthly_average_expenses"));
            assertEquals(AnalysisType.SPENDING_TREND,
                AnalysisType.fromString("spending_trend"));
        }

        @Test
        @DisplayName("Parses mixed case name correctly")
        void fromString_withMixedCase_returnsCorrectEnum() {
            assertEquals(AnalysisType.SAVINGS_RATE,
                AnalysisType.fromString("Savings_Rate"));
            assertEquals(AnalysisType.SAVINGS_RATE,
                AnalysisType.fromString("SaViNgS_rAtE"));
            assertEquals(AnalysisType.MONTHLY_AVERAGE_EXPENSES,
                AnalysisType.fromString("Monthly_Average_Expenses"));
        }

        @Test
        @DisplayName("Parses name with leading spaces")
        void fromString_withLeadingSpaces_returnsCorrectEnum() {
            assertEquals(AnalysisType.SAVINGS_RATE,
                AnalysisType.fromString("  SAVINGS_RATE"));
            assertEquals(AnalysisType.SAVINGS_RATE,
                AnalysisType.fromString("\tSAVINGS_RATE"));
        }

        @Test
        @DisplayName("Parses name with trailing spaces")
        void fromString_withTrailingSpaces_returnsCorrectEnum() {
            assertEquals(AnalysisType.SAVINGS_RATE,
                AnalysisType.fromString("SAVINGS_RATE  "));
            assertEquals(AnalysisType.SAVINGS_RATE,
                AnalysisType.fromString("SAVINGS_RATE\n"));
        }

        @Test
        @DisplayName("Parses name with both leading and trailing spaces")
        void fromString_withBothSpaces_returnsCorrectEnum() {
            assertEquals(AnalysisType.SAVINGS_RATE,
                AnalysisType.fromString("  SAVINGS_RATE  "));
            assertEquals(AnalysisType.MONTHLY_AVERAGE_EXPENSES,
                AnalysisType.fromString("\t\nMONTHLY_AVERAGE_EXPENSES \t"));
        }

        @Test
        @DisplayName("Parses all enum values correctly")
        void fromString_withAllEnumValues_returnsAllCorrectly() {
            for (AnalysisType expectedType : AnalysisType.values()) {
                AnalysisType actualType = AnalysisType.fromString(expectedType.name());
                assertEquals(expectedType, actualType,
                    "Failed to parse: " + expectedType.name());
            }
        }
    }

    // ========================================
    // fromString() - Null/Empty Validation Tests
    // ========================================

    @Nested
    @DisplayName("fromString() - Null and Empty validation")
    class FromStringNullEmptyTests {

        @Test
        @DisplayName("Throws exception when input is null")
        void fromString_withNull_throwsIllegalArgumentException() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisType.fromString(null)
            );

            assertTrue(exception.getMessage().contains("null"),
                "Error message should mention null");
        }

        @Test
        @DisplayName("Throws exception when input is empty string")
        void fromString_withEmptyString_throwsIllegalArgumentException() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisType.fromString("")
            );

            assertTrue(exception.getMessage().contains("vacío") ||
                      exception.getMessage().contains("null"),
                "Error message should mention empty");
        }

        @Test
        @DisplayName("Throws exception when input is blank string")
        void fromString_withBlankString_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("   "));
        }

        @Test
        @DisplayName("Throws exception when input is only spaces")
        void fromString_withOnlySpaces_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("     "));
        }

        @Test
        @DisplayName("Throws exception when input is only tabs")
        void fromString_withTabs_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("\t\t\t"));
        }

        @Test
        @DisplayName("Throws exception when input is only newlines")
        void fromString_withNewlines_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("\n\n"));
        }

        @Test
        @DisplayName("Throws exception when input is mixed whitespace")
        void fromString_withMixedWhitespace_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString(" \t\n\r "));
        }
    }

    // ========================================
    // fromString() - Invalid Values Tests
    // ========================================

    @Nested
    @DisplayName("fromString() - Invalid values")
    class FromStringInvalidValuesTests {

        @Test
        @DisplayName("Throws exception for non-existent enum value")
        void fromString_withNonExistentValue_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("INVALID_TYPE"));
        }

        @Test
        @DisplayName("Throws exception for partial match")
        void fromString_withPartialMatch_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("SAVINGS"));
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("MONTHLY_AVERAGE"));
        }

        @Test
        @DisplayName("Throws exception for typo - singular form")
        void fromString_withTypoSingular_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("SAVING_RATE"));
        }

        @Test
        @DisplayName("Throws exception for name with hyphen instead of underscore")
        void fromString_withHyphen_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("SAVINGS-RATE"));
        }

        @Test
        @DisplayName("Throws exception for name with dot instead of underscore")
        void fromString_withDot_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("SAVINGS.RATE"));
        }

        @Test
        @DisplayName("Throws exception for name with internal spaces")
        void fromString_withInternalSpaces_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("SAVINGS RATE"));
        }

        @Test
        @DisplayName("Throws exception for name without underscore")
        void fromString_withoutUnderscore_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("SAVINGSRATE"));
        }

        @Test
        @DisplayName("Throws exception for completely unrelated string")
        void fromString_withUnrelatedString_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("RANDOM_VALUE"));
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("123"));
        }
    }

    // ========================================
    // Error Message Tests
    // ========================================

    @Nested
    @DisplayName("Error messages")
    class ErrorMessageTests {

        @Test
        @DisplayName("Null error message is descriptive")
        void fromString_withNull_hasDescriptiveMessage() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisType.fromString(null)
            );

            String message = exception.getMessage();
            assertTrue(message.contains("null") || message.contains("vacío"),
                "Error message should describe the problem: " + message);
        }

        @Test
        @DisplayName("Empty error message is descriptive")
        void fromString_withEmpty_hasDescriptiveMessage() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisType.fromString("")
            );

            String message = exception.getMessage();
            assertFalse(message.isEmpty(), "Error message should not be empty");
        }

        @Test
        @DisplayName("Invalid value error includes input value")
        void fromString_withInvalid_includesInputValue() {
            String invalidInput = "INVALID_TYPE";

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisType.fromString(invalidInput)
            );

            assertTrue(exception.getMessage().contains(invalidInput),
                "Error message should include the invalid input: " + exception.getMessage());
        }

        @Test
        @DisplayName("Error messages are in Spanish")
        void fromString_messagesAreInSpanish() {
            // Test null/empty message
            IllegalArgumentException nullException = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisType.fromString(null)
            );
            assertTrue(nullException.getMessage().matches(".*[áéíóúñÁÉÍÓÚÑ].*|.*vacío.*"),
                "Message should be in Spanish");

            // Test invalid value message
            IllegalArgumentException invalidException = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisType.fromString("INVALID")
            );
            assertTrue(invalidException.getMessage().matches(".*[áéíóúñÁÉÍÓÚÑ].*|.*válido.*"),
                "Message should be in Spanish");
        }
    }

    // ========================================
    // Edge Cases Tests
    // ========================================

    @Nested
    @DisplayName("Edge cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("Throws exception for very long string")
        void fromString_withVeryLongString_throwsException() {
            String veryLongString = "A".repeat(10000);

            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString(veryLongString));
        }

        @Test
        @DisplayName("Throws exception for string with unicode characters")
        void fromString_withUnicodeCharacters_throwsException() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("SÁVINGS_RÁTE"));
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("SAVINGS_RATE™"));
        }

        @Test
        @DisplayName("Preserves original input in error message")
        void fromString_preservesOriginalInputInErrorMessage() {
            String input = "My_Invalid_Type";

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnalysisType.fromString(input)
            );

            assertTrue(exception.getMessage().contains(input),
                "Error message should preserve original input casing");
        }

        @Test
        @DisplayName("Handles multiple consecutive spaces")
        void fromString_withMultipleSpaces_handlesCorrectly() {
            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString("SAVINGS     RATE"));
        }

        @Test
        @DisplayName("Handles non-breaking space (U+00A0)")
        void fromString_withNonBreakingSpace_handlesCorrectly() {
            String withNonBreakingSpace = "\u00A0\u00A0";

            assertThrows(IllegalArgumentException.class,
                () -> AnalysisType.fromString(withNonBreakingSpace));
        }
    }

    // ========================================
    // Enum Behavior Tests
    // ========================================

    @Nested
    @DisplayName("Enum behavior")
    class EnumBehaviorTests {

        @Test
        @DisplayName("valueOf() works with exact name")
        void valueOf_withExactName_works() {
            assertEquals(AnalysisType.SAVINGS_RATE,
                AnalysisType.valueOf("SAVINGS_RATE"));
        }

        @Test
        @DisplayName("name() returns uppercase constant name")
        void name_returnsUppercaseConstantName() {
            assertEquals("SAVINGS_RATE", AnalysisType.SAVINGS_RATE.name());
            assertEquals("MONTHLY_AVERAGE_EXPENSES",
                AnalysisType.MONTHLY_AVERAGE_EXPENSES.name());
            assertEquals("SPENDING_TREND", AnalysisType.SPENDING_TREND.name());
        }

        @Test
        @DisplayName("ordinal() returns correct position")
        void ordinal_returnsCorrectPosition() {
            assertEquals(0, AnalysisType.SAVINGS_RATE.ordinal());
            assertEquals(1, AnalysisType.MONTHLY_AVERAGE_EXPENSES.ordinal());
            assertEquals(2, AnalysisType.SPENDING_TREND.ordinal());
        }

        @Test
        @DisplayName("toString() returns name")
        void toString_returnsName() {
            assertEquals("SAVINGS_RATE", AnalysisType.SAVINGS_RATE.toString());
        }
    }
}


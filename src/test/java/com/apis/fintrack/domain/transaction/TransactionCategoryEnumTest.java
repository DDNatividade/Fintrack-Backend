package com.apis.fintrack.domain.transaction;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TransactionCategoryEnum} enum.
 *
 * <p>Tests cover enum values, fromString conversion, validation,
 * and descriptions following specification-based testing approaches.</p>
 */
@DisplayName("TransactionCategoryEnum Tests")
class TransactionCategoryEnumTest {

    // ==================== ENUM VALUES - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Enum Values")
    class EnumValuesTests {

        /**
         * Verifies that all expected categories exist.
         */
        @Test
        @DisplayName("Should have all expected categories")
        void enumValues_shouldContainAllExpectedCategories() {
            TransactionCategoryEnum[] values = TransactionCategoryEnum.values();

            assertTrue(values.length >= 13);

            assertNotNull(TransactionCategoryEnum.valueOf("HOUSING"));
            assertNotNull(TransactionCategoryEnum.valueOf("RECREATIONAL"));
            assertNotNull(TransactionCategoryEnum.valueOf("ALIMENTATION"));
            assertNotNull(TransactionCategoryEnum.valueOf("INVERSION"));
            assertNotNull(TransactionCategoryEnum.valueOf("TRANSPORTATION"));
            assertNotNull(TransactionCategoryEnum.valueOf("HEALTH"));
            assertNotNull(TransactionCategoryEnum.valueOf("EDUCATION"));
            assertNotNull(TransactionCategoryEnum.valueOf("UTILITIES"));
            assertNotNull(TransactionCategoryEnum.valueOf("SALARY"));
            assertNotNull(TransactionCategoryEnum.valueOf("FREELANCE"));
            assertNotNull(TransactionCategoryEnum.valueOf("GIFTS"));
            assertNotNull(TransactionCategoryEnum.valueOf("SALES"));
            assertNotNull(TransactionCategoryEnum.valueOf("OTHER"));
        }

        /**
         * Verifies that each category has a description.
         */
        @Test
        @DisplayName("Each category should have a description")
        void enumValues_shouldHaveDescriptions() {
            for (TransactionCategoryEnum category : TransactionCategoryEnum.values()) {
                assertNotNull(category.getDescription());
                assertFalse(category.getDescription().isEmpty());
            }
        }
    }

    // ==================== fromString() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Static Method: fromString()")
    class FromStringTests {

        /**
         * Verifies that fromString() returns correct enum for uppercase name.
         */
        @Test
        @DisplayName("Should return enum for uppercase name")
        void fromString_uppercase_shouldReturnEnum() {
            TransactionCategoryEnum result = TransactionCategoryEnum.fromString("SALARY");

            assertEquals(TransactionCategoryEnum.SALARY, result);
        }

        /**
         * Verifies that fromString() returns correct enum for lowercase name.
         */
        @Test
        @DisplayName("Should return enum for lowercase name")
        void fromString_lowercase_shouldReturnEnum() {
            TransactionCategoryEnum result = TransactionCategoryEnum.fromString("salary");

            assertEquals(TransactionCategoryEnum.SALARY, result);
        }

        /**
         * Verifies that fromString() returns correct enum for mixed case name.
         */
        @Test
        @DisplayName("Should return enum for mixed case name")
        void fromString_mixedCase_shouldReturnEnum() {
            TransactionCategoryEnum result = TransactionCategoryEnum.fromString("SaLaRy");

            assertEquals(TransactionCategoryEnum.SALARY, result);
        }

        /**
         * Verifies that fromString() throws for invalid name.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid name")
        void fromString_invalidName_shouldThrowException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> TransactionCategoryEnum.fromString("INVALID")
            );
        }

        /**
         * Verifies that fromString() throws for null name.
         */
        @Test
        @DisplayName("Should throw exception for null name")
        void fromString_nullName_shouldThrowException() {
            assertThrows(
                    Exception.class,
                    () -> TransactionCategoryEnum.fromString(null)
            );
        }
    }

    // ==================== isValid() - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Static Method: isValid()")
    class IsValidTests {

        /**
         * Verifies that isValid() returns true for valid category.
         */
        @Test
        @DisplayName("Should return true for valid category")
        void isValid_validCategory_shouldReturnTrue() {
            assertTrue(TransactionCategoryEnum.isValid(TransactionCategoryEnum.SALARY));
            assertTrue(TransactionCategoryEnum.isValid(TransactionCategoryEnum.HOUSING));
        }

        /**
         * Verifies that isValid() returns false for null.
         */
        @Test
        @DisplayName("Should return false for null")
        void isValid_null_shouldReturnFalse() {
            assertFalse(TransactionCategoryEnum.isValid(null));
        }

        /**
         * Verifies that isValid() returns true for all enum values.
         */
        @Test
        @DisplayName("Should return true for all enum values")
        void isValid_allEnumValues_shouldReturnTrue() {
            for (TransactionCategoryEnum category : TransactionCategoryEnum.values()) {
                assertTrue(TransactionCategoryEnum.isValid(category));
            }
        }
    }

    // ==================== DESCRIPTION - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Descriptions")
    class DescriptionTests {

        /**
         * Verifies SALARY description.
         */
        @Test
        @DisplayName("SALARY should have correct description")
        void salary_shouldHaveCorrectDescription() {
            assertEquals("Income from work or services rendered",
                    TransactionCategoryEnum.SALARY.getDescription());
        }

        /**
         * Verifies HOUSING description.
         */
        @Test
        @DisplayName("HOUSING should have correct description")
        void housing_shouldHaveCorrectDescription() {
            assertEquals("Home-related expenses",
                    TransactionCategoryEnum.HOUSING.getDescription());
        }

        /**
         * Verifies OTHER description.
         */
        @Test
        @DisplayName("OTHER should have correct description")
        void other_shouldHaveCorrectDescription() {
            assertEquals("Miscellaneous expenses or income that do not fit into other categories",
                    TransactionCategoryEnum.OTHER.getDescription());
        }
    }
}


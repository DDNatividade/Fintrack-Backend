package com.apis.fintrack.domain.transaction;

import com.apis.fintrack.domain.transaction.model.Category;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Category} value object.
 *
 * <p>Tests cover factory methods, validation rules,
 * and equality contract following specification-based testing approaches.</p>
 */
@DisplayName("Category Value Object Tests")
class CategoryTest {

    // ==================== FACTORY METHOD: of() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid Category.
         */
        @Test
        @DisplayName("Should return Category when given a valid enum")
        void of_withValidEnum_shouldReturnCategory() {
            Category result = Category.of(TransactionCategoryEnum.SALARY);

            assertNotNull(result);
            assertEquals(TransactionCategoryEnum.SALARY, result.getValue());
        }

        /**
         * Verifies that null enum throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when enum is null")
        void of_withNullEnum_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Category.of(null)
            );
        }

        /**
         * Verifies that all enum values can be wrapped.
         */
        @Test
        @DisplayName("Should accept all TransactionCategoryEnum values")
        void of_allEnumValues_shouldReturnCategory() {
            for (TransactionCategoryEnum enumValue : TransactionCategoryEnum.values()) {
                Category result = Category.of(enumValue);
                assertEquals(enumValue, result.getValue());
            }
        }
    }

    // ==================== FACTORY METHOD: fromString() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: fromString()")
    class FromStringFactoryTests {

        /**
         * Verifies that fromString() creates Category from valid name.
         */
        @Test
        @DisplayName("Should return Category when given valid category name")
        void fromString_withValidName_shouldReturnCategory() {
            Category result = Category.fromString("SALARY");

            assertEquals(TransactionCategoryEnum.SALARY, result.getValue());
        }

        /**
         * Verifies that fromString() handles lowercase.
         */
        @Test
        @DisplayName("Should accept lowercase category name")
        void fromString_withLowercase_shouldReturnCategory() {
            Category result = Category.fromString("salary");

            assertEquals(TransactionCategoryEnum.SALARY, result.getValue());
        }

        /**
         * Verifies that fromString() trims whitespace.
         */
        @Test
        @DisplayName("Should trim whitespace from category name")
        void fromString_withWhitespace_shouldReturnCategory() {
            Category result = Category.fromString("  SALARY  ");

            assertEquals(TransactionCategoryEnum.SALARY, result.getValue());
        }

        /**
         * Verifies that null name throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when name is null")
        void fromString_withNullName_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Category.fromString(null)
            );
        }

        /**
         * Verifies that empty name throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when name is empty")
        void fromString_withEmptyName_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Category.fromString("")
            );
        }

        /**
         * Verifies that invalid name throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when name is invalid")
        void fromString_withInvalidName_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Category.fromString("INVALID_CATEGORY")
            );
        }
    }

    // ==================== QUERY METHODS - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Query Methods")
    class QueryMethodsTests {

        /**
         * Verifies getValue() returns the enum value.
         */
        @Test
        @DisplayName("getValue() should return the enum value")
        void getValue_shouldReturnEnumValue() {
            Category category = Category.of(TransactionCategoryEnum.HOUSING);

            assertEquals(TransactionCategoryEnum.HOUSING, category.getValue());
        }

        /**
         * Verifies getValue().name() returns category name.
         */
        @Test
        @DisplayName("getValue().name() should return category name")
        void getValue_name_shouldReturnCategoryName() {
            Category category = Category.of(TransactionCategoryEnum.HEALTH);

            assertEquals("HEALTH", category.getValue().name());
        }

        /**
         * Verifies getValue().getDescription() returns category description.
         */
        @Test
        @DisplayName("getValue().getDescription() should return category description")
        void getValue_getDescription_shouldReturnCategoryDescription() {
            Category category = Category.of(TransactionCategoryEnum.HEALTH);

            assertNotNull(category.getValue().getDescription());
            assertFalse(category.getValue().getDescription().isEmpty());
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two Category with same enum are equal.
         */
        @Test
        @DisplayName("equals() should return true for same enum value")
        void equals_withSameEnum_shouldReturnTrue() {
            Category cat1 = Category.of(TransactionCategoryEnum.HOUSING);
            Category cat2 = Category.of(TransactionCategoryEnum.HOUSING);

            assertEquals(cat1, cat2);
        }

        /**
         * Verifies that two Category with different enums are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different enum values")
        void equals_withDifferentEnum_shouldReturnFalse() {
            Category cat1 = Category.of(TransactionCategoryEnum.HOUSING);
            Category cat2 = Category.of(TransactionCategoryEnum.SALARY);

            assertNotEquals(cat1, cat2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            Category category = Category.of(TransactionCategoryEnum.HOUSING);

            assertNotEquals(null, category);
        }

        /**
         * Verifies reflexivity.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            Category category = Category.of(TransactionCategoryEnum.HOUSING);

            assertEquals(category, category);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_equalObjects_shouldReturnSameHash() {
            Category cat1 = Category.of(TransactionCategoryEnum.SALARY);
            Category cat2 = Category.of(TransactionCategoryEnum.SALARY);

            assertEquals(cat1.hashCode(), cat2.hashCode());
        }

        /**
         * Verifies hashCode consistency.
         */
        @Test
        @DisplayName("hashCode() should be consistent across multiple calls")
        void hashCode_multipleCalls_shouldBeConsistent() {
            Category category = Category.of(TransactionCategoryEnum.SALARY);

            int hash1 = category.hashCode();
            int hash2 = category.hashCode();

            assertEquals(hash1, hash2);
        }
    }
}


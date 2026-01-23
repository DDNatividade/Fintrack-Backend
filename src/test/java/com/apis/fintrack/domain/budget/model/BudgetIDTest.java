package com.apis.fintrack.domain.budget.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BudgetID Value Object.
 *
 * Tests follow specification-based testing approach, covering:
 * - Constructor validation (negative values)
 * - Factory methods (of, empty)
 * - State methods (isEmpty, value)
 * - Equality and hashCode contracts
 *
 * BudgetID is an immutable Value Object that wraps a Long identifier.
 * It allows null values (representing new/unassigned IDs) but rejects negative values.
 */
@DisplayName("BudgetID Value Object Tests")
class BudgetIDTest {

    // ========================================
    // PRIORITY HIGH - Negative Value Validation
    // ========================================

    @Nested
    @DisplayName("Constructor - Negative Value Validation")
    class ConstructorNegativeValidationTests {

        /**
         * Verifies that creating a BudgetID with a negative value (-1)
         * throws IllegalArgumentException.
         * <p>
         * Business Rule: Budget IDs cannot be negative as they represent
         * database primary keys which are always non-negative.
         * <p>
         * Bug it could reveal: Missing or incorrect validation of negative values.
         */
        @Test
        @DisplayName("constructor with negative value throws IllegalArgumentException")
        void constructor_negativeValue_throwsIllegalArgument() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new BudgetID(-1L)
            );

            assertEquals("Budget id cannot be negative", exception.getMessage(),
                    "Exception message should indicate negative ID is not allowed");
        }

        /**
         * Verifies that creating a BudgetID with Long.MIN_VALUE
         * throws IllegalArgumentException.
         * <p>
         * This tests the extreme negative boundary to ensure no overflow issues.
         * <p>
         * Bug it could reveal: Overflow or boundary condition not handled.
         */
        @Test
        @DisplayName("constructor with Long.MIN_VALUE throws IllegalArgumentException")
        void constructor_minLongValue_throwsIllegalArgument() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new BudgetID(Long.MIN_VALUE)
            );

            assertEquals("Budget id cannot be negative", exception.getMessage(),
                    "Should reject Long.MIN_VALUE as negative");
        }

        /**
         * Verifies that the factory method of() with a negative value
         * also throws IllegalArgumentException.
         * <p>
         * Ensures the factory properly delegates to the constructor validation.
         * <p>
         * Bug it could reveal: Factory bypasses constructor validation.
         */
        @Test
        @DisplayName("of() with negative value throws IllegalArgumentException")
        void of_negativeValue_throwsIllegalArgument() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> BudgetID.of(-100L)
            );

            assertEquals("Budget id cannot be negative", exception.getMessage(),
                    "Factory method should delegate validation to constructor");
        }
    }

    // ========================================
    // PRIORITY HIGH - Factory Methods
    // ========================================

    @Nested
    @DisplayName("Factory Methods")
    class FactoryMethodsTests {

        /**
         * Verifies that of() with a positive value creates a valid BudgetID.
         * <p>
         * This is the primary happy path for creating BudgetID instances.
         * <p>
         * Bug it could reveal: Factory fails to create valid instances.
         */
        @Test
        @DisplayName("of() with positive value creates BudgetID")
        void of_positiveValue_createsBudgetID() {
            BudgetID budgetID = BudgetID.of(123L);

            assertNotNull(budgetID, "BudgetID should not be null");
            assertEquals(123L, budgetID.value(), "Value should be 123");
            assertFalse(budgetID.isEmpty(), "Should not be empty");
        }

        /**
         * Verifies that of() with zero creates a valid BudgetID.
         * <p>
         * Zero is a valid ID (first auto-increment in some databases).
         * <p>
         * Bug it could reveal: Zero incorrectly treated as invalid or empty.
         */
        @Test
        @DisplayName("of() with zero creates valid BudgetID")
        void of_zero_createsBudgetID() {
            BudgetID budgetID = BudgetID.of(0L);

            assertNotNull(budgetID, "BudgetID should not be null");
            assertEquals(0L, budgetID.value(), "Value should be 0");
            assertFalse(budgetID.isEmpty(), "Zero should not be considered empty");
        }

        /**
         * Verifies that of() with null creates a BudgetID with null value.
         * <p>
         * Null represents an unassigned/new ID before persistence.
         * <p>
         * Bug it could reveal: Null incorrectly rejected or causes NPE.
         */
        @Test
        @DisplayName("of() with null creates BudgetID with null value")
        void of_null_createsBudgetID() {
            BudgetID budgetID = BudgetID.of(null);

            assertNotNull(budgetID, "BudgetID instance should not be null");
            assertNull(budgetID.value(), "Internal value should be null");
            assertTrue(budgetID.isEmpty(), "Should be considered empty");
        }

        /**
         * Verifies that of() with Long.MAX_VALUE creates a valid BudgetID.
         * <p>
         * Tests the positive boundary to ensure no overflow issues.
         * <p>
         * Bug it could reveal: Overflow or boundary rejection.
         */
        @Test
        @DisplayName("of() with Long.MAX_VALUE creates valid BudgetID")
        void of_maxLongValue_createsBudgetID() {
            BudgetID budgetID = BudgetID.of(Long.MAX_VALUE);

            assertNotNull(budgetID, "BudgetID should not be null");
            assertEquals(Long.MAX_VALUE, budgetID.value(),
                    "Should store Long.MAX_VALUE correctly");
            assertFalse(budgetID.isEmpty(), "Should not be empty");
        }

        /**
         * Verifies that empty() returns a BudgetID with null internal value.
         * <p>
         * empty() is a semantic factory for creating new/unassigned IDs.
         * <p>
         * Bug it could reveal: empty() returns wrong value or non-null internal.
         */
        @Test
        @DisplayName("empty() returns BudgetID with null value")
        void empty_returnsNullValue() {
            BudgetID budgetID = BudgetID.empty();

            assertNotNull(budgetID, "BudgetID instance should not be null");
            assertNull(budgetID.value(), "Internal value should be null");
            assertTrue(budgetID.isEmpty(), "Should be considered empty");
        }
    }

    // ========================================
    // PRIORITY MEDIUM - State Methods
    // ========================================

    @Nested
    @DisplayName("State Methods")
    class StateMethodsTests {

        /**
         * Verifies that isEmpty() returns true when value is null.
         * <p>
         * Bug it could reveal: isEmpty() logic inverted or incorrect.
         */
        @Test
        @DisplayName("isEmpty() with null value returns true")
        void isEmpty_withNullValue_returnsTrue() {
            BudgetID budgetID = BudgetID.of(null);

            assertTrue(budgetID.isEmpty(),
                    "isEmpty() should return true for null value");
        }

        /**
         * Verifies that isEmpty() returns false when value is positive.
         * <p>
         * Bug it could reveal: Positive values incorrectly treated as empty.
         */
        @Test
        @DisplayName("isEmpty() with positive value returns false")
        void isEmpty_withPositiveValue_returnsFalse() {
            BudgetID budgetID = BudgetID.of(42L);

            assertFalse(budgetID.isEmpty(),
                    "isEmpty() should return false for positive value");
        }

        /**
         * Verifies that isEmpty() returns false when value is zero.
         * <p>
         * Zero is a valid ID, not empty.
         * <p>
         * Bug it could reveal: Zero incorrectly treated as empty/null.
         */
        @Test
        @DisplayName("isEmpty() with zero returns false")
        void isEmpty_withZero_returnsFalse() {
            BudgetID budgetID = BudgetID.of(0L);

            assertFalse(budgetID.isEmpty(),
                    "isEmpty() should return false for zero (zero is a valid ID)");
        }

        /**
         * Verifies that value() returns the stored value correctly.
         * <p>
         * Bug it could reveal: Getter returns wrong value or transforms it.
         */
        @Test
        @DisplayName("value() returns stored value")
        void value_returnsStoredValue() {
            Long expectedValue = 999L;
            BudgetID budgetID = BudgetID.of(expectedValue);

            assertEquals(expectedValue, budgetID.value(),
                    "value() should return the exact stored value");
        }
    }

    // ========================================
    // PRIORITY MEDIUM - Equality and HashCode
    // ========================================

    @Nested
    @DisplayName("Equality and HashCode")
    class EqualityTests {

        /**
         * Verifies that two BudgetIDs with the same value are equal.
         * <p>
         * This is the core equality contract for Value Objects.
         * <p>
         * Bug it could reveal: equals() compares references instead of values.
         */
        @Test
        @DisplayName("equals() with same value returns true")
        void equals_sameValue_returnsTrue() {
            BudgetID id1 = BudgetID.of(100L);
            BudgetID id2 = BudgetID.of(100L);

            assertEquals(id1, id2, "BudgetIDs with same value should be equal");
            assertEquals(id2, id1, "Equality should be symmetric");
        }

        /**
         * Verifies that two BudgetIDs with different values are not equal.
         * <p>
         * Bug it could reveal: equals() always returns true or ignores value.
         */
        @Test
        @DisplayName("equals() with different values returns false")
        void equals_differentValue_returnsFalse() {
            BudgetID id1 = BudgetID.of(100L);
            BudgetID id2 = BudgetID.of(200L);

            assertNotEquals(id1, id2, "BudgetIDs with different values should not be equal");
        }

        /**
         * Verifies that two BudgetIDs with null values are equal.
         * <p>
         * Both represent "new/unassigned" state.
         * <p>
         * Bug it could reveal: Null comparison causes NPE or wrong result.
         */
        @Test
        @DisplayName("equals() with both null values returns true")
        void equals_bothNull_returnsTrue() {
            BudgetID id1 = BudgetID.empty();
            BudgetID id2 = BudgetID.of(null);

            assertEquals(id1, id2, "Both null BudgetIDs should be equal");
        }

        /**
         * Verifies that BudgetID with null is not equal to one with a value.
         * <p>
         * Bug it could reveal: Null and value comparison incorrect.
         */
        @Test
        @DisplayName("equals() with one null and one value returns false")
        void equals_oneNullOneValue_returnsFalse() {
            BudgetID nullId = BudgetID.empty();
            BudgetID valueId = BudgetID.of(100L);

            assertNotEquals(nullId, valueId, "Null and value BudgetIDs should not be equal");
            assertNotEquals(valueId, nullId, "Comparison should be symmetric");
        }

        /**
         * Verifies reflexivity: an object equals itself.
         * <p>
         * Bug it could reveal: Reflexivity contract violated.
         */
        @Test
        @DisplayName("equals() with same instance returns true (reflexivity)")
        void equals_sameInstance_returnsTrue() {
            BudgetID id = BudgetID.of(50L);

            assertEquals(id, id, "Object should equal itself");
        }

        /**
         * Verifies that comparing with null returns false, not NPE.
         * <p>
         * Bug it could reveal: NPE when comparing with null.
         */
        @Test
        @DisplayName("equals() with null literal returns false")
        void equals_null_returnsFalse() {
            BudgetID id = BudgetID.of(100L);

            assertNotEquals(null, id, "BudgetID should not equal null");
            assertFalse(id.equals(null), "equals(null) should return false");
        }

        /**
         * Verifies that comparing with different type returns false.
         * <p>
         * Bug it could reveal: ClassCastException or incorrect type handling.
         */
        @Test
        @DisplayName("equals() with different type returns false")
        void equals_differentType_returnsFalse() {
            BudgetID id = BudgetID.of(100L);
            String differentType = "100";

            assertFalse(id.equals(differentType),
                    "BudgetID should not equal different types");
        }

        /**
         * Verifies that equal objects have the same hashCode.
         * <p>
         * This is required by the hashCode contract.
         * <p>
         * Bug it could reveal: hashCode contract violated.
         */
        @Test
        @DisplayName("hashCode() same for equal objects")
        void hashCode_sameValue_sameHash() {
            BudgetID id1 = BudgetID.of(100L);
            BudgetID id2 = BudgetID.of(100L);

            assertEquals(id1.hashCode(), id2.hashCode(),
                    "Equal objects must have same hashCode");
        }

        /**
         * Verifies that different values likely produce different hashCodes.
         * <p>
         * Note: This is not strictly required but indicates good hash distribution.
         * <p>
         * Bug it could reveal: Poor hash distribution (constant hash).
         */
        @Test
        @DisplayName("hashCode() different for different values (likely)")
        void hashCode_differentValue_likelyDifferentHash() {
            BudgetID id1 = BudgetID.of(100L);
            BudgetID id2 = BudgetID.of(200L);

            // Note: Different values CAN have same hash (collision), but shouldn't always
            assertNotEquals(id1.hashCode(), id2.hashCode(),
                    "Different values should likely have different hashCodes");
        }

        /**
         * Verifies hashCode consistency for null values.
         * <p>
         * Bug it could reveal: NPE when computing hash of null value.
         */
        @Test
        @DisplayName("hashCode() works with null value")
        void hashCode_nullValue_doesNotThrow() {
            BudgetID nullId = BudgetID.empty();

            assertDoesNotThrow(nullId::hashCode,
                    "hashCode() should not throw for null value");
        }
    }
}


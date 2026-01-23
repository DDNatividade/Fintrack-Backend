package com.apis.fintrack.domain.user;

import com.apis.fintrack.domain.user.model.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link RoleType} enum.
 *
 * <p>Tests cover enum values, query methods (isAdmin, hasSubscription),
 * and descriptions following specification-based testing approaches.</p>
 */
@DisplayName("RoleType Enum Tests")
class RoleTypeTest {

    // ==================== ENUM VALUES - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Enum Values")
    class EnumValuesTests {

        /**
         * Verifies that all expected roles exist.
         */
        @Test
        @DisplayName("Should have USER, ADMIN, and SUBSCRIBER values")
        void enumValues_shouldContainAllExpectedRoles() {
            assertNotNull(RoleType.valueOf("USER"));
            assertNotNull(RoleType.valueOf("ADMIN"));
            assertNotNull(RoleType.valueOf("SUBSCRIBER"));
            assertEquals(3, RoleType.values().length);
        }

        /**
         * Verifies that each role has a description.
         */
        @Test
        @DisplayName("Each role should have a description")
        void enumValues_shouldHaveDescriptions() {
            for (RoleType role : RoleType.values()) {
                assertNotNull(role.getDescription());
                assertFalse(role.getDescription().isEmpty());
            }
        }
    }

    // ==================== QUERY METHODS - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Query Methods")
    class QueryMethodsTests {

        /**
         * Verifies isAdmin() returns true for ADMIN.
         */
        @Test
        @DisplayName("ADMIN.isAdmin() should return true")
        void isAdmin_admin_shouldReturnTrue() {
            assertTrue(RoleType.ADMIN.isAdmin());
        }

        /**
         * Verifies isAdmin() returns false for USER.
         */
        @Test
        @DisplayName("USER.isAdmin() should return false")
        void isAdmin_user_shouldReturnFalse() {
            assertFalse(RoleType.USER.isAdmin());
        }

        /**
         * Verifies isAdmin() returns false for SUBSCRIBER.
         */
        @Test
        @DisplayName("SUBSCRIBER.isAdmin() should return false")
        void isAdmin_subscriber_shouldReturnFalse() {
            assertFalse(RoleType.SUBSCRIBER.isAdmin());
        }

        /**
         * Verifies hasSubscription() returns true for SUBSCRIBER.
         */
        @Test
        @DisplayName("SUBSCRIBER.hasSubscription() should return true")
        void hasSubscription_subscriber_shouldReturnTrue() {
            assertTrue(RoleType.SUBSCRIBER.hasSubscription());
        }

        /**
         * Verifies hasSubscription() returns true for ADMIN.
         */
        @Test
        @DisplayName("ADMIN.hasSubscription() should return true")
        void hasSubscription_admin_shouldReturnTrue() {
            assertTrue(RoleType.ADMIN.hasSubscription());
        }

        /**
         * Verifies hasSubscription() returns false for USER.
         */
        @Test
        @DisplayName("USER.hasSubscription() should return false")
        void hasSubscription_user_shouldReturnFalse() {
            assertFalse(RoleType.USER.hasSubscription());
        }
    }

    // ==================== DESCRIPTION - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Descriptions")
    class DescriptionTests {

        /**
         * Verifies USER description.
         */
        @Test
        @DisplayName("USER should have correct description")
        void user_shouldHaveCorrectDescription() {
            assertEquals("Standard user", RoleType.USER.getDescription());
        }

        /**
         * Verifies ADMIN description.
         */
        @Test
        @DisplayName("ADMIN should have correct description")
        void admin_shouldHaveCorrectDescription() {
            assertEquals("Administrator", RoleType.ADMIN.getDescription());
        }

        /**
         * Verifies SUBSCRIBER description.
         */
        @Test
        @DisplayName("SUBSCRIBER should have correct description")
        void subscriber_shouldHaveCorrectDescription() {
            assertEquals("Subscriber", RoleType.SUBSCRIBER.getDescription());
        }
    }
}


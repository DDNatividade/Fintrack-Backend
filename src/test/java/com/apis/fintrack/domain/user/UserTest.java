package com.apis.fintrack.domain.user;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.user.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link User} domain entity.
 *
 * <p>Tests cover factory method, constructor validation, domain behavior,
 * and query methods following specification-based testing approaches.</p>
 */
@DisplayName("User Domain Entity Tests")
class UserTest {

    private FullName validFullName;
    private Email validEmail;
    private Password validPassword;
    private BirthDate validBirthDate;
    private Money validFunds;

    @BeforeEach
    void setUp() {
        validFullName = FullName.of("John", "Doe");
        validEmail = Email.of("john.doe@example.com");
        validPassword = Password.of("ValidPass1");
        validBirthDate = BirthDate.of(LocalDate.now().minusYears(25));
        validFunds = Money.of(new BigDecimal("1000.00"));
    }

    // ==================== CONSTRUCTOR - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        /**
         * Verifies that the constructor creates a valid User.
         */
        @Test
        @DisplayName("Should create User with valid parameters")
        void constructor_withValidParams_shouldCreateUser() {
            User user = new User(
                    UserId.empty(),
                    validFullName,
                    validEmail,
                    validPassword,
                    validBirthDate,
                    validFunds
            );

            assertNotNull(user);
            assertTrue(user.getId().isEmpty());
            assertEquals(validFullName, user.getFullName());
            assertEquals(validEmail, user.getEmail());
            assertEquals(validPassword, user.getPassword());
            assertEquals(validBirthDate, user.getBirthDate());
            assertEquals(validFunds, user.getAvailableFunds());
            assertEquals(RoleType.USER, user.getRole());
        }

        /**
         * Verifies that null funds defaults to zero.
         */
        @Test
        @DisplayName("Should default to zero funds when null is provided")
        void constructor_nullFunds_shouldDefaultToZero() {
            User user = new User(
                    UserId.empty(),
                    validFullName,
                    validEmail,
                    validPassword,
                    validBirthDate,
                    null
            );

            assertTrue(user.getAvailableFunds().isZero());
        }

        /**
         * Verifies that null fullName throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException when fullName is null")
        void constructor_nullFullName_shouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, () ->
                    new User(
                            UserId.empty(),
                            null,
                            validEmail,
                            validPassword,
                            validBirthDate,
                            validFunds
                    )
            );
        }

        /**
         * Verifies that null email throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException when email is null")
        void constructor_nullEmail_shouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, () ->
                    new User(
                            UserId.empty(),
                            validFullName,
                            null,
                            validPassword,
                            validBirthDate,
                            validFunds
                    )
            );
        }

        /**
         * Verifies that null password throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException when password is null")
        void constructor_nullPassword_shouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, () ->
                    new User(
                            UserId.empty(),
                            validFullName,
                            validEmail,
                            null,
                            validBirthDate,
                            validFunds
                    )
            );
        }

        /**
         * Verifies that null birthDate throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException when birthDate is null")
        void constructor_nullBirthDate_shouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, () ->
                    new User(
                            UserId.empty(),
                            validFullName,
                            validEmail,
                            validPassword,
                            null,
                            validFunds
                    )
            );
        }

        /**
         * Verifies that default role is USER.
         */
        @Test
        @DisplayName("Should assign USER role by default")
        void constructor_shouldAssignUserRoleByDefault() {
            User user = new User(
                    UserId.empty(),
                    validFullName,
                    validEmail,
                    validPassword,
                    validBirthDate,
                    validFunds
            );

            assertEquals(RoleType.USER, user.getRole());
        }
    }

    // ==================== FACTORY METHOD: create() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: create()")
    class CreateFactoryTests {

        /**
         * Verifies that create() returns a new User with correct initial state.
         */
        @Test
        @DisplayName("Should create User with correct initial state")
        void create_shouldCreateUserWithCorrectState() {
            User user = User.create(
                    validFullName,
                    validEmail,
                    validPassword,
                    validBirthDate
            );

            assertNotNull(user);
            assertTrue(user.getId().isEmpty());
            assertEquals(validFullName, user.getFullName());
            assertEquals(validEmail, user.getEmail());
            assertEquals(validPassword, user.getPassword());
            assertEquals(validBirthDate, user.getBirthDate());
            assertTrue(user.getAvailableFunds().isZero());
            assertEquals(RoleType.USER, user.getRole());
        }

        /**
         * Verifies that create() assigns empty UserId.
         */
        @Test
        @DisplayName("Should assign empty UserId")
        void create_shouldAssignEmptyId() {
            User user = User.create(
                    validFullName,
                    validEmail,
                    validPassword,
                    validBirthDate
            );

            assertTrue(user.getId().isEmpty());
        }

        /**
         * Verifies that create() sets zero available funds.
         */
        @Test
        @DisplayName("Should set zero available funds")
        void create_shouldSetZeroFunds() {
            User user = User.create(
                    validFullName,
                    validEmail,
                    validPassword,
                    validBirthDate
            );

            assertTrue(user.getAvailableFunds().isZero());
        }
    }

    // ==================== BUSINESS METHODS - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Business Methods")
    class BusinessMethodsTests {

        /**
         * Verifies changeName() updates the name.
         */
        @Test
        @DisplayName("changeName() should update the name")
        void changeName_shouldUpdateName() {
            User user = User.create(validFullName, validEmail, validPassword, validBirthDate);

            user.changeName("Jane");

            assertEquals("Jane", user.getFullName().getName());
            assertEquals("Doe", user.getFullName().getSurname()); // Surname unchanged
        }

        /**
         * Verifies changeSurname() updates the surname.
         */
        @Test
        @DisplayName("changeSurname() should update the surname")
        void changeSurname_shouldUpdateSurname() {
            User user = User.create(validFullName, validEmail, validPassword, validBirthDate);

            user.changeSurname("Smith");

            assertEquals("John", user.getFullName().getName()); // Name unchanged
            assertEquals("Smith", user.getFullName().getSurname());
        }

        /**
         * Verifies changeEmail() updates the email.
         */
        @Test
        @DisplayName("changeEmail() should update the email")
        void changeEmail_shouldUpdateEmail() {
            User user = User.create(validFullName, validEmail, validPassword, validBirthDate);
            Email newEmail = Email.of("new.email@example.com");

            user.changeEmail(newEmail);

            assertEquals(newEmail, user.getEmail());
        }

        /**
         * Verifies changeEmail() with null throws exception.
         */
        @Test
        @DisplayName("changeEmail() with null should throw NullPointerException")
        void changeEmail_null_shouldThrowException() {
            User user = User.create(validFullName, validEmail, validPassword, validBirthDate);

            assertThrows(NullPointerException.class, () -> user.changeEmail(null));
        }

        /**
         * Verifies changePassword() updates the password.
         */
        @Test
        @DisplayName("changePassword() should update the password")
        void changePassword_shouldUpdatePassword() {
            User user = User.create(validFullName, validEmail, validPassword, validBirthDate);
            Password newPassword = Password.of("NewPassword1");

            user.changePassword(newPassword);

            assertEquals(newPassword, user.getPassword());
        }

        /**
         * Verifies changePassword() with null throws exception.
         */
        @Test
        @DisplayName("changePassword() with null should throw NullPointerException")
        void changePassword_null_shouldThrowException() {
            User user = User.create(validFullName, validEmail, validPassword, validBirthDate);

            assertThrows(NullPointerException.class, () -> user.changePassword(null));
        }

        /**
         * Verifies changeBirthDate() updates the birth date.
         */
        @Test
        @DisplayName("changeBirthDate() should update the birth date")
        void changeBirthDate_shouldUpdateBirthDate() {
            User user = User.create(validFullName, validEmail, validPassword, validBirthDate);
            BirthDate newBirthDate = BirthDate.of(LocalDate.of(1990, 5, 15));

            user.changeBirthDate(newBirthDate);

            assertEquals(newBirthDate, user.getBirthDate());
        }

        /**
         * Verifies changeBirthDate() with null throws exception.
         */
        @Test
        @DisplayName("changeBirthDate() with null should throw NullPointerException")
        void changeBirthDate_null_shouldThrowException() {
            User user = User.create(validFullName, validEmail, validPassword, validBirthDate);

            assertThrows(NullPointerException.class, () -> user.changeBirthDate(null));
        }
    }

    // ==================== FUNDS MANAGEMENT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Funds Management")
    class FundsManagementTests {

        /**
         * Verifies addFunds() increases available funds.
         */
        @Test
        @DisplayName("addFunds() should increase available funds")
        void addFunds_shouldIncreaseFunds() {
            User user = new User(
                    UserId.empty(),
                    validFullName,
                    validEmail,
                    validPassword,
                    validBirthDate,
                    Money.of(new BigDecimal("100.00"))
            );

            user.addFunds(Money.of(new BigDecimal("50.00")));

            assertEquals(new BigDecimal("150.00"), user.getAvailableFunds().getAmount());
        }

        /**
         * Verifies withdrawFunds() decreases available funds.
         */
        @Test
        @DisplayName("withdrawFunds() should decrease available funds")
        void withdrawFunds_shouldDecreaseFunds() {
            User user = new User(
                    UserId.empty(),
                    validFullName,
                    validEmail,
                    validPassword,
                    validBirthDate,
                    Money.of(new BigDecimal("100.00"))
            );

            user.withdrawFunds(Money.of(new BigDecimal("30.00")));

            assertEquals(new BigDecimal("70.00"), user.getAvailableFunds().getAmount());
        }
    }
}


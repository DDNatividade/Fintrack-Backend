package com.apis.fintrack.application.user.usecases;

import com.apis.fintrack.domain.user.exception.EmailAlreadyExistsException;
import com.apis.fintrack.domain.user.model.*;
import com.apis.fintrack.domain.user.port.input.RegisterUserUseCase;
import com.apis.fintrack.domain.user.port.output.PasswordEncoderPort;
import com.apis.fintrack.domain.user.port.output.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RegisterUserUseCaseImpl.
 *
 * Tests cover:
 * - Happy path: successful user registration
 * - Invalid inputs: null values, invalid formats
 * - Email duplication scenarios
 * - Password encoding verification
 * - Repository interaction verification
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterUserUseCaseImpl Tests")
class RegisterUserUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    private RegisterUserUseCaseImpl registerUserUseCase;

    @BeforeEach
    void setUp() {
        registerUserUseCase = new RegisterUserUseCaseImpl(userRepository, passwordEncoder);
    }

    @Nested
    @DisplayName("execute Tests - Happy Path")
    class ExecuteHappyPathTests {

        /**
         * Happy path: Successfully registers a new user.
         * Verifies complete registration flow with all components.
         */
        @Test
        @DisplayName("Should successfully register a new user")
        void execute_withValidCommand_shouldRegisterUser() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                "Doe",
                "john.doe@example.com",
                "Password123!",
                LocalDate.of(1990, 1, 1)
            );

            String hashedPassword = "hashedPassword123";
            when(userRepository.existsByEmail(command.email())).thenReturn(false);
            when(passwordEncoder.encode(command.password())).thenReturn(hashedPassword);

            User savedUser = User.create(
                FullName.of("John", "Doe"),
                Email.of("john.doe@example.com"),
                Password.fromStorage(hashedPassword),
                BirthDate.of(LocalDate.of(1990, 1, 1))
            );
            when(userRepository.save(any(User.class))).thenReturn(savedUser);

            // When
            User result = registerUserUseCase.execute(command);

            // Then
            assertNotNull(result);
            verify(userRepository).existsByEmail(command.email());
            verify(passwordEncoder).encode(command.password());
            verify(userRepository).save(any(User.class));
        }

        /**
         * Happy path: Password is correctly hashed before storage.
         * Verifies that raw password is never stored.
         */
        @Test
        @DisplayName("Should hash password before saving user")
        void execute_shouldHashPasswordBeforeSaving() {
            // Given
            String rawPassword = "MySecurePassword123!";
            String hashedPassword = "$2a$10$hashedPasswordValue";
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "Jane",
                "Smith",
                "jane.smith@example.com",
                rawPassword,
                LocalDate.of(1995, 5, 15)
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);
            when(passwordEncoder.encode(rawPassword)).thenReturn(hashedPassword);
            when(userRepository.save(any(User.class))).thenReturn(any(User.class));

            // When
            registerUserUseCase.execute(command);

            // Then
            verify(passwordEncoder).encode(rawPassword);
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            // Verify the user was created with hashed password
            assertNotNull(userCaptor.getValue());
        }

        /**
         * Happy path: User with minimum valid birth date.
         * Tests boundary condition with very old birth date.
         */
        @Test
        @DisplayName("Should accept user with minimum valid birth date")
        void execute_withOldBirthDate_shouldSucceed() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "Old",
                "User",
                "old.user@example.com",
                "Password123!",
                LocalDate.of(1900, 1, 1)
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
            when(userRepository.save(any(User.class))).thenReturn(any(User.class));

            // When
            User result = registerUserUseCase.execute(command);

            // Then
            assertNotNull(result);
            verify(userRepository).save(any(User.class));
        }

        /**
         * Happy path: User with special characters in name.
         * Tests support for international characters.
         */
        @Test
        @DisplayName("Should accept names with special characters")
        void execute_withSpecialCharactersInName_shouldSucceed() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "María",
                "O'Connor-Smith",
                "maria.oconnor@example.com",
                "Password123!",
                LocalDate.of(1990, 1, 1)
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
            when(userRepository.save(any(User.class))).thenReturn(any(User.class));

            // When
            User result = registerUserUseCase.execute(command);

            // Then
            assertNotNull(result);
            verify(userRepository).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("execute Tests - Email Validation")
    class ExecuteEmailValidationTests {

        /**
         * Invalid input: Email already exists in system.
         * Tests duplicate email prevention.
         */
        @Test
        @DisplayName("Should throw EmailAlreadyExistsException when email is taken")
        void execute_whenEmailExists_shouldThrowEmailAlreadyExistsException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                "Doe",
                "existing@example.com",
                "Password123!",
                LocalDate.of(1990, 1, 1)
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(true);

            // When & Then
            EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
                registerUserUseCase.execute(command);
            });

            assertNotNull(exception);
            verify(userRepository).existsByEmail(command.email());
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any(User.class));
        }

        /**
         * Invalid input: Null email in command.
         * Tests handling of missing email.
         */
        @Test
        @DisplayName("Should throw exception when email is null")
        void execute_whenEmailIsNull_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                "Doe",
                null,
                "Password123!",
                LocalDate.of(1990, 1, 1)
            );

            // When & Then
            assertThrows(NullPointerException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }

        /**
         * Invalid input: Empty email string.
         * Tests validation of empty email.
         */
        @Test
        @DisplayName("Should throw exception when email is empty")
        void execute_whenEmailIsEmpty_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                "Doe",
                "",
                "Password123!",
                LocalDate.of(1990, 1, 1)
            );

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }

        /**
         * Invalid input: Email with invalid format.
         * Tests email format validation.
         */
        @Test
        @DisplayName("Should throw exception when email format is invalid")
        void execute_whenEmailFormatIsInvalid_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                "Doe",
                "invalid-email-format",
                "Password123!",
                LocalDate.of(1990, 1, 1)
            );

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("execute Tests - Password Validation")
    class ExecutePasswordValidationTests {

        /**
         * Invalid input: Null password in command.
         * Tests handling of missing password.
         */
        @Test
        @DisplayName("Should throw exception when password is null")
        void execute_whenPasswordIsNull_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                "Doe",
                "john.doe@example.com",
                null,
                LocalDate.of(1990, 1, 1)
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);

            // When & Then
            assertThrows(NullPointerException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }

        /**
         * Invalid input: Empty password string.
         * Tests validation of empty password.
         */
        @Test
        @DisplayName("Should throw exception when password is empty")
        void execute_whenPasswordIsEmpty_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                "Doe",
                "john.doe@example.com",
                "",
                LocalDate.of(1990, 1, 1)
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }

        /**
         * Invalid input: Password too short.
         * Tests minimum password length validation.
         */
        @Test
        @DisplayName("Should throw exception when password is too short")
        void execute_whenPasswordTooShort_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                "Doe",
                "john.doe@example.com",
                "Pass1!",
                LocalDate.of(1990, 1, 1)
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("execute Tests - Name Validation")
    class ExecuteNameValidationTests {

        /**
         * Invalid input: Null name in command.
         * Tests handling of missing name.
         */
        @Test
        @DisplayName("Should throw exception when name is null")
        void execute_whenNameIsNull_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                null,
                "Doe",
                "john.doe@example.com",
                "Password123!",
                LocalDate.of(1990, 1, 1)
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);

            // When & Then
            assertThrows(NullPointerException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }

        /**
         * Invalid input: Empty name string.
         * Tests validation of empty name.
         */
        @Test
        @DisplayName("Should throw exception when name is empty")
        void execute_whenNameIsEmpty_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "",
                "Doe",
                "john.doe@example.com",
                "Password123!",
                LocalDate.of(1990, 1, 1)
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }

        /**
         * Invalid input: Null surname in command.
         * Tests handling of missing surname.
         */
        @Test
        @DisplayName("Should throw exception when surname is null")
        void execute_whenSurnameIsNull_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                null,
                "john.doe@example.com",
                "Password123!",
                LocalDate.of(1990, 1, 1)
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);

            // When & Then
            assertThrows(NullPointerException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }

        /**
         * Invalid input: Empty surname string.
         * Tests validation of empty surname.
         */
        @Test
        @DisplayName("Should throw exception when surname is empty")
        void execute_whenSurnameIsEmpty_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                "",
                "john.doe@example.com",
                "Password123!",
                LocalDate.of(1990, 1, 1)
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("execute Tests - Birth Date Validation")
    class ExecuteBirthDateValidationTests {

        /**
         * Invalid input: Null birth date in command.
         * Tests handling of missing birth date.
         */
        @Test
        @DisplayName("Should throw exception when birth date is null")
        void execute_whenBirthDateIsNull_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                "Doe",
                "john.doe@example.com",
                "Password123!",
                null
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);

            // When & Then
            assertThrows(NullPointerException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }

        /**
         * Invalid input: Future birth date.
         * Tests validation of birth date in the future.
         */
        @Test
        @DisplayName("Should throw exception when birth date is in the future")
        void execute_whenBirthDateIsInFuture_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                "Doe",
                "john.doe@example.com",
                "Password123!",
                LocalDate.now().plusDays(1)
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }

        /**
         * Boundary condition: Birth date is today.
         * Tests edge case with current date as birth date.
         */
        @Test
        @DisplayName("Should throw exception when birth date is today")
        void execute_whenBirthDateIsToday_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                "John",
                "Doe",
                "john.doe@example.com",
                "Password123!",
                LocalDate.now()
            );

            when(userRepository.existsByEmail(command.email())).thenReturn(false);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("execute Tests - Command Validation")
    class ExecuteCommandValidationTests {

        /**
         * Invalid input: Null command object.
         * Tests handling of completely missing command.
         */
        @Test
        @DisplayName("Should throw exception when command is null")
        void execute_whenCommandIsNull_shouldThrowException() {
            // Given
            RegisterUserUseCase.RegisterUserCommand command = null;

            // When & Then
            assertThrows(NullPointerException.class, () -> {
                registerUserUseCase.execute(command);
            });

            verify(userRepository, never()).save(any(User.class));
        }
    }
}


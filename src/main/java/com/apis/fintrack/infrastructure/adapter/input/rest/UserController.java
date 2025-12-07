package com.apis.fintrack.infrastructure.adapter.input.rest;

import com.apis.fintrack.domain.user.model.role.model.RoleType;
import com.apis.fintrack.domain.user.model.User;
import com.apis.fintrack.domain.user.port.input.DeleteUserUseCase;
import com.apis.fintrack.domain.user.port.input.FindUserUseCase;
import com.apis.fintrack.domain.user.port.input.RegisterUserUseCase;
import com.apis.fintrack.domain.user.port.input.UpdateUserUseCase;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.CreateUserDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch.*;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit.PagedResponse;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit.ShowUserDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para operaciones de usuario.
 *
 * Este es un Adaptador Primario (Input Adapter) que recibe peticiones HTTP
 * y las traduce a llamadas a los puertos de entrada (Use Cases).
 */
@RestController
@RequestMapping("/apis/users")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final FindUserUseCase findUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UserRestMapper mapper;

    public UserController(RegisterUserUseCase registerUserUseCase,
                          FindUserUseCase findUserUseCase,
                          UpdateUserUseCase updateUserUseCase,
                          DeleteUserUseCase deleteUserUseCase,
                          UserRestMapper mapper) {
        this.registerUserUseCase = registerUserUseCase;
        this.findUserUseCase = findUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.mapper = mapper;
    }

    // ==================== OPERACIONES DE LECTURA ====================

    @GetMapping
    public ResponseEntity<PagedResponse<ShowUserDTO>> showUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<User> users = findUserUseCase.findAll();
        List<ShowUserDTO> userDTOs = mapper.toShowUserDTOList(users);

        PagedResponse<ShowUserDTO> response = new PagedResponse<>(
                userDTOs,
                0, // totalPages - simplificado
                userDTOs.size(),
                size,
                userDTOs.size() < size
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowUserDTO> showUser(@PathVariable Long id) {
        User user = findUserUseCase.findById(id);
        ShowUserDTO userDTO = mapper.toShowUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/roles/{role}")
    public ResponseEntity<PagedResponse<ShowUserDTO>> showUserByRole(
            @PathVariable RoleType role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<User> users = findUserUseCase.findByRole(role, page, size);
        List<ShowUserDTO> userDTOs = mapper.toShowUserDTOList(users);

        PagedResponse<ShowUserDTO> response = new PagedResponse<>(
                userDTOs,
                0,
                userDTOs.size(),
                size,
                userDTOs.size() < size
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dates")
    public ResponseEntity<PagedResponse<ShowUserDTO>> showUserByDate(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<User> users = findUserUseCase.findByBirthDateBetween(startDate, endDate, page, size);
        List<ShowUserDTO> userDTOs = mapper.toShowUserDTOList(users);

        PagedResponse<ShowUserDTO> response = new PagedResponse<>(
                userDTOs,
                0,
                userDTOs.size(),
                size,
                userDTOs.size() < size
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{name}/surname/{surname}")
    public ResponseEntity<ShowUserDTO> showUserByNameAndSurname(
            @PathVariable String name,
            @PathVariable String surname) {
        User user = findUserUseCase.findByNameAndSurname(name, surname);
        ShowUserDTO userDTO = mapper.toShowUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<ShowUserDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("No authenticated user found");
        }
        User user = findUserUseCase.findByEmail(userDetails.getUsername());
        ShowUserDTO userDTO = mapper.toShowUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    // ==================== OPERACIONES DE ESCRITURA ====================

    @PostMapping
    public ResponseEntity<ShowUserDTO> postUser(@Valid @RequestBody CreateUserDTO userDTO) {
        RegisterUserUseCase.RegisterUserCommand command = mapper.toCommand(userDTO);
        User user = registerUserUseCase.execute(command);
        ShowUserDTO showUserDTO = mapper.toShowUserDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(showUserDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deleteUserUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== OPERACIONES DE ACTUALIZACIÃ“N (PATCH) ====================

    @PatchMapping("/{id}/name")
    public ResponseEntity<ShowUserDTO> changeUserName(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserNameDTO dto) {
        User user = updateUserUseCase.updateName(id, dto.getName());
        return ResponseEntity.ok(mapper.toShowUserDTO(user));
    }

    @PatchMapping("/{id}/surname")
    public ResponseEntity<ShowUserDTO> changeUserSurname(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserSurnameDTO dto) {
        User user = updateUserUseCase.updateSurname(id, dto.getSurname());
        return ResponseEntity.ok(mapper.toShowUserDTO(user));
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<ShowUserDTO> changeUserEmail(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserEmailDTO dto) {
        User user = updateUserUseCase.updateEmail(id, dto.email());
        return ResponseEntity.ok(mapper.toShowUserDTO(user));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ShowUserDTO> changeUserPassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserPasswordDTO dto) {
        User user = updateUserUseCase.updatePassword(id, dto.password());
        return ResponseEntity.ok(mapper.toShowUserDTO(user));
    }

    @PatchMapping("/{id}/birthday")
    public ResponseEntity<ShowUserDTO> changeUserBirthday(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserBirthdayDTO dto) {
        User user = updateUserUseCase.updateBirthDate(id, dto.birthday());
        return ResponseEntity.ok(mapper.toShowUserDTO(user));
    }
}





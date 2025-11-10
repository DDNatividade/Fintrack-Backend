package com.apis.fintrack.Controller;

import com.apis.fintrack.DTO.PagedResponse;
import com.apis.fintrack.DTO.UserEntity.Entry.CreateUserDTO;
import com.apis.fintrack.DTO.UserEntity.Entry.Patch.*;
import com.apis.fintrack.DTO.UserEntity.Exit.ShowUserDTO;
import com.apis.fintrack.Entity.RoleEnum;
import com.apis.fintrack.Entity.UserEntity;
import com.apis.fintrack.Mapper.UserMapStruct;
import com.apis.fintrack.Service.Impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;


@RestController
@RequestMapping("/apis/users")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    private UserMapStruct userMapStruct;

    @GetMapping
    public ResponseEntity<PagedResponse<ShowUserDTO>> showUsers(Pageable pageable) {
        Page<UserEntity> usersEntityPage = userService.showAll(pageable);
        Page<ShowUserDTO> userDTOPage = usersEntityPage.map(userService::toShowUserDTO);

        PagedResponse<ShowUserDTO> userDTOPagedResponse = new PagedResponse<>(
                userDTOPage.getContent(),
                userDTOPage.getTotalPages(),
                userDTOPage.getTotalElements(),
                userDTOPage.getSize(),
                userDTOPage.isLast()
                );
        return ResponseEntity.status(HttpStatus.OK).body(userDTOPagedResponse);
    }

    @PostMapping
    public ResponseEntity<ShowUserDTO> postUser(@Valid @RequestBody CreateUserDTO userDTO) {

        UserEntity user = userService.RegisterNewUser(userDTO);
        userService.save(user);
        ShowUserDTO showUserDTO = userService.toShowUserDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(showUserDTO);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowUserDTO> showUser(@PathVariable @Valid Long id) {
       UserEntity user= userService.findByUserId(id);
        ShowUserDTO userDTO= userService.toShowUserDTO(user);
        return ResponseEntity.status(HttpStatus.FOUND).body(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable @Valid Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles/{role}")
    public ResponseEntity<PagedResponse<ShowUserDTO>>
    showUserByRole(@PathVariable @Valid RoleEnum role, Pageable pageable) {
        Page<UserEntity> pageUserentity=userService.findAllByRole(role, pageable);
        Page<ShowUserDTO> userDTOPage=pageUserentity.map(userService::toShowUserDTO);

        PagedResponse<ShowUserDTO> userDTOPagedResponse = new PagedResponse<>(
                userDTOPage.getContent(),
                userDTOPage.getTotalPages(),
                userDTOPage.getTotalElements(),
                userDTOPage.getSize(),
                userDTOPage.isLast()
        );

        return ResponseEntity.status(HttpStatus.OK).body(userDTOPagedResponse);
    }

    @GetMapping("/dates")
    public ResponseEntity<PagedResponse<ShowUserDTO>>
    showUserByDate(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, Pageable pageable) {
        Page<UserEntity> userEntities = userService.findByDate(startDate, endDate, pageable);
        Page<ShowUserDTO> ShowUserDTOPage=userEntities.map(userService::toShowUserDTO);
        PagedResponse<ShowUserDTO> serDTOPagedResponse = new PagedResponse<>(
                ShowUserDTOPage.getContent(),
                ShowUserDTOPage.getTotalPages(),
                ShowUserDTOPage.getTotalElements(),
                ShowUserDTOPage.getSize(),
                ShowUserDTOPage.isLast()
        );

        return ResponseEntity.status(HttpStatus.OK).body(serDTOPagedResponse);

    }

    @GetMapping("/name/{name}/surname/{surname}")
    public ResponseEntity<ShowUserDTO>
    showUserByNameAndSurname(
            @Valid @PathVariable  String name,
            @Valid  @PathVariable String surname){
        UserEntity user = userService.findByNameAndSurname(name, surname);
        ShowUserDTO userDTO= userService.toShowUserDTO(user);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);

    }


    @PatchMapping("/{id}/name")
    public ResponseEntity<ShowUserDTO> changeUserName(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserNameDTO dto
    ) {
        UserEntity user = userService.findByUserId(id);
        user.setName(dto.getName());
        userService.save(user);

        ShowUserDTO showUserDTO = userService.toShowUserDTO(user);
        return ResponseEntity.ok(showUserDTO);
    }


    @PatchMapping("/{id}/surname")
    public ResponseEntity<ShowUserDTO> changeUserSurname(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserSurnameDTO dto
    ) {
        UserEntity user = userService.findByUserId(id);
        user.setSurname(dto.getSurname());
        userService.save(user);

        ShowUserDTO showUserDTO = userService.toShowUserDTO(user);
        return ResponseEntity.ok(showUserDTO);
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<ShowUserDTO> changeUserEmail(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserEmailDTO dto
    ) {
        UserEntity user = userService.findByUserId(id);
        user.setEmail(dto.getEmail());
        userService.save(user);

        ShowUserDTO showUserDTO = userService.toShowUserDTO(user);
        return ResponseEntity.ok(showUserDTO);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ShowUserDTO> changeUserPassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserPasswordDTO dto
    ) {
        UserEntity user = userService.findByUserId(id);
        user.setPassword(dto.getPassword());

        userService.save(user);
        ShowUserDTO showUserDTO = userService.toShowUserDTO(user);
        return ResponseEntity.ok(showUserDTO);
    }


    @PatchMapping("/{id}/birthday")
    public ResponseEntity<ShowUserDTO> changeUserBirthday(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserBirthdayDTO dto
    ) {
        UserEntity user = userService.findByUserId(id);
        user.setBirthDate(dto.getBirthday());
        userService.save(user);

        ShowUserDTO showUserDTO = userService.toShowUserDTO(user);
        return ResponseEntity.ok(showUserDTO);
    }


    @GetMapping("/me")
    public ResponseEntity<ShowUserDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("There are no users authenticated");
        }

        String email = userDetails.getUsername();
        var userEntity = userService.findByEmail(email);
        ShowUserDTO userDTO = userService.toShowUserDTO(userEntity);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
}



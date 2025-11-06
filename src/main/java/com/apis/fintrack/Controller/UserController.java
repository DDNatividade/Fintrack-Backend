package com.apis.fintrack.Controller;

import com.apis.fintrack.DTO.PagedResponse;
import com.apis.fintrack.DTO.UserEntity.Entry.CreateUserDTO;
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
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;


//UserControllers
@RestController
@RequestMapping("/apis/users")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserMapStruct userMapStruct;

    @GetMapping
    public ResponseEntity<PagedResponse<ShowUserDTO>> showUsers(Pageable pageable) {
        Page<UserEntity> usersEntityPage = userService.showAll(pageable);
        Page<ShowUserDTO> userDTOPage = usersEntityPage.map(userMapStruct::toShowUserDTO);

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

        UserEntity user = userMapStruct.toUserEntity(userDTO);
        userService.save(user);
        ShowUserDTO showUserDTO = userMapStruct.toShowUserDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(showUserDTO);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowUserDTO> showUser(@PathVariable @Valid Long id) {
       UserEntity user= userService.findByUserId(id);
        ShowUserDTO userDTO= userMapStruct.toShowUserDTO(user);
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
        Page<ShowUserDTO> userDTOPage=pageUserentity.map(userMapStruct::toShowUserDTO);

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
        Page<ShowUserDTO> ShowUserDTOPage=userEntities.map(userMapStruct::toShowUserDTO);
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
        ShowUserDTO userDTO= userMapStruct.toShowUserDTO(user);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);

    }




}

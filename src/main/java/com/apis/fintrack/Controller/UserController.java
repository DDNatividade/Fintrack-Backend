package com.apis.fintrack.Controller;

import com.apis.fintrack.DTO.UserEntity.Entry.CreateUserDTO;
import com.apis.fintrack.DTO.UserEntity.Exit.ShowUserDTO;
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


//UserControllers
@RestController
@RequestMapping("/apis/users")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserMapStruct userMapStruct;

    @GetMapping
    public ResponseEntity<Page<ShowUserDTO>> showUsers(Pageable pageable) {
        Page<UserEntity> usersEntityPage = userService.showAll(pageable);
        Page<ShowUserDTO> userDTOPage = usersEntityPage.map(userMapStruct::toShowUserDTO);

        return ResponseEntity.status(HttpStatus.OK).body(userDTOPage);

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


}

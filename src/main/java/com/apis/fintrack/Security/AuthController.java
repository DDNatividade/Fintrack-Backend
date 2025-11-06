package com.apis.fintrack.Security;

import com.apis.fintrack.DTO.UserEntity.Entry.CreateUserDTO;
import com.apis.fintrack.DTO.UserEntity.Entry.UserLoginDTO;
import com.apis.fintrack.Security.SecurityDTO.AuthResponse;
import com.apis.fintrack.Security.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid CreateUserDTO user){
    return ResponseEntity.ok(service.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid UserLoginDTO user){
        return ResponseEntity.ok(service.authenticate(user));
    }
}

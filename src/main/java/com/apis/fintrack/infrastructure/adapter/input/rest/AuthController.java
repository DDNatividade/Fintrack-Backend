package com.apis.fintrack.infrastructure.adapter.input.rest;

import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.CreateUserDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserLoginDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit.AuthResponse;
import com.apis.fintrack.infrastructure.security.service.AuthService;
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
    public ResponseEntity<AuthResponse> register( @Valid @RequestBody CreateUserDTO user){
    return ResponseEntity.ok(service.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid UserLoginDTO user){
        return ResponseEntity.ok(service.authenticate(user));
    }
}


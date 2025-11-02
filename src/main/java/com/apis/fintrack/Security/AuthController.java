//package com.apis.fintrack.Security;
//
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/apis/auth")
//
//public class AuthController {
//    @Autowired
//    private AuthService service;
//
//    @PostMapping("/register")
//    public ResponseEntity<UserCreateDTO> register(@RequestBody @Valid UserCreateDTO user){
//        return ResponseEntity.ok(service.register(user));
//
//    }
//
//}

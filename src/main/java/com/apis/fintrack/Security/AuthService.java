//package com.apis.apisjwtswagger.Security.Service;
//
//
//import com.apis.apisjwtswagger.Controller.UserMapper;
//import com.apis.apisjwtswagger.DTO.Users.UserCreateDTO;
//import com.apis.apisjwtswagger.DTO.Users.UserLoginDTO;
//import com.apis.apisjwtswagger.Exceptions.RolesNotFoundException;
//import com.apis.apisjwtswagger.Exceptions.UserNotFoundException;
//import com.apis.apisjwtswagger.Repository.RoleRepository;
//import com.apis.apisjwtswagger.Repository.UsersRepository;
//import com.apis.apisjwtswagger.Security.AuthenticationDTO.AuthResponse;
//import com.apis.apisjwtswagger.Security.JwtService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final UsersRepository repository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
//
//    @Autowired
//    UserMapper userMapper;
//    @Autowired
//    RoleRepository roleRepository;
//
//    /**
//     * REGISTRO - Crea usuario y devuelve JWT
//     */
//
//    public AuthResponse register(UserCreateDTO request) {
//
//        System.out.println(request.getUser_role());
//        // 1. Crear el usuario con password hasheada
//        var newUser = userMapper.toUsersEntity(request);
//        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
//        newUser.setRole(roleRepository.findByIdRole(
//                        request
//                                .getUser_role())
//                .orElseThrow(() -> new RolesNotFoundException("Role not found")));
//
//        // 2. Generar JWT
//        var jwtToken = jwtService.generateToken(newUser);
//
//        // 3. Guardar en BD
//        repository.save(newUser);
//
//
//        // 4. Devolver respuesta con el token
//        return AuthResponse.builder()
//                .token(jwtToken)
//                .build();
//    }
//
//    /**
//     * LOGIN - Valida credenciales y devuelve JWT
//     */
//
//    public AuthResponse authenticate(UserLoginDTO request) {
//        // 1. AUTENTICAR - Aquí Spring valida user/password
//        // Si falla, lanza una excepción automáticamente
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.email(),
//                        request.password()
//                )
//        );
//        // 2. Si llegamos aquí, las credenciales son correctas
//        // Buscar el usuario en BD
//        var user = repository.findByEmail(request.email())
//                .orElseThrow(()->
//                        new UserNotFoundException("User not found"));
//
//        // 3. Generar JWT
//        var jwtToken = jwtService.generateToken(user);
//
//        // 4. Devolver respuesta con el token
//        return AuthResponse.builder()
//                .token(jwtToken)
//                .build();
//    }
//}
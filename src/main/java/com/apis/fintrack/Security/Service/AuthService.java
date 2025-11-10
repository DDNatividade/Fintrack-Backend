package com.apis.fintrack.Security.Service;

import com.apis.fintrack.DAO.RoleRepository;
import com.apis.fintrack.DAO.UserRepository;
import com.apis.fintrack.DTO.UserEntity.Entry.CreateUserDTO;
import com.apis.fintrack.DTO.UserEntity.Entry.UserLoginDTO;
import com.apis.fintrack.Entity.RoleEnum;
import com.apis.fintrack.Exception.RolesNotFoundException;
import com.apis.fintrack.Exception.UserNotFoundException;
import com.apis.fintrack.Mapper.UserMapStruct;
import com.apis.fintrack.Security.JwtService;
import com.apis.fintrack.Security.SecurityDTO.AuthResponse;
import com.apis.fintrack.Service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
private final JwtService jwtService;
private final AuthenticationManager authenticationManager;

@Autowired
private UserMapStruct userMapper;

@Autowired
RoleRepository roleRepository;

@Autowired
UserServiceImpl userService;

    /* REGISTRO - Crea usuario y devuelve JWT
    */

    public AuthResponse register(CreateUserDTO request) {
          //1. Crear el usuario con password hasheada
          var newUser = userService.RegisterNewUser(request);
          newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
          newUser.setRole(roleRepository.findByRoleName(RoleEnum.User)
                          .orElseThrow(() -> new RolesNotFoundException("Role not found")));
          // 2. Generar JWT
          var jwtToken = jwtService.generateToken(newUser);
          // 3. Guardar en BD
        userRepository.save(newUser);

          // 4. Devolver respuesta con el token
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
    /**
    * LOGIN - Valida credenciales y devuelve JWT
    */
    public AuthResponse authenticate(UserLoginDTO request) {
        // 1. AUTENTICAR - Aquí Spring valida user/password Si falla,
        // lanza una excepción automáticamente
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                          request.getEmail(),
                          request.getPassword()
                )
        );
        // 2. Si llegamos aquí, las credenciales son correctas
        // Buscar el usuario en BD
        var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(()-> new UserNotFoundException("User not found"));
        // 3. Generar JWT
        var jwtToken = jwtService.generateToken(user);
        // 4. Devolver respuesta con el token
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
        }
    }
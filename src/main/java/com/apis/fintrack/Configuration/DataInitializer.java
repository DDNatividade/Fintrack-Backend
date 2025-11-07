package com.apis.fintrack.Configuration;

import com.apis.fintrack.DAO.PropertyRepository;
import com.apis.fintrack.DAO.RoleRepository;
import com.apis.fintrack.DAO.UserRepository;
import com.apis.fintrack.Entity.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    CommandLineRunner initData(
            RoleRepository roleRepository,
            PropertyRepository propertyRepository,
            UserRepository userRepository
    ) {
        return args -> {

            userRepository.deleteAll();
            propertyRepository.deleteAll();
            roleRepository.deleteAll();


            // === Crear propiedades ===
            PropertyEntity read = new PropertyEntity(0, PropertyEnum.Read, new HashSet<>());
            PropertyEntity write = new PropertyEntity(0, PropertyEnum.Write, new HashSet<>());
            PropertyEntity delete = new PropertyEntity(0, PropertyEnum.Delete, new HashSet<>());
            PropertyEntity update = new PropertyEntity(0, PropertyEnum.Update, new HashSet<>());
            PropertyEntity create = new PropertyEntity(0, PropertyEnum.Create, new HashSet<>());

            propertyRepository.saveAll(List.of(read, write, delete, update, create));

            // === Crear roles ===
            RoleEntity adminRole = new RoleEntity();
            adminRole.setRoleName(RoleEnum.Admin);

            RoleEntity userRole = new RoleEntity();
            userRole.setRoleName(RoleEnum.User);

            roleRepository.saveAll(List.of(adminRole, userRole));

            // === Asignar propiedades a roles ===
            adminRole.setPropertyWithRole(Set.of(read, write, delete, update, create));
            userRole.setPropertyWithRole(Set.of(read));

            // Actualizar la relación inversa
            read.setRolesWithProperty(Set.of(adminRole, userRole));
            write.setRolesWithProperty(Set.of(adminRole));
            delete.setRolesWithProperty(Set.of(adminRole));
            update.setRolesWithProperty(Set.of(adminRole));
            create.setRolesWithProperty(Set.of(adminRole));

            roleRepository.saveAll(List.of(adminRole, userRole));
            propertyRepository.saveAll(List.of(read, write, delete, update, create));

            // === Crear usuarios ===
            UserEntity admin = new UserEntity();
            admin.setName("Admin");
            admin.setSurname("System");
            admin.setEmail("admin@fintrack.com");
            admin.setPassword(passwordEncoder.encode("Admin1234"));
            admin.setBirthDate(LocalDate.of(1990, 1, 1));
            admin.setAvailableFunds(BigDecimal.valueOf(10000));
            admin.setRole(adminRole);

            UserEntity user = new UserEntity();
            user.setName("User");
            user.setSurname("Normal");
            user.setEmail("user@fintrack.com");
            user.setPassword(passwordEncoder.encode("User1234"));
            user.setBirthDate(LocalDate.of(1995, 5, 15));
            user.setAvailableFunds(BigDecimal.valueOf(2000));
            user.setRole(userRole);

            userRepository.saveAll(List.of(admin, user));

            System.out.println("FinTrack DataInitializer ejecutado correctamente.");
        };
    }
}

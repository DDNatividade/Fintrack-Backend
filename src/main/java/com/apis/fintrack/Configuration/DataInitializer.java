package com.apis.fintrack.Configuration;

import com.apis.fintrack.DAO.PropertyRepository;
import com.apis.fintrack.DAO.RoleRepository;
import com.apis.fintrack.DAO.UserRepository;
import com.apis.fintrack.Entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PropertyRepository propertyRepository;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            log.info("🔄 Starting FinTrack data initialization...");

            // === CREAR ROLES SI NO EXISTEN ===
            if (roleRepository.count() == 0) {
                RoleEntity adminRole = new RoleEntity();
                adminRole.setRoleName(RoleEnum.Admin);
                adminRole.setUsersWithRole(new ArrayList<>());
                adminRole.setPropertyWithRole(new HashSet<>());

                RoleEntity userRole = new RoleEntity();
                userRole.setRoleName(RoleEnum.User);
                userRole.setUsersWithRole(new ArrayList<>());
                userRole.setPropertyWithRole(new HashSet<>());

                roleRepository.saveAll(List.of(adminRole, userRole));
                log.info("Default roles created: ADMIN, USER");
            } else {
                log.info("Roles already exist, skipping creation.");
            }

            // Recuperar roles creados o existentes
            RoleEntity adminRole = roleRepository.findByRoleName(RoleEnum.Admin)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            RoleEntity userRole = roleRepository.findByRoleName(RoleEnum.User)
                    .orElseThrow(() -> new RuntimeException("User role not found"));

            // === CREAR PROPIEDADES SI NO EXISTEN ===
            if (propertyRepository.count() == 0) {
                PropertyEntity readProperty = new PropertyEntity();
                readProperty.setProperty(PropertyEnum.Read);
                readProperty.setRolesWithProperty(Set.of(adminRole, userRole));

                PropertyEntity writeProperty = new PropertyEntity();
                writeProperty.setProperty(PropertyEnum.Write);
                writeProperty.setRolesWithProperty(Set.of(adminRole));

                PropertyEntity updateProperty = new PropertyEntity();
                updateProperty.setProperty(PropertyEnum.Update);
                updateProperty.setRolesWithProperty(Set.of(adminRole));

                PropertyEntity createProperty = new PropertyEntity();
                createProperty.setProperty(PropertyEnum.Create);
                createProperty.setRolesWithProperty(Set.of(adminRole));

                PropertyEntity deleteProperty = new PropertyEntity();
                deleteProperty.setProperty(PropertyEnum.Delete);
                deleteProperty.setRolesWithProperty(Set.of(adminRole));

                propertyRepository.saveAll(List.of(
                        readProperty,
                        writeProperty,
                        updateProperty,
                        createProperty,
                        deleteProperty
                ));

                log.info("Default properties created and assigned to roles.");
            } else {
                log.info("Properties already exist, skipping creation.");
            }

            // === CREAR USUARIO ADMIN POR DEFECTO ===
            if (userRepository.findByEmail("admin@fintrack.com").isEmpty()) {
                UserEntity admin = new UserEntity();
                admin.setName("System");
                admin.setSurname("Administrator");
                admin.setEmail("admin@fintrack.com");
                admin.setPassword(passwordEncoder.encode("Admin1234"));
                admin.setBirthDate(LocalDate.of(1900, 1, 1));
                admin.setAvailableFunds(BigDecimal.ZERO);
                admin.setRole(adminRole);

                userRepository.save(admin);
                log.info("Default admin user created: admin@fintrack.com");
            } else {
                log.info("Admin user already exists, skipping creation.");
            }



            log.info("FinTrack data initialization complete.");
        };
    }
}

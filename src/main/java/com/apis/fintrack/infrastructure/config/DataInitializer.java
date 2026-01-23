package com.apis.fintrack.infrastructure.config;

import com.apis.fintrack.infrastructure.security.model.PropertyEnum;
import com.apis.fintrack.domain.user.model.RoleType;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.PropertyRepository;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.RoleRepository;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.UserRepository;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.PropertyJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.RoleJPAEntity;
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
            PropertyJPAEntity read = new PropertyJPAEntity(1L, PropertyEnum.READ, new HashSet<>());
            PropertyJPAEntity write = new PropertyJPAEntity(2L, PropertyEnum.WRITE, new HashSet<>());
            PropertyJPAEntity delete = new PropertyJPAEntity(3L, PropertyEnum.DELETE, new HashSet<>());
            PropertyJPAEntity update = new PropertyJPAEntity(4L, PropertyEnum.UPDATE, new HashSet<>());
            PropertyJPAEntity create = new PropertyJPAEntity(5L, PropertyEnum.CREATE, new HashSet<>());

            propertyRepository.saveAll(List.of(read, write, delete, update, create));

            // === Crear roles ===
            RoleJPAEntity adminRole = new RoleJPAEntity();
            adminRole.setRoleName(RoleType.ADMIN);

            RoleJPAEntity userRole = new RoleJPAEntity();
            userRole.setRoleName(RoleType.USER);

            roleRepository.saveAll(List.of(adminRole, userRole));

            // === Asignar propiedades a roles ===
            adminRole.setPropertyWithRole(Set.of(read, write, delete, update, create));
            userRole.setPropertyWithRole(Set.of(read));

            // Update the inverse relationship
            read.setRolesWithProperty(Set.of(adminRole, userRole));
            write.setRolesWithProperty(Set.of(adminRole));
            delete.setRolesWithProperty(Set.of(adminRole));
            update.setRolesWithProperty(Set.of(adminRole));
            create.setRolesWithProperty(Set.of(adminRole));

            roleRepository.saveAll(List.of(adminRole, userRole));
            propertyRepository.saveAll(List.of(read, write, delete, update, create));

            // === Crear usuarios ===
            UserJPAEntity admin = new UserJPAEntity();
            admin.setName("Admin");
            admin.setSurname("System");
            admin.setEmail("admin@fintrack.com");
            admin.setPassword(passwordEncoder.encode("Admin1234"));
            admin.setBirthDate(LocalDate.of(1990, 1, 1));
            admin.setAvailableFunds(BigDecimal.valueOf(10000));
            admin.setRole(adminRole);

            UserJPAEntity user = new UserJPAEntity();
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



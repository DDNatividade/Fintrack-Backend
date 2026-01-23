package com.apis.fintrack.infrastructure.adapter.output.persistence.repository;

import com.apis.fintrack.domain.user.model.RoleType;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository public interface UserRepository extends JpaRepository<UserJPAEntity,Long> {


    @Query(value = "Select u from UserJPAEntity u")
    Page<UserJPAEntity> showAll(Pageable pageable);

    @Query(value = "Select u from UserJPAEntity u WHERE u.userId=?1")
    Optional<UserJPAEntity> searchById(Long id);

    @Query(value = "Select u from UserJPAEntity u WHERE u.name=?1 and u.surname=?2")
    Optional<UserJPAEntity> findByUsernameAndSurname(String username, String surname);

    @Query(value = "Select u from UserJPAEntity u WHERE u.birthDate BETWEEN :startDate  and :endDate")
    Page<UserJPAEntity> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(value = "Select u from UserJPAEntity u WHERE u.role.roleName=?1")
    Page<UserJPAEntity> findByRole(RoleType roleName, Pageable pageable);

    @Query(value = "Select u from UserJPAEntity u WHERE u.email=?1")
    Optional<UserJPAEntity> findByEmail(String email);

}

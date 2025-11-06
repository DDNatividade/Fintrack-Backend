package com.apis.fintrack.DAO;

import com.apis.fintrack.Entity.RoleEnum;
import com.apis.fintrack.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    @Query(value = "Select u from UserEntity u")
    Optional<Page<UserEntity>> showAll(Pageable pageable);

    @Query(value = "Select u from UserEntity u WHERE u.userId=?1")
    Optional<UserEntity> searchById(Long id);

    @Query(value = "Select u from UserEntity u WHERE u.name=?1 and u.surname=?2")
    Optional<UserEntity> findByUsernameAndSurname(String username, String surname);

    @Query(value = "Select u from UserEntity u WHERE u.birthDate BETWEEN :startDate  and :endDate")
    Optional<Page<UserEntity>> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(value = "Select u from UserEntity u WHERE u.role.roleName=?1")
    Optional<Page<UserEntity>> findByRole(RoleEnum roleName, Pageable pageable);

    @Query(value = "Select u from UserEntity u WHERE u.email=?1")
    Optional<UserEntity> findByEmail(String email);




}

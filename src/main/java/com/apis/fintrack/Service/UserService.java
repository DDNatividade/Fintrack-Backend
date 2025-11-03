package com.apis.fintrack.Service;


import com.apis.fintrack.Entity.RoleEnum;
import com.apis.fintrack.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface UserService {
    UserEntity findByUserId(Long userId);
    UserEntity findByNameAndSurname(String name, String username);
    UserEntity findByDate(LocalDate startDate, LocalDate endDate);
    Page<UserEntity> findAllByRole(RoleEnum role, Pageable pageable);
    Page<UserEntity> showAll(Pageable pageable);
    UserEntity findByEmail(String email);
    void deleteById(Long id);
    void save(UserEntity user);
    void calculateBalance(UserEntity user);




}

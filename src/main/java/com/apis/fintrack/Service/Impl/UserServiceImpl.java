package com.apis.fintrack.Service.Impl;

import com.apis.fintrack.DAO.RoleRepository;
import com.apis.fintrack.DAO.UserRepository;
import com.apis.fintrack.DTO.UserEntity.Entry.CreateUserDTO;
import com.apis.fintrack.Entity.RoleEnum;
import com.apis.fintrack.Entity.TransactionEntity;
import com.apis.fintrack.Entity.UserEntity;
import com.apis.fintrack.Exception.RolesNotFoundException;
import com.apis.fintrack.Exception.UserNotFoundException;
import com.apis.fintrack.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl  implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserEntity findByUserId(Long userId) {
        return userRepository.searchById(userId)
                .orElseThrow(() -> new UserNotFoundException("There is no user with id: " + userId));
    }

    @Override
    public UserEntity findByNameAndSurname(String name, String surname) {
        return userRepository.findByUsernameAndSurname(name, surname)
                .orElseThrow(() -> new UserNotFoundException("There is no user  named: " + name+ " " + surname));
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email: " + email));
    }

    @Override
    public Page<UserEntity> findByDate(LocalDate startDate, LocalDate endDate,Pageable pageable) {
        return userRepository.findByDateBetween(startDate, endDate, pageable)
                .orElseThrow(() -> new UserNotFoundException
                        ("There is no user with birth date between: " +
                                "" + startDate + " and " + endDate));
    }

    @Override
    public Page<UserEntity> findAllByRole(RoleEnum role, Pageable pageable) {
        return userRepository.findByRole(role, pageable)
                .orElseThrow(() -> new UserNotFoundException("There is no user with role: " + role));
    }

    @Override
    public Page<UserEntity> showAll(Pageable pageable) {
        return userRepository.showAll(pageable)
                .orElseThrow(() -> new UserNotFoundException("There are no users yet"));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void save(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public void calculateBalance(UserEntity user) {
        List<TransactionEntity> transactions = user.getTransactions();
        BigDecimal salary=BigDecimal.ZERO;
        for (TransactionEntity transaction : transactions) {
            salary=salary.add(transaction.getAmount());
        }

        if(salary.longValue()<=0) {
            salary=BigDecimal.ZERO;
        }

       salary.setScale(2, BigDecimal.ROUND_HALF_UP);
       user.setAvailableFunds(salary);


    }

    public UserEntity RegisterNewUser(CreateUserDTO userDTO){
        UserEntity user = new UserEntity();
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(roleRepository.findByRoleName(userDTO.getRole())
                .orElseThrow(() -> new RolesNotFoundException("There are no roles named "+userDTO.getRole())));
        return user;
    }
}

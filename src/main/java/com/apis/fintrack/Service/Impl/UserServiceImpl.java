package com.apis.fintrack.Service.Impl;

import com.apis.fintrack.DAO.UserRepository;
import com.apis.fintrack.Entity.RoleEnum;
import com.apis.fintrack.Entity.TransactionEntity;
import com.apis.fintrack.Entity.UserEntity;
import com.apis.fintrack.Exception.UserNotFoundException;
import com.apis.fintrack.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public UserEntity findByDate(LocalDate startDate, LocalDate endDate) {
        return userRepository.findByDateBetween(startDate, endDate)
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
}

package com.apis.fintrack.Service.Impl;

import com.apis.fintrack.DAO.TransactionRepository;
import com.apis.fintrack.Entity.CategoryEnum;
import com.apis.fintrack.Entity.TransactionEntity;
import com.apis.fintrack.Exception.TransactionNotFoundException;
import com.apis.fintrack.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void addTransaction(TransactionEntity transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public TransactionEntity findTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(()->
                new TransactionNotFoundException("There are no transactions with the id: " + id));
    }

    @Override
    public Page<TransactionEntity> findAll(Pageable pageable) {
        return transactionRepository.shoeAllTransactions(pageable)
                .orElseThrow(() -> new TransactionNotFoundException("There are no transactions yet"));
    }

    @Override
    public Page<TransactionEntity> findAllByCategory(CategoryEnum category, Pageable pageable) {
        return transactionRepository.findAllByCategory(category, pageable)
                .orElseThrow(() -> new TransactionNotFoundException("There are no transactions with this category"));
    }

    @Override
    public Page<TransactionEntity> findAllBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return transactionRepository.findTransactionEntitiesByTransaction_dateBetween(startDate, endDate, pageable)
                .orElseThrow(() -> new TransactionNotFoundException("There are no transactions between "+ startDate+" and "+endDate));
    }

    @Override
    public Page<TransactionEntity> findAllBetweenAmounts(BigDecimal firstAmount, BigDecimal secondAmount, Pageable pageable) {
        return transactionRepository.findTransactionEntitiesByAmountBetween(firstAmount,secondAmount,pageable)
                .orElseThrow(() -> new TransactionNotFoundException("Not Transactions found"));
    }

    @Override
    public Page<TransactionEntity> findAllOrderByAmount(Pageable pageable) {
        return transactionRepository.findAllOrderByAmount(pageable)
                .orElseThrow(()-> new TransactionNotFoundException("Not Transactions found"));
    }

    @Override
    public Page<TransactionEntity> findAllOrderByTransactionDate(Pageable pageable) {
        return transactionRepository.findAllOrderByTransactionDate(pageable)
                .orElseThrow(()-> new TransactionNotFoundException("Not Transactions found"));
    }

    @Override
    public Page<TransactionEntity> findAllIfIncome(Boolean isIncome, Pageable pageable) {
        return transactionRepository.findAllIncome(isIncome, pageable)
                .orElseThrow(() -> new TransactionNotFoundException("Not Transactions found"));
    }

    @Override
    public void analizeTransaction(TransactionEntity transaction) {
        if(!transaction.isIncome()) transaction.setAmount(transaction.getAmount().negate());
    }
}

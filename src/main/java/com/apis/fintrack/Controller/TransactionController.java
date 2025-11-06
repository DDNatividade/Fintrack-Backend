package com.apis.fintrack.Controller;

import com.apis.fintrack.DTO.PagedResponse;
import com.apis.fintrack.DTO.TransactionEntity.Entry.CreateTransactionDTO;
import com.apis.fintrack.DTO.TransactionEntity.Entry.Patch.ChangeTransactionAmountDTO;
import com.apis.fintrack.DTO.TransactionEntity.Entry.Patch.ChangeTransactionCategoryDTO;
import com.apis.fintrack.DTO.TransactionEntity.Entry.Patch.ChangeTransactionDateDTO;
import com.apis.fintrack.DTO.TransactionEntity.Entry.Patch.ChangeTransactionTypeDTO;
import com.apis.fintrack.DTO.TransactionEntity.Exit.ShowTransactionDTO;
import com.apis.fintrack.Entity.CategoryEnum;
import com.apis.fintrack.Entity.TransactionEntity;
import com.apis.fintrack.Mapper.TransactionMapStruct;
import com.apis.fintrack.Service.TransactionService;
import com.apis.fintrack.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/apis/transactions")
public class TransactionController {

    @Autowired
    private  TransactionService transactionService;

    @Autowired
    private  UserService userService;

    @Autowired
    TransactionMapStruct mapStruct;

    @GetMapping
    public ResponseEntity<PagedResponse<ShowTransactionDTO>> showAllTransactions(Pageable pageable) {
        Page<TransactionEntity> transactionEntities = transactionService.findAll(pageable);
        Page<ShowTransactionDTO> responseTransaction = transactionEntities.map(mapStruct::toShowTransactionDTO);

        PagedResponse<ShowTransactionDTO> showTransactionDTO = new PagedResponse<>(
                responseTransaction.getContent(),
                responseTransaction.getTotalPages(),
                responseTransaction.getTotalElements(),
                responseTransaction.getSize(),
                responseTransaction.isLast()
        );
        return ResponseEntity.status(HttpStatus.OK).body(showTransactionDTO);
    }

    @PostMapping
    public ResponseEntity<ShowTransactionDTO> createTransaction(@Valid @RequestBody CreateTransactionDTO dto) {
        TransactionEntity transactionEntity = mapStruct.toTransactionEntity(dto);
        transactionService.addTransaction(transactionEntity);

        ShowTransactionDTO showTransactionDTO = mapStruct.toShowTransactionDTO(transactionEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(showTransactionDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowTransactionDTO> showTransactionById(@PathVariable @Valid Long id) {
        TransactionEntity transactionEntity = transactionService.findTransactionById(id);
        return ResponseEntity.ok(mapStruct.toShowTransactionDTO(transactionEntity));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<PagedResponse<ShowTransactionDTO>> showTransactionByCategory(
            @PathVariable @Valid CategoryEnum category, Pageable pageable) {

        Page<TransactionEntity> transactionEntities = transactionService.findAllByCategory(category, pageable);
        Page<ShowTransactionDTO> responseTransaction = transactionEntities.map(mapStruct::toShowTransactionDTO);

        PagedResponse<ShowTransactionDTO> showTransactionDTO = new PagedResponse<>(
                responseTransaction.getContent(),
                responseTransaction.getTotalPages(),
                responseTransaction.getTotalElements(),
                responseTransaction.getSize(),
                responseTransaction.isLast()
        );

        return ResponseEntity.ok(showTransactionDTO);
    }

    @GetMapping("/dates")
    public ResponseEntity<PagedResponse<ShowTransactionDTO>> showAllBetweenDates(
            @RequestParam @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            Pageable pageable) {

        Page<TransactionEntity> entities = transactionService.findAllBetween(start, end, pageable);
        Page<ShowTransactionDTO> responseTransaction = entities.map(mapStruct::toShowTransactionDTO);

        PagedResponse<ShowTransactionDTO> showTransactionDTO = new PagedResponse<>(
                responseTransaction.getContent(),
                responseTransaction.getTotalPages(),
                responseTransaction.getTotalElements(),
                responseTransaction.getSize(),
                responseTransaction.isLast()
        );

        return ResponseEntity.ok(showTransactionDTO);
    }


    @GetMapping("/income")
    public ResponseEntity<PagedResponse<ShowTransactionDTO>> showIfIncome(
            @RequestParam Boolean isIncome, Pageable pageable) {

        Page<TransactionEntity> transactions = transactionService.findAllIfIncome(isIncome, pageable);
        Page<ShowTransactionDTO> showTransactionDTOS = transactions.map(mapStruct::toShowTransactionDTO);

        PagedResponse<ShowTransactionDTO> response = new PagedResponse<>(
                showTransactionDTOS.getContent(),
                showTransactionDTOS.getTotalPages(),
                showTransactionDTOS.getTotalElements(),
                showTransactionDTOS.getSize(),
                showTransactionDTOS.isLast()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * ✅ Actualizar completamente una transacción
     * PUT /apis/transactions/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShowTransactionDTO> editTransaction(
            @PathVariable Long id,
            @Valid @RequestBody CreateTransactionDTO transactionDTO) {

        TransactionEntity transactionEntity = transactionService.findTransactionById(id);
        transactionEntity.setCategory(transactionDTO.getCategory());
        transactionEntity.setDescription(transactionDTO.getDescription());
        transactionEntity.setAmount(transactionDTO.getAmount());
        transactionEntity.setIncome(transactionDTO.isIncome());

        transactionService.addTransaction(transactionEntity);

        ShowTransactionDTO showTransactionDTO = mapStruct.toShowTransactionDTO(transactionEntity);
        return ResponseEntity.ok(showTransactionDTO);
    }


    @PatchMapping("/{id}/type")
    public ResponseEntity<ShowTransactionDTO> changeTransactionType(
            @PathVariable Long id,
            @Valid @RequestBody ChangeTransactionTypeDTO dto) {

        TransactionEntity transaction = transactionService.findTransactionById(id);
        transaction.setIncome(dto.isIncome());
        transactionService.addTransaction(transaction);

        return ResponseEntity.ok(mapStruct.toShowTransactionDTO(transaction));
    }


    @PatchMapping("/{id}/date")
    public ResponseEntity<ShowTransactionDTO> changeTransactionDate(
            @PathVariable Long id,
            @Valid @RequestBody ChangeTransactionDateDTO dto) {

        TransactionEntity transaction = transactionService.findTransactionById(id);
        transaction.setTransaction_date(dto.getTransaction_date());
        transactionService.addTransaction(transaction);

        return ResponseEntity.ok(mapStruct.toShowTransactionDTO(transaction));
    }


    @PatchMapping("/{id}/category")
    public ResponseEntity<ShowTransactionDTO> changeTransactionCategory(
            @PathVariable Long id,
            @Valid @RequestBody ChangeTransactionCategoryDTO dto) {

        TransactionEntity transaction = transactionService.findTransactionById(id);
        transaction.setCategory(dto.getCategory());
        transactionService.addTransaction(transaction);

        return ResponseEntity.ok(mapStruct.toShowTransactionDTO(transaction));
    }

    @PatchMapping("/{id}/amount")
    public ResponseEntity<ShowTransactionDTO> changeTransactionAmount(
            @PathVariable Long id,
            @Valid @RequestBody ChangeTransactionAmountDTO dto) {

        TransactionEntity transaction = transactionService.findTransactionById(id);
        transaction.setAmount(dto.getAmount());
        transactionService.addTransaction(transaction);

        return ResponseEntity.ok(mapStruct.toShowTransactionDTO(transaction));
    }
}

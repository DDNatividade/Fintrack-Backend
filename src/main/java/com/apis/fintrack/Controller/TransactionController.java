package com.apis.fintrack.Controller;

import com.apis.fintrack.DTO.PagedResponse;
import com.apis.fintrack.DTO.TransactionEntity.Entry.CreateTransactionDTO;
import com.apis.fintrack.DTO.TransactionEntity.Exit.ShowTransactionDTO;
import com.apis.fintrack.Entity.CategoryEnum;
import com.apis.fintrack.Entity.RoleEntity;
import com.apis.fintrack.Entity.TransactionEntity;
import com.apis.fintrack.Mapper.TransactionMapStruct;
import com.apis.fintrack.Service.RoleService;
import com.apis.fintrack.Service.TransactionService;
import com.apis.fintrack.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/apis/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    TransactionMapStruct mapStruct;


    @GetMapping
    public ResponseEntity<PagedResponse<ShowTransactionDTO>>
    showAllTransactions(Pageable pageable) {
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
    public ResponseEntity<ShowTransactionDTO>
    createTransaction(@RequestBody CreateTransactionDTO dto) {
        TransactionEntity transactionEntity = mapStruct.toTransactionEntity(dto);
        transactionService.addTransaction(transactionEntity);

        ShowTransactionDTO showTransactionDTO = mapStruct.toShowTransactionDTO(transactionEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(showTransactionDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showTransactionById(@PathVariable @Valid Long id) {
        TransactionEntity transactionEntity = transactionService.findTransactionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(mapStruct.toShowTransactionDTO(transactionEntity));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<PagedResponse<ShowTransactionDTO>>
    showTransactionByCategory(@PathVariable @Valid CategoryEnum category, Pageable pageable) {
        Page<TransactionEntity> TransactionEntities=
                transactionService.findAllByCategory(category, pageable);
        Page<ShowTransactionDTO> responseTransaction = TransactionEntities.map(mapStruct::toShowTransactionDTO);
        PagedResponse<ShowTransactionDTO> showTransactionDTO = new PagedResponse<>(
                responseTransaction.getContent(),
                responseTransaction.getTotalPages(),
                responseTransaction.getTotalElements(),
                responseTransaction.getSize(),
                responseTransaction.isLast()
        );
        return ResponseEntity.status(HttpStatus.OK).body(showTransactionDTO);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<ShowTransactionDTO>>
    showAllBetweenDates(@RequestParam @Valid LocalDate start,
                        @RequestParam @Valid LocalDate end, Pageable pageable)
    {
        Page<TransactionEntity> entities = transactionService.findAllBetween(start, end, pageable);
        Page<ShowTransactionDTO> responseTransaction = entities.map(mapStruct::toShowTransactionDTO);

        PagedResponse<ShowTransactionDTO> showTransactionDTO = new PagedResponse<>(
                responseTransaction.getContent(),
                responseTransaction.getTotalPages(),
                responseTransaction.getTotalElements(),
                responseTransaction.getSize(),
                responseTransaction.isLast()
        );

        return ResponseEntity.status(HttpStatus.OK).body(showTransactionDTO);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<ShowTransactionDTO>>
    showIfIncome(@RequestParam Boolean isIncome, Pageable pageable) {
        if (isIncome) {
           Page<TransactionEntity> incomes = transactionService.findAllIfIncome(isIncome, pageable);
           Page<ShowTransactionDTO> showTransactionDTOS = incomes.map(mapStruct::toShowTransactionDTO);
            PagedResponse<ShowTransactionDTO> PagedResponseShowTransactionsDTO = new PagedResponse<ShowTransactionDTO>(
                    showTransactionDTOS.getContent(),
                    showTransactionDTOS.getTotalPages(),
                    showTransactionDTOS.getTotalElements(),
                    showTransactionDTOS.getSize(),
                    showTransactionDTOS.isLast()
            );

            return ResponseEntity.status(HttpStatus.OK).body(PagedResponseShowTransactionsDTO);

        }
        else {
            Page<TransactionEntity> incomes = transactionService.findAllIfIncome(!isIncome, pageable);
            Page<ShowTransactionDTO> showTransactionDTOS = incomes.map(mapStruct::toShowTransactionDTO);
            PagedResponse<ShowTransactionDTO> PagedResponseShowTransactionsDTO = new PagedResponse<ShowTransactionDTO>(
                    showTransactionDTOS.getContent(),
                    showTransactionDTOS.getTotalPages(),
                    showTransactionDTOS.getTotalElements(),
                    showTransactionDTOS.getSize(),
                    showTransactionDTOS.isLast()
            );

            return ResponseEntity.status(HttpStatus.OK).body(PagedResponseShowTransactionsDTO);

        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?>
    editTransaction(@PathVariable Long id,@Valid @RequestBody CreateTransactionDTO transactionDTO
    ) {
        TransactionEntity transactionEntity = transactionService.findTransactionById(id);
        transactionEntity.setCategory(transactionDTO.getCategory());
        transactionEntity.setDescription(transactionDTO.getDescription());
        transactionEntity.setAmount(transactionDTO.getAmount());
        transactionEntity.setIncome(transactionDTO.isIncome());

        transactionService.addTransaction(transactionEntity);

        ShowTransactionDTO showTransactionDTO = mapStruct.toShowTransactionDTO(transactionEntity);

        return ResponseEntity.status(HttpStatus.OK).body(showTransactionDTO);

    }



}

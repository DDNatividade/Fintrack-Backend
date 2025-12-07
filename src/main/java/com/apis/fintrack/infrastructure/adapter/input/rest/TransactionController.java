package com.apis.fintrack.infrastructure.adapter.input.rest;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;
import com.apis.fintrack.domain.transaction.port.input.CreateTransactionUseCase;
import com.apis.fintrack.domain.transaction.port.input.DeleteTransactionUseCase;
import com.apis.fintrack.domain.transaction.port.input.FindTransactionUseCase;
import com.apis.fintrack.domain.transaction.port.input.UpdateTransactionUseCase;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.CreateTransactionDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.TransactionPatch.ChangeTransactionAmountDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.TransactionPatch.ChangeTransactionCategoryDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.TransactionPatch.ChangeTransactionDateDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.TransactionPatch.ChangeTransactionTypeDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit.PagedResponse;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit.ShowTransactionDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.mapper.TransactionRestMapper;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para operaciones de transacciones.
 *
 * Usa arquitectura hexagonal con puertos de entrada (Use Cases).
 */
@RestController
@RequestMapping("/apis/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final FindTransactionUseCase findTransactionUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;
    private final TransactionRestMapper mapper;

    public TransactionController(
            CreateTransactionUseCase createTransactionUseCase,
            FindTransactionUseCase findTransactionUseCase,
            UpdateTransactionUseCase updateTransactionUseCase,
            DeleteTransactionUseCase deleteTransactionUseCase,
            TransactionRestMapper mapper) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.findTransactionUseCase = findTransactionUseCase;
        this.updateTransactionUseCase = updateTransactionUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;
        this.mapper = mapper;
    }


    // ==================== GET ENDPOINTS ====================

    @GetMapping
    public ResponseEntity<PagedResponse<ShowTransactionDTO>> showAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Transaction> transactions = findTransactionUseCase.findAll(page, size);
        List<ShowTransactionDTO> dtos = mapper.toShowTransactionDTOList(transactions);

        PagedResponse<ShowTransactionDTO> response = new PagedResponse<>(
                dtos, 0, dtos.size(), size, true
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowTransactionDTO> showTransactionById(@PathVariable Long id) {
        Transaction transaction = findTransactionUseCase.findById(id);
        return ResponseEntity.ok(mapper.toShowTransactionDTO(transaction));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<PagedResponse<ShowTransactionDTO>> showTransactionByCategory(
            @PathVariable TransactionCategoryEnum category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Transaction> transactions = findTransactionUseCase.findByCategory(category, page, size);
        List<ShowTransactionDTO> dtos = mapper.toShowTransactionDTOList(transactions);

        PagedResponse<ShowTransactionDTO> response = new PagedResponse<>(
                dtos, 0, dtos.size(), size, true
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dates")
    public ResponseEntity<PagedResponse<ShowTransactionDTO>> showAllBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Transaction> transactions = findTransactionUseCase.findByDateBetween(start, end, page, size);
        List<ShowTransactionDTO> dtos = mapper.toShowTransactionDTOList(transactions);

        PagedResponse<ShowTransactionDTO> response = new PagedResponse<>(
                dtos, 0, dtos.size(), size, true
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/income")
    public ResponseEntity<PagedResponse<ShowTransactionDTO>> showIfIncome(
            @RequestParam Boolean isIncome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Transaction> transactions = findTransactionUseCase.findByType(isIncome, page, size);
        List<ShowTransactionDTO> dtos = mapper.toShowTransactionDTOList(transactions);

        PagedResponse<ShowTransactionDTO> response = new PagedResponse<>(
                dtos, 0, dtos.size(), size, true
        );
        return ResponseEntity.ok(response);
    }

    // ==================== POST ENDPOINTS ====================

    @PostMapping
    public ResponseEntity<ShowTransactionDTO> createTransaction(
            @Valid @RequestBody CreateTransactionDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        // TODO: Obtener userId del usuario autenticado
        Long userId = 1L; // Placeholder - implementar obtención real del userId

        var command = mapper.toCommand(dto, userId);
        Transaction transaction = createTransactionUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toShowTransactionDTO(transaction));
    }

    // ==================== PUT ENDPOINTS ====================

    @PutMapping("/{id}")
    public ResponseEntity<ShowTransactionDTO> editTransaction(
            @PathVariable Long id,
            @Valid @RequestBody CreateTransactionDTO dto) {

        var command = mapper.toUpdateCommand(id, dto);
        Transaction transaction = updateTransactionUseCase.update(command);

        return ResponseEntity.ok(mapper.toShowTransactionDTO(transaction));
    }

    // ==================== PATCH ENDPOINTS ====================

    @PatchMapping("/{id}/type")
    public ResponseEntity<ShowTransactionDTO> changeTransactionType(
            @PathVariable Long id,
            @Valid @RequestBody ChangeTransactionTypeDTO dto) {

        Transaction transaction = updateTransactionUseCase.updateType(id, dto.isIncome());
        return ResponseEntity.ok(mapper.toShowTransactionDTO(transaction));
    }

    @PatchMapping("/{id}/date")
    public ResponseEntity<ShowTransactionDTO> changeTransactionDate(
            @PathVariable Long id,
            @Valid @RequestBody ChangeTransactionDateDTO dto) {

        Transaction transaction = updateTransactionUseCase.updateDate(id, dto.getTransaction_date());
        return ResponseEntity.ok(mapper.toShowTransactionDTO(transaction));
    }

    @PatchMapping("/{id}/category")
    public ResponseEntity<ShowTransactionDTO> changeTransactionCategory(
            @PathVariable Long id,
            @Valid @RequestBody ChangeTransactionCategoryDTO dto) {

        Transaction transaction = updateTransactionUseCase.updateCategory(id, dto.getCategory());
        return ResponseEntity.ok(mapper.toShowTransactionDTO(transaction));
    }

    @PatchMapping("/{id}/amount")
    public ResponseEntity<ShowTransactionDTO> changeTransactionAmount(
            @PathVariable Long id,
            @Valid @RequestBody ChangeTransactionAmountDTO dto) {

        Transaction transaction = updateTransactionUseCase.updateAmount(id, dto.getAmount());
        return ResponseEntity.ok(mapper.toShowTransactionDTO(transaction));
    }

    // ==================== DELETE ENDPOINTS ====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        deleteTransactionUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}



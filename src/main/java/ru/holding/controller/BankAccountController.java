package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.IncomeExpenseDynamicsResponse;
import ru.holding.dto.request.CreateBankAccountRequest;
import ru.holding.dto.request.UpdateBankAccountRequest;
import ru.holding.dto.response.BankAccountDTO;
import ru.holding.service.BankAccountService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bank-accounts")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Bank Accounts", description = "Bank account management API")
public class BankAccountController {

    private final BankAccountService accountService;

    @GetMapping("/income-expense-dynamics")
    @Operation(summary = "Get income and expense dynamics for chart")
    public ResponseEntity<IncomeExpenseDynamicsResponse> getIncomeExpenseDynamics() {
        IncomeExpenseDynamicsResponse response = IncomeExpenseDynamicsResponse.builder()
                .months(List.of("Фев", "Мар", "Апр", "Май", "Июн"))
                .incomes(List.of(80000, 75000, 82000, 78000, 85000))
                .expenses(List.of(45000, 50000, 48000, 52000, 47000))
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a new bank account")
    public ResponseEntity<BankAccountDTO> create(@Valid @RequestBody CreateBankAccountRequest request) {
        BankAccountDTO created = accountService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all bank accounts or filter by enterprise/active status")
    public ResponseEntity<List<BankAccountDTO>> getAll(
            @RequestParam(required = false) Long enterpriseId,
            @RequestParam(required = false) Boolean active) {
        if (enterpriseId != null) {
            return ResponseEntity.ok(accountService.getByEnterpriseId(enterpriseId));
        }
        if (active != null && active) {
            return ResponseEntity.ok(accountService.getActiveAccounts());
        }
        return ResponseEntity.ok(accountService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get bank account by ID")
    public ResponseEntity<BankAccountDTO> getById(@PathVariable Long id) {
        BankAccountDTO account = accountService.getById(id);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update bank account by ID")
    public ResponseEntity<BankAccountDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateBankAccountRequest request) {
        BankAccountDTO updated = accountService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate bank account by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

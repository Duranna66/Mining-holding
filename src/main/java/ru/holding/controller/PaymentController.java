package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.request.CreatePaymentRequest;
import ru.holding.dto.request.UpdatePaymentRequest;
import ru.holding.dto.response.PaymentDTO;
import ru.holding.enums.PaymentStatus;
import ru.holding.enums.PaymentType;
import ru.holding.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Payments", description = "Payment management API")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Create a new payment")
    public ResponseEntity<PaymentDTO> create(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentDTO created = paymentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all payments or filter by bank account/status/type")
    public ResponseEntity<List<PaymentDTO>> getAll(
            @RequestParam(required = false) Long bankAccountId,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) PaymentType type) {
        if (bankAccountId != null) {
            return ResponseEntity.ok(paymentService.getByBankAccountId(bankAccountId));
        }
        if (status != null) {
            return ResponseEntity.ok(paymentService.getByStatus(status));
        }
        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentDTO> getById(@PathVariable Long id) {
        PaymentDTO payment = paymentService.getById(id);
        return ResponseEntity.ok(payment);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update payment by ID")
    public ResponseEntity<PaymentDTO> update(@PathVariable Long id,
                                              @Valid @RequestBody UpdatePaymentRequest request) {
        PaymentDTO updated = paymentService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update payment status")
    public ResponseEntity<PaymentDTO> updateStatus(@PathVariable Long id,
                                                    @RequestParam PaymentStatus status) {
        PaymentDTO updated = paymentService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.request.CreateCustomerRequest;
import ru.holding.dto.request.UpdateCustomerRequest;
import ru.holding.dto.response.CustomerDTO;
import ru.holding.enums.CustomerType;
import ru.holding.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Customers", description = "Customer management API")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<CustomerDTO> create(@Valid @RequestBody CreateCustomerRequest request) {
        CustomerDTO created = customerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all customers or filter by active status/type")
    public ResponseEntity<List<CustomerDTO>> getAll(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) CustomerType type) {
        if (active != null && active) {
            return ResponseEntity.ok(customerService.getActiveCustomers());
        }
        if (type != null) {
            return ResponseEntity.ok(customerService.getByType(type));
        }
        return ResponseEntity.ok(customerService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long id) {
        CustomerDTO customer = customerService.getById(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer by ID")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id,
                                               @Valid @RequestBody UpdateCustomerRequest request) {
        CustomerDTO updated = customerService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate customer by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

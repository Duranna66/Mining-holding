package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.request.CreateSupplierRequest;
import ru.holding.dto.request.UpdateSupplierRequest;
import ru.holding.dto.response.SupplierDTO;
import ru.holding.enums.SupplierType;
import ru.holding.service.SupplierService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Suppliers", description = "Supplier management API")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    @Operation(summary = "Create a new supplier")
    public ResponseEntity<SupplierDTO> create(@Valid @RequestBody CreateSupplierRequest request) {
        SupplierDTO created = supplierService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all suppliers or filter by active status/type")
    public ResponseEntity<List<SupplierDTO>> getAll(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) SupplierType type) {
        if (active != null && active) {
            return ResponseEntity.ok(supplierService.getActiveSuppliers());
        }
        if (type != null) {
            return ResponseEntity.ok(supplierService.getByType(type));
        }
        return ResponseEntity.ok(supplierService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by ID")
    public ResponseEntity<SupplierDTO> getById(@PathVariable Long id) {
        SupplierDTO supplier = supplierService.getById(id);
        return ResponseEntity.ok(supplier);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update supplier by ID")
    public ResponseEntity<SupplierDTO> update(@PathVariable Long id,
                                               @Valid @RequestBody UpdateSupplierRequest request) {
        SupplierDTO updated = supplierService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate supplier by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.request.CreateWarehouseRequest;
import ru.holding.dto.request.UpdateWarehouseRequest;
import ru.holding.dto.response.WarehouseDTO;
import ru.holding.service.WarehouseService;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Warehouses", description = "Warehouse management API")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    @Operation(summary = "Create a new warehouse")
    public ResponseEntity<WarehouseDTO> create(@Valid @RequestBody CreateWarehouseRequest request) {
        WarehouseDTO created = warehouseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all warehouses or filter by enterprise")
    public ResponseEntity<List<WarehouseDTO>> getAll(
            @RequestParam(required = false) Long enterpriseId) {
        if (enterpriseId != null) {
            return ResponseEntity.ok(warehouseService.getByEnterpriseId(enterpriseId));
        }
        return ResponseEntity.ok(warehouseService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get warehouse by ID")
    public ResponseEntity<WarehouseDTO> getById(@PathVariable Long id) {
        WarehouseDTO warehouse = warehouseService.getById(id);
        return ResponseEntity.ok(warehouse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update warehouse by ID")
    public ResponseEntity<WarehouseDTO> update(@PathVariable Long id,
                                                @Valid @RequestBody UpdateWarehouseRequest request) {
        WarehouseDTO updated = warehouseService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete warehouse by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        warehouseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

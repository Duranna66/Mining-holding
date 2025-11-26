package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.request.CreateMaterialInventoryRequest;
import ru.holding.dto.request.UpdateMaterialInventoryRequest;
import ru.holding.dto.response.MaterialInventoryDTO;
import ru.holding.service.MaterialInventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Material Inventory", description = "Material inventory management API")
public class MaterialInventoryController {

    private final MaterialInventoryService inventoryService;

    @PostMapping
    @Operation(summary = "Create a new material inventory record")
    public ResponseEntity<MaterialInventoryDTO> create(@Valid @RequestBody CreateMaterialInventoryRequest request) {
        MaterialInventoryDTO created = inventoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all inventory records or filter by warehouse/material")
    public ResponseEntity<List<MaterialInventoryDTO>> getAll(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long materialId) {
        if (warehouseId != null) {
            return ResponseEntity.ok(inventoryService.getByWarehouseId(warehouseId));
        }
        if (materialId != null) {
            return ResponseEntity.ok(inventoryService.getByMaterialId(materialId));
        }
        return ResponseEntity.ok(inventoryService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inventory record by ID")
    public ResponseEntity<MaterialInventoryDTO> getById(@PathVariable Long id) {
        MaterialInventoryDTO inventory = inventoryService.getById(id);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update inventory quantity by ID")
    public ResponseEntity<MaterialInventoryDTO> update(@PathVariable Long id,
                                                        @Valid @RequestBody UpdateMaterialInventoryRequest request) {
        MaterialInventoryDTO updated = inventoryService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete inventory record by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inventoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

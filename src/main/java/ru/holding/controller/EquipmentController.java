package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.request.CreateEquipmentRequest;
import ru.holding.dto.request.UpdateEquipmentRequest;
import ru.holding.dto.response.EquipmentDTO;
import ru.holding.enums.EquipmentStatus;
import ru.holding.service.EquipmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipment")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Equipment", description = "Equipment management API")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @PostMapping
    @Operation(summary = "Create a new equipment")
    public ResponseEntity<EquipmentDTO> create(@Valid @RequestBody CreateEquipmentRequest request) {
        EquipmentDTO created = equipmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all equipment or filter by department ID or status")
    public ResponseEntity<List<EquipmentDTO>> getAll(@RequestParam(required = false) Long departmentId,
                                                     @RequestParam(required = false) EquipmentStatus status) {
        List<EquipmentDTO> equipment;
        if (departmentId != null) {
            equipment = equipmentService.getByDepartmentId(departmentId);
        } else if (status != null) {
            equipment = equipmentService.getByStatus(status);
        } else {
            equipment = equipmentService.getAll();
        }
        return ResponseEntity.ok(equipment);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get equipment by ID")
    public ResponseEntity<EquipmentDTO> getById(@PathVariable Long id) {
        EquipmentDTO equipment = equipmentService.getById(id);
        return ResponseEntity.ok(equipment);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update equipment by ID")
    public ResponseEntity<EquipmentDTO> update(@PathVariable Long id,
                                              @Valid @RequestBody UpdateEquipmentRequest request) {
        EquipmentDTO updated = equipmentService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete equipment by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

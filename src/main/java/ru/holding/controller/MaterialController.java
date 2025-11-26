package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.request.CreateMaterialRequest;
import ru.holding.dto.request.UpdateMaterialRequest;
import ru.holding.dto.response.MaterialDTO;
import ru.holding.service.MaterialService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Materials", description = "Material management API")
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    @Operation(summary = "Create a new material")
    public ResponseEntity<MaterialDTO> create(@Valid @RequestBody CreateMaterialRequest request) {
        MaterialDTO created = materialService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all materials")
    public ResponseEntity<List<MaterialDTO>> getAll() {
        List<MaterialDTO> materials = materialService.getAll();
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get material by ID")
    public ResponseEntity<MaterialDTO> getById(@PathVariable Long id) {
        MaterialDTO material = materialService.getById(id);
        return ResponseEntity.ok(material);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update material by ID")
    public ResponseEntity<MaterialDTO> update(@PathVariable Long id,
                                             @Valid @RequestBody UpdateMaterialRequest request) {
        MaterialDTO updated = materialService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete material by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

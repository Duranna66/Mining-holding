package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.request.CreateEnterpriseRequest;
import ru.holding.dto.request.UpdateEnterpriseRequest;
import ru.holding.dto.response.EnterpriseDTO;
import ru.holding.service.EnterpriseService;

import java.util.List;

@RestController
@RequestMapping("/api/enterprises")
@RequiredArgsConstructor
@Tag(name = "Enterprises", description = "Enterprise management API")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    @PostMapping
    @Operation(summary = "Create a new enterprise")
    public ResponseEntity<EnterpriseDTO> create(@Valid @RequestBody CreateEnterpriseRequest request) {
        EnterpriseDTO created = enterpriseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all enterprises")
    public ResponseEntity<List<EnterpriseDTO>> getAll() {
        List<EnterpriseDTO> enterprises = enterpriseService.getAll();
        return ResponseEntity.ok(enterprises);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get enterprise by ID")
    public ResponseEntity<EnterpriseDTO> getById(@PathVariable Long id) {
        EnterpriseDTO enterprise = enterpriseService.getById(id);
        return ResponseEntity.ok(enterprise);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update enterprise by ID")
    public ResponseEntity<EnterpriseDTO> update(@PathVariable Long id,
                                                @Valid @RequestBody UpdateEnterpriseRequest request) {
        EnterpriseDTO updated = enterpriseService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete enterprise by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enterpriseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

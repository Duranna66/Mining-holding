package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.request.CreateDepartmentRequest;
import ru.holding.dto.request.UpdateDepartmentRequest;
import ru.holding.dto.response.DepartmentDTO;
import ru.holding.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Departments", description = "Department management API")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @Operation(summary = "Create a new department")
    public ResponseEntity<DepartmentDTO> create(@Valid @RequestBody CreateDepartmentRequest request) {
        DepartmentDTO created = departmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all departments or filter by enterprise ID")
    public ResponseEntity<List<DepartmentDTO>> getAll(@RequestParam(required = false) Long enterpriseId) {
        List<DepartmentDTO> departments;
        if (enterpriseId != null) {
            departments = departmentService.getByEnterpriseId(enterpriseId);
        } else {
            departments = departmentService.getAll();
        }
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<DepartmentDTO> getById(@PathVariable Long id) {
        DepartmentDTO department = departmentService.getById(id);
        return ResponseEntity.ok(department);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update department by ID")
    public ResponseEntity<DepartmentDTO> update(@PathVariable Long id,
                                               @Valid @RequestBody UpdateDepartmentRequest request) {
        DepartmentDTO updated = departmentService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

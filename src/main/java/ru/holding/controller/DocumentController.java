package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.request.CreateDocumentRequest;
import ru.holding.dto.request.UpdateDocumentRequest;
import ru.holding.dto.response.DocumentDTO;
import ru.holding.enums.DocumentStatus;
import ru.holding.service.DocumentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Documents", description = "Document management API")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    @Operation(summary = "Create a new document")
    public ResponseEntity<DocumentDTO> create(@Valid @RequestBody CreateDocumentRequest request) {
        DocumentDTO created = documentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all documents or filter by enterprise/supplier/status")
    public ResponseEntity<List<DocumentDTO>> getAll(
            @RequestParam(required = false) Long enterpriseId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) DocumentStatus status) {
        if (enterpriseId != null) {
            return ResponseEntity.ok(documentService.getByEnterpriseId(enterpriseId));
        }
        if (status != null) {
            return ResponseEntity.ok(documentService.getByStatus(status));
        }
        return ResponseEntity.ok(documentService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document by ID")
    public ResponseEntity<DocumentDTO> getById(@PathVariable Long id) {
        DocumentDTO document = documentService.getById(id);
        return ResponseEntity.ok(document);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update document by ID")
    public ResponseEntity<DocumentDTO> update(@PathVariable Long id,
                                               @Valid @RequestBody UpdateDocumentRequest request) {
        DocumentDTO updated = documentService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update document status")
    public ResponseEntity<DocumentDTO> updateStatus(@PathVariable Long id,
                                                     @RequestParam DocumentStatus status) {
        DocumentDTO updated = documentService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete document by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

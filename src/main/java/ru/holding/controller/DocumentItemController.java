package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.request.CreateDocumentItemRequest;
import ru.holding.dto.request.UpdateDocumentItemRequest;
import ru.holding.dto.response.DocumentItemDTO;
import ru.holding.service.DocumentItemService;

import java.util.List;

@RestController
@RequestMapping("/api/document-items")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Document Items", description = "Document item management API")
public class DocumentItemController {

    private final DocumentItemService itemService;

    @PostMapping
    @Operation(summary = "Create a new document item")
    public ResponseEntity<DocumentItemDTO> create(@Valid @RequestBody CreateDocumentItemRequest request) {
        DocumentItemDTO created = itemService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all document items or filter by document")
    public ResponseEntity<List<DocumentItemDTO>> getAll(
            @RequestParam(required = false) Long documentId) {
        if (documentId != null) {
            return ResponseEntity.ok(itemService.getByDocumentId(documentId));
        }
        return ResponseEntity.ok(itemService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document item by ID")
    public ResponseEntity<DocumentItemDTO> getById(@PathVariable Long id) {
        DocumentItemDTO item = itemService.getById(id);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update document item by ID")
    public ResponseEntity<DocumentItemDTO> update(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateDocumentItemRequest request) {
        DocumentItemDTO updated = itemService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete document item by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

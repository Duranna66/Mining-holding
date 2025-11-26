package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateDocumentItemRequest;
import ru.holding.dto.request.UpdateDocumentItemRequest;
import ru.holding.dto.response.DocumentItemDTO;
import ru.holding.entity.Document;
import ru.holding.entity.DocumentItem;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.repository.DocumentItemRepository;
import ru.holding.repository.DocumentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentItemService {

    private final DocumentItemRepository itemRepository;
    private final DocumentRepository documentRepository;

    @Transactional
    public DocumentItemDTO create(CreateDocumentItemRequest request) {
        log.info("Creating new document item for document: {}", request.getDocumentId());

        Document document = documentRepository.findById(request.getDocumentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Document not found with id: " + request.getDocumentId()));

        DocumentItem item = DocumentItem.builder()
                .document(document)
                .itemType(request.getItemType())
                .itemId(request.getItemId())
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .price(request.getPrice())
                .amount(request.getAmount())
                .build();

        DocumentItem saved = itemRepository.save(item);
        log.info("Document item created with ID: {}", saved.getId());

        return DocumentItemDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public DocumentItemDTO getById(Long id) {
        log.debug("Fetching document item by ID: {}", id);

        DocumentItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document item not found with id: " + id));

        return DocumentItemDTO.fromEntity(item);
    }

    @Transactional(readOnly = true)
    public List<DocumentItemDTO> getAll() {
        log.debug("Fetching all document items");

        return itemRepository.findAll().stream()
                .map(DocumentItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DocumentItemDTO> getByDocumentId(Long documentId) {
        log.debug("Fetching document items by document ID: {}", documentId);

        return itemRepository.findByDocumentId(documentId).stream()
                .map(DocumentItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public DocumentItemDTO update(Long id, UpdateDocumentItemRequest request) {
        log.info("Updating document item with ID: {}", id);

        DocumentItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document item not found with id: " + id));

        if (request.getItemType() != null) {
            item.setItemType(request.getItemType());
        }
        if (request.getItemId() != null) {
            item.setItemId(request.getItemId());
        }
        if (request.getQuantity() != null) {
            item.setQuantity(request.getQuantity());
        }
        if (request.getUnit() != null) {
            item.setUnit(request.getUnit());
        }
        if (request.getPrice() != null) {
            item.setPrice(request.getPrice());
        }
        if (request.getAmount() != null) {
            item.setAmount(request.getAmount());
        }

        DocumentItem updated = itemRepository.save(item);
        log.info("Document item updated with ID: {}", updated.getId());

        return DocumentItemDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting document item with ID: {}", id);

        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Document item not found with id: " + id);
        }

        itemRepository.deleteById(id);
        log.info("Document item deleted with ID: {}", id);
    }
}

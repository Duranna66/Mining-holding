package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateDocumentRequest;
import ru.holding.dto.request.UpdateDocumentRequest;
import ru.holding.dto.response.DocumentDTO;
import ru.holding.entity.Document;
import ru.holding.entity.Enterprise;
import ru.holding.entity.Supplier;
import ru.holding.entity.User;
import ru.holding.enums.DocumentStatus;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.exception.ValidationException;
import ru.holding.repository.DocumentRepository;
import ru.holding.repository.EnterpriseRepository;
import ru.holding.repository.SupplierRepository;
import ru.holding.repository.UserRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepo userRepository;

    @Transactional
    public DocumentDTO create(CreateDocumentRequest request) {
        log.info("Creating new document: {} - {}", request.getDocumentType(), request.getDocumentNumber());

        // Check for duplicate document number and type
        documentRepository.findByDocumentTypeAndDocumentNumber(
                request.getDocumentType(), request.getDocumentNumber()
        ).ifPresent(existing -> {
            throw new ValidationException("Document with this type and number already exists");
        });

        Enterprise enterprise = enterpriseRepository.findById(request.getEnterpriseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enterprise not found with id: " + request.getEnterpriseId()));

        Supplier supplier = null;
        if (request.getSupplierId() != null) {
            supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Supplier not found with id: " + request.getSupplierId()));
        }

        User user = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getCreatedByUserId()));

        Document document = Document.builder()
                .documentType(request.getDocumentType())
                .documentNumber(request.getDocumentNumber())
                .documentDate(request.getDocumentDate())
                .enterprise(enterprise)
                .supplier(supplier)
                .status(request.getStatus())
                .totalAmount(request.getTotalAmount())
                .currency(request.getCurrency() != null ? request.getCurrency() : "RUB")
                .notes(request.getNotes())
                .createdBy(user)
                .build();

        Document saved = documentRepository.save(document);
        log.info("Document created with ID: {}", saved.getId());

        return DocumentDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public DocumentDTO getById(Long id) {
        log.debug("Fetching document by ID: {}", id);

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));

        return DocumentDTO.fromEntity(document);
    }

    @Transactional(readOnly = true)
    public List<DocumentDTO> getAll() {
        log.debug("Fetching all documents");

        return documentRepository.findAll().stream()
                .map(DocumentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DocumentDTO> getByEnterpriseId(Long enterpriseId) {
        log.debug("Fetching documents by enterprise ID: {}", enterpriseId);

        return documentRepository.findByEnterpriseId(enterpriseId).stream()
                .map(DocumentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DocumentDTO> getByStatus(DocumentStatus status) {
        log.debug("Fetching documents by status: {}", status);

        return documentRepository.findByStatus(status).stream()
                .map(DocumentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public DocumentDTO update(Long id, UpdateDocumentRequest request) {
        log.info("Updating document with ID: {}", id);

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));

        if (request.getDocumentType() != null) {
            document.setDocumentType(request.getDocumentType());
        }
        if (request.getDocumentNumber() != null) {
            document.setDocumentNumber(request.getDocumentNumber());
        }
        if (request.getDocumentDate() != null) {
            document.setDocumentDate(request.getDocumentDate());
        }
        if (request.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Supplier not found with id: " + request.getSupplierId()));
            document.setSupplier(supplier);
        }
        if (request.getStatus() != null) {
            document.setStatus(request.getStatus());
        }
        if (request.getTotalAmount() != null) {
            document.setTotalAmount(request.getTotalAmount());
        }
        if (request.getCurrency() != null) {
            document.setCurrency(request.getCurrency());
        }
        if (request.getNotes() != null) {
            document.setNotes(request.getNotes());
        }

        Document updated = documentRepository.save(document);
        log.info("Document updated with ID: {}", updated.getId());

        return DocumentDTO.fromEntity(updated);
    }

    @Transactional
    public DocumentDTO updateStatus(Long id, DocumentStatus status) {
        log.info("Updating document status with ID: {} to {}", id, status);

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));

        document.setStatus(status);

        Document updated = documentRepository.save(document);
        log.info("Document status updated with ID: {}", updated.getId());

        return DocumentDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting document with ID: {}", id);

        if (!documentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Document not found with id: " + id);
        }

        documentRepository.deleteById(id);
        log.info("Document deleted with ID: {}", id);
    }
}

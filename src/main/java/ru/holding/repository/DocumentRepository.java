package ru.holding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.holding.entity.Document;
import ru.holding.enums.DocumentStatus;
import ru.holding.enums.DocumentType;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByEnterpriseId(Long enterpriseId);
    List<Document> findBySupplierId(Long supplierId);
    List<Document> findByStatus(DocumentStatus status);
    List<Document> findByDocumentType(DocumentType type);
    Optional<Document> findByDocumentTypeAndDocumentNumber(DocumentType type, String number);
}

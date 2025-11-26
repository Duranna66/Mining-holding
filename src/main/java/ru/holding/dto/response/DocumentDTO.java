package ru.holding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.entity.Document;
import ru.holding.enums.DocumentStatus;
import ru.holding.enums.DocumentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDTO {
    private Long id;
    private DocumentType documentType;
    private String documentNumber;
    private LocalDate documentDate;
    private Long enterpriseId;
    private String enterpriseName;
    private Long supplierId;
    private String supplierName;
    private DocumentStatus status;
    private BigDecimal totalAmount;
    private String currency;
    private String notes;
    private Long createdByUserId;
    private String createdByUserEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DocumentDTO fromEntity(Document document) {
        return DocumentDTO.builder()
                .id(document.getId())
                .documentType(document.getDocumentType())
                .documentNumber(document.getDocumentNumber())
                .documentDate(document.getDocumentDate())
                .enterpriseId(document.getEnterprise().getId())
                .enterpriseName(document.getEnterprise().getName())
                .supplierId(document.getSupplier() != null ? document.getSupplier().getId() : null)
                .supplierName(document.getSupplier() != null ? document.getSupplier().getName() : null)
                .status(document.getStatus())
                .totalAmount(document.getTotalAmount())
                .currency(document.getCurrency())
                .notes(document.getNotes())
                .createdByUserId(document.getCreatedBy().getId())
                .createdByUserEmail(document.getCreatedBy().getEmail())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}

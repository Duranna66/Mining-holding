package ru.holding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.DocumentStatus;
import ru.holding.enums.DocumentType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocumentRequest {

    @NotNull(message = "Document type is required")
    private DocumentType documentType;

    @NotBlank(message = "Document number is required")
    private String documentNumber;

    @NotNull(message = "Document date is required")
    private LocalDate documentDate;

    @NotNull(message = "Enterprise ID is required")
    private Long enterpriseId;

    private Long supplierId;

    @NotNull(message = "Status is required")
    private DocumentStatus status;

    private BigDecimal totalAmount;

    private String currency;

    private String notes;

    @NotNull(message = "Created by user ID is required")
    private Long createdByUserId;
}

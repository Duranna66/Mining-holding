package ru.holding.dto.request;

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
public class UpdateDocumentRequest {

    private DocumentType documentType;

    private String documentNumber;

    private LocalDate documentDate;

    private Long supplierId;

    private DocumentStatus status;

    private BigDecimal totalAmount;

    private String currency;

    private String notes;
}

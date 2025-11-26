package ru.holding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.CounterpartyType;
import ru.holding.enums.PaymentStatus;
import ru.holding.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {

    @NotBlank(message = "Payment number is required")
    private String paymentNumber;

    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    @NotNull(message = "Bank account ID is required")
    private Long bankAccountId;

    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;

    @NotNull(message = "Counterparty type is required")
    private CounterpartyType counterpartyType;

    private Long counterpartyId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String currency;

    private String purpose;

    private Long documentId;

    @NotNull(message = "Status is required")
    private PaymentStatus status;

    @NotNull(message = "Created by user ID is required")
    private Long createdByUserId;
}

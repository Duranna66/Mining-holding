package ru.holding.dto.request;

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
public class UpdatePaymentRequest {

    private String paymentNumber;

    private LocalDate paymentDate;

    private PaymentType paymentType;

    private CounterpartyType counterpartyType;

    private Long counterpartyId;

    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String currency;

    private String purpose;

    private Long documentId;

    private PaymentStatus status;
}

package ru.holding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.entity.Payment;
import ru.holding.enums.CounterpartyType;
import ru.holding.enums.PaymentStatus;
import ru.holding.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;
    private String paymentNumber;
    private LocalDate paymentDate;
    private Long bankAccountId;
    private String bankAccountNumber;
    private PaymentType paymentType;
    private CounterpartyType counterpartyType;
    private Long counterpartyId;
    private BigDecimal amount;
    private String currency;
    private String purpose;
    private Long documentId;
    private PaymentStatus status;
    private Long createdByUserId;
    private String createdByUserEmail;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    public static PaymentDTO fromEntity(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .paymentNumber(payment.getPaymentNumber())
                .paymentDate(payment.getPaymentDate())
                .bankAccountId(payment.getBankAccount().getId())
                .bankAccountNumber(payment.getBankAccount().getAccountNumber())
                .paymentType(payment.getPaymentType())
                .counterpartyType(payment.getCounterpartyType())
                .counterpartyId(payment.getCounterpartyId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .purpose(payment.getPurpose())
                .documentId(payment.getDocument() != null ? payment.getDocument().getId() : null)
                .status(payment.getStatus())
                .createdByUserId(payment.getCreatedBy().getId())
                .createdByUserEmail(payment.getCreatedBy().getEmail())
                .createdAt(payment.getCreatedAt())
                .processedAt(payment.getProcessedAt())
                .build();
    }
}

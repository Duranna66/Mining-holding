package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreatePaymentRequest;
import ru.holding.dto.request.UpdatePaymentRequest;
import ru.holding.dto.response.PaymentDTO;
import ru.holding.entity.BankAccount;
import ru.holding.entity.Document;
import ru.holding.entity.Payment;
import ru.holding.entity.User;
import ru.holding.enums.PaymentStatus;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.exception.ValidationException;
import ru.holding.repository.BankAccountRepository;
import ru.holding.repository.DocumentRepository;
import ru.holding.repository.PaymentRepository;
import ru.holding.repository.UserRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BankAccountRepository accountRepository;
    private final DocumentRepository documentRepository;
    private final UserRepo userRepository;

    @Transactional
    public PaymentDTO create(CreatePaymentRequest request) {
        log.info("Creating new payment: {}", request.getPaymentNumber());

        // Check if payment number already exists
        paymentRepository.findByPaymentNumber(request.getPaymentNumber())
                .ifPresent(existing -> {
                    throw new ValidationException("Payment with number " + request.getPaymentNumber() + " already exists");
                });

        BankAccount bankAccount = accountRepository.findById(request.getBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bank account not found with id: " + request.getBankAccountId()));

        Document document = null;
        if (request.getDocumentId() != null) {
            document = documentRepository.findById(request.getDocumentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Document not found with id: " + request.getDocumentId()));
        }

        User user = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getCreatedByUserId()));

        Payment payment = Payment.builder()
                .paymentNumber(request.getPaymentNumber())
                .paymentDate(request.getPaymentDate())
                .bankAccount(bankAccount)
                .paymentType(request.getPaymentType())
                .counterpartyType(request.getCounterpartyType())
                .counterpartyId(request.getCounterpartyId())
                .amount(request.getAmount())
                .currency(request.getCurrency() != null ? request.getCurrency() : "RUB")
                .purpose(request.getPurpose())
                .document(document)
                .status(request.getStatus())
                .createdBy(user)
                .build();

        Payment saved = paymentRepository.save(payment);
        log.info("Payment created with ID: {}", saved.getId());

        return PaymentDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public PaymentDTO getById(Long id) {
        log.debug("Fetching payment by ID: {}", id);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        return PaymentDTO.fromEntity(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> getAll() {
        log.debug("Fetching all payments");

        return paymentRepository.findAll().stream()
                .map(PaymentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> getByBankAccountId(Long bankAccountId) {
        log.debug("Fetching payments by bank account ID: {}", bankAccountId);

        return paymentRepository.findByBankAccountId(bankAccountId).stream()
                .map(PaymentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> getByStatus(PaymentStatus status) {
        log.debug("Fetching payments by status: {}", status);

        return paymentRepository.findByStatus(status).stream()
                .map(PaymentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentDTO updateStatus(Long id, PaymentStatus status) {
        log.info("Updating payment status with ID: {} to {}", id, status);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        payment.setStatus(status);

        // Set processed_at if status is PROCESSED
        if (status == PaymentStatus.PROCESSED && payment.getProcessedAt() == null) {
            payment.setProcessedAt(LocalDateTime.now());
        }

        Payment updated = paymentRepository.save(payment);
        log.info("Payment status updated with ID: {}", updated.getId());

        return PaymentDTO.fromEntity(updated);
    }

    @Transactional
    public PaymentDTO update(Long id, UpdatePaymentRequest request) {
        log.info("Updating payment with ID: {}", id);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        if (request.getPaymentNumber() != null && !request.getPaymentNumber().equals(payment.getPaymentNumber())) {
            // Check if new payment number already exists
            paymentRepository.findByPaymentNumber(request.getPaymentNumber())
                    .ifPresent(existing -> {
                        throw new ValidationException("Payment with number " + request.getPaymentNumber() + " already exists");
                    });
            payment.setPaymentNumber(request.getPaymentNumber());
        }
        if (request.getPaymentDate() != null) {
            payment.setPaymentDate(request.getPaymentDate());
        }
        if (request.getPaymentType() != null) {
            payment.setPaymentType(request.getPaymentType());
        }
        if (request.getCounterpartyType() != null) {
            payment.setCounterpartyType(request.getCounterpartyType());
        }
        if (request.getCounterpartyId() != null) {
            payment.setCounterpartyId(request.getCounterpartyId());
        }
        if (request.getAmount() != null) {
            payment.setAmount(request.getAmount());
        }
        if (request.getCurrency() != null) {
            payment.setCurrency(request.getCurrency());
        }
        if (request.getPurpose() != null) {
            payment.setPurpose(request.getPurpose());
        }
        if (request.getDocumentId() != null) {
            Document document = documentRepository.findById(request.getDocumentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Document not found with id: " + request.getDocumentId()));
            payment.setDocument(document);
        }
        if (request.getStatus() != null) {
            payment.setStatus(request.getStatus());
            // Set processed_at if status is PROCESSED
            if (request.getStatus() == PaymentStatus.PROCESSED && payment.getProcessedAt() == null) {
                payment.setProcessedAt(LocalDateTime.now());
            }
        }

        Payment updated = paymentRepository.save(payment);
        log.info("Payment updated with ID: {}", updated.getId());

        return PaymentDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting payment with ID: {}", id);

        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }

        paymentRepository.deleteById(id);
        log.info("Payment deleted with ID: {}", id);
    }
}

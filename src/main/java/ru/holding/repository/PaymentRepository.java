package ru.holding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.holding.entity.Payment;
import ru.holding.enums.PaymentStatus;
import ru.holding.enums.PaymentType;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBankAccountId(Long bankAccountId);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByPaymentType(PaymentType type);
    List<Payment> findByDocumentId(Long documentId);
    Optional<Payment> findByPaymentNumber(String paymentNumber);
}

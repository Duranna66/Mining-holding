package ru.holding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.holding.entity.BankAccount;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByEnterpriseId(Long enterpriseId);
    List<BankAccount> findByIsActive(Boolean isActive);
    Optional<BankAccount> findByAccountNumber(String accountNumber);
}

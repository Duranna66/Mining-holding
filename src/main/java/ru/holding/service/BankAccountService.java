package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateBankAccountRequest;
import ru.holding.dto.request.UpdateBankAccountRequest;
import ru.holding.dto.response.BankAccountDTO;
import ru.holding.entity.BankAccount;
import ru.holding.entity.Enterprise;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.exception.ValidationException;
import ru.holding.repository.BankAccountRepository;
import ru.holding.repository.EnterpriseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankAccountService {

    private final BankAccountRepository accountRepository;
    private final EnterpriseRepository enterpriseRepository;

    @Transactional
    public BankAccountDTO create(CreateBankAccountRequest request) {
        log.info("Creating new bank account: {}", request.getAccountNumber());

        // Check if account number already exists
        accountRepository.findByAccountNumber(request.getAccountNumber())
                .ifPresent(existing -> {
                    throw new ValidationException("Bank account with number " + request.getAccountNumber() + " already exists");
                });

        Enterprise enterprise = enterpriseRepository.findById(request.getEnterpriseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enterprise not found with id: " + request.getEnterpriseId()));

        BankAccount account = BankAccount.builder()
                .enterprise(enterprise)
                .accountNumber(request.getAccountNumber())
                .bankName(request.getBankName())
                .bik(request.getBik())
                .correspondentAccount(request.getCorrespondentAccount())
                .currency(request.getCurrency() != null ? request.getCurrency() : "RUB")
                .isActive(true)
                .build();

        BankAccount saved = accountRepository.save(account);
        log.info("Bank account created with ID: {}", saved.getId());

        return BankAccountDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public BankAccountDTO getById(Long id) {
        log.debug("Fetching bank account by ID: {}", id);

        BankAccount account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with id: " + id));

        return BankAccountDTO.fromEntity(account);
    }

    @Transactional(readOnly = true)
    public List<BankAccountDTO> getAll() {
        log.debug("Fetching all bank accounts");

        return accountRepository.findAll().stream()
                .map(BankAccountDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BankAccountDTO> getByEnterpriseId(Long enterpriseId) {
        log.debug("Fetching bank accounts by enterprise ID: {}", enterpriseId);

        return accountRepository.findByEnterpriseId(enterpriseId).stream()
                .map(BankAccountDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BankAccountDTO> getActiveAccounts() {
        log.debug("Fetching active bank accounts");

        return accountRepository.findByIsActive(true).stream()
                .map(BankAccountDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public BankAccountDTO update(Long id, UpdateBankAccountRequest request) {
        log.info("Updating bank account with ID: {}", id);

        BankAccount account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with id: " + id));

        if (request.getAccountNumber() != null && !request.getAccountNumber().equals(account.getAccountNumber())) {
            // Check if new account number already exists
            accountRepository.findByAccountNumber(request.getAccountNumber())
                    .ifPresent(existing -> {
                        throw new ValidationException("Bank account with number " + request.getAccountNumber() + " already exists");
                    });
            account.setAccountNumber(request.getAccountNumber());
        }
        if (request.getBankName() != null) {
            account.setBankName(request.getBankName());
        }
        if (request.getBik() != null) {
            account.setBik(request.getBik());
        }
        if (request.getCorrespondentAccount() != null) {
            account.setCorrespondentAccount(request.getCorrespondentAccount());
        }
        if (request.getCurrency() != null) {
            account.setCurrency(request.getCurrency());
        }

        BankAccount updated = accountRepository.save(account);
        log.info("Bank account updated with ID: {}", updated.getId());

        return BankAccountDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deactivating bank account with ID: {}", id);

        BankAccount account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with id: " + id));

        account.setIsActive(false);
        accountRepository.save(account);
        log.info("Bank account deactivated with ID: {}", id);
    }
}

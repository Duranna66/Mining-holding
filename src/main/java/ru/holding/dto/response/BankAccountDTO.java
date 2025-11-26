package ru.holding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.entity.BankAccount;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountDTO {
    private Long id;
    private Long enterpriseId;
    private String enterpriseName;
    private String accountNumber;
    private String bankName;
    private String bik;
    private String correspondentAccount;
    private String currency;
    private Boolean isActive;
    private LocalDateTime createdAt;

    public static BankAccountDTO fromEntity(BankAccount account) {
        return BankAccountDTO.builder()
                .id(account.getId())
                .enterpriseId(account.getEnterprise().getId())
                .enterpriseName(account.getEnterprise().getName())
                .accountNumber(account.getAccountNumber())
                .bankName(account.getBankName())
                .bik(account.getBik())
                .correspondentAccount(account.getCorrespondentAccount())
                .currency(account.getCurrency())
                .isActive(account.getIsActive())
                .createdAt(account.getCreatedAt())
                .build();
    }
}

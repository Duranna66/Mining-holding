package ru.holding.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBankAccountRequest {

    @Size(max = 20, message = "Account number must not exceed 20 characters")
    private String accountNumber;

    private String bankName;

    @Size(min = 9, max = 9, message = "BIK must be 9 characters")
    private String bik;

    @Size(max = 20, message = "Correspondent account must not exceed 20 characters")
    private String correspondentAccount;

    private String currency;
}

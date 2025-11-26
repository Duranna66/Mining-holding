package ru.holding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBankAccountRequest {

    @NotNull(message = "Enterprise ID is required")
    private Long enterpriseId;

    @NotBlank(message = "Account number is required")
    @Size(max = 20, message = "Account number must not exceed 20 characters")
    private String accountNumber;

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotBlank(message = "BIK is required")
    @Size(min = 9, max = 9, message = "BIK must be 9 characters")
    private String bik;

    @NotBlank(message = "Correspondent account is required")
    @Size(max = 20, message = "Correspondent account must not exceed 20 characters")
    private String correspondentAccount;

    private String currency;
}

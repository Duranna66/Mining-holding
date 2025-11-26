package ru.holding.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.SupplierType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSupplierRequest {

    private String name;

    private String legalName;

    @Size(min = 10, max = 12, message = "INN must be 10-12 characters")
    private String inn;

    @Size(min = 9, max = 9, message = "KPP must be 9 characters")
    private String kpp;

    private String address;

    private String contactPerson;

    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private SupplierType supplierType;

    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;
}

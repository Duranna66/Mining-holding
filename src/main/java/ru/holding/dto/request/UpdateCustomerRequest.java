package ru.holding.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.CustomerType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequest {

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

    private CustomerType customerType;
}

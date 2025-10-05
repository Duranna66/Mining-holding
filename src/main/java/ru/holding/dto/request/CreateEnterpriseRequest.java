package ru.holding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.EnterpriseType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEnterpriseRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Type is required")
    private EnterpriseType type;

    @NotBlank(message = "Location is required")
    private String location;
}

package ru.holding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.DepartmentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDepartmentRequest {

    @NotNull(message = "Enterprise ID is required")
    private Long enterpriseId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Type is required")
    private DepartmentType type;
}

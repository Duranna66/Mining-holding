package ru.holding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.EquipmentStatus;
import ru.holding.enums.EquipmentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEquipmentRequest {

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Type is required")
    private EquipmentType type;

    @NotBlank(message = "Inventory number is required")
    private String inventoryNumber;

    @NotNull(message = "Status is required")
    private EquipmentStatus status;
}

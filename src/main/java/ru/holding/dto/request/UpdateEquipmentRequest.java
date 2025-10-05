package ru.holding.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.EquipmentStatus;
import ru.holding.enums.EquipmentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEquipmentRequest {
    private Long departmentId;
    private String name;
    private EquipmentType type;
    private String inventoryNumber;
    private EquipmentStatus status;
}

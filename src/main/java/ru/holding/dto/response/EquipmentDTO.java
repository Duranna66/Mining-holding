package ru.holding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.entity.Equipment;
import ru.holding.enums.EquipmentStatus;
import ru.holding.enums.EquipmentType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentDTO {
    private Long id;
    private Long departmentId;
    private String name;
    private EquipmentType type;
    private String inventoryNumber;
    private EquipmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EquipmentDTO fromEntity(Equipment equipment) {
        return EquipmentDTO.builder()
                .id(equipment.getId())
                .departmentId(equipment.getDepartmentId())
                .name(equipment.getName())
                .type(equipment.getType())
                .inventoryNumber(equipment.getInventoryNumber())
                .status(equipment.getStatus())
                .createdAt(equipment.getCreatedAt())
                .updatedAt(equipment.getUpdatedAt())
                .build();
    }
}

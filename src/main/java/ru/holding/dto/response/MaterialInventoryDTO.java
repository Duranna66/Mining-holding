package ru.holding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.entity.MaterialInventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialInventoryDTO {
    private Long id;
    private Long warehouseId;
    private String warehouseName;
    private Long materialId;
    private String materialName;
    private BigDecimal quantity;
    private String unit;
    private LocalDateTime lastUpdated;

    public static MaterialInventoryDTO fromEntity(MaterialInventory inventory) {
        return MaterialInventoryDTO.builder()
                .id(inventory.getId())
                .warehouseId(inventory.getWarehouse().getId())
                .warehouseName(inventory.getWarehouse().getName())
                .materialId(inventory.getMaterial().getId())
                .materialName(inventory.getMaterial().getName())
                .quantity(inventory.getQuantity())
                .unit(inventory.getUnit())
                .lastUpdated(inventory.getLastUpdated())
                .build();
    }
}

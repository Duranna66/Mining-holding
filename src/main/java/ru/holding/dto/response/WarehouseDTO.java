package ru.holding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.entity.Warehouse;
import ru.holding.enums.WarehouseType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseDTO {
    private Long id;
    private Long enterpriseId;
    private String enterpriseName;
    private String name;
    private WarehouseType type;
    private BigDecimal capacity;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static WarehouseDTO fromEntity(Warehouse warehouse) {
        return WarehouseDTO.builder()
                .id(warehouse.getId())
                .enterpriseId(warehouse.getEnterprise().getId())
                .enterpriseName(warehouse.getEnterprise().getName())
                .name(warehouse.getName())
                .type(warehouse.getType())
                .capacity(warehouse.getCapacity())
                .address(warehouse.getAddress())
                .createdAt(warehouse.getCreatedAt())
                .updatedAt(warehouse.getUpdatedAt())
                .build();
    }
}

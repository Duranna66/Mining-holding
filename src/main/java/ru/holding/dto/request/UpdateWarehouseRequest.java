package ru.holding.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.WarehouseType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWarehouseRequest {

    private String name;

    private WarehouseType type;

    @Positive(message = "Capacity must be positive")
    private BigDecimal capacity;

    private String address;
}

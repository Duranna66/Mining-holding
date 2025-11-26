package ru.holding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.WarehouseType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWarehouseRequest {

    @NotNull(message = "Enterprise ID is required")
    private Long enterpriseId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Type is required")
    private WarehouseType type;

    @Positive(message = "Capacity must be positive")
    private BigDecimal capacity;

    private String address;
}

package ru.holding.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMaterialInventoryRequest {

    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    private String unit;
}

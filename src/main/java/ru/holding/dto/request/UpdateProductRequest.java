package ru.holding.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.ProductCategory;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    private String name;

    private String productCode;

    private ProductCategory category;

    private String unit;

    private String description;

    @Positive(message = "Standard cost must be positive")
    private BigDecimal standardCost;
}

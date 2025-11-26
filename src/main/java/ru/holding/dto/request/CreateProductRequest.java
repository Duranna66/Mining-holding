package ru.holding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.ProductCategory;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Product code is required")
    private String productCode;

    @NotNull(message = "Category is required")
    private ProductCategory category;

    @NotBlank(message = "Unit is required")
    private String unit;

    private String description;

    @Positive(message = "Standard cost must be positive")
    private BigDecimal standardCost;
}

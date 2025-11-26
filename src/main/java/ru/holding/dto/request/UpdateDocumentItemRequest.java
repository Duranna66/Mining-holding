package ru.holding.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.ItemType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDocumentItemRequest {

    private ItemType itemType;

    private Long itemId;

    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    private String unit;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}

package ru.holding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.entity.DocumentItem;
import ru.holding.enums.ItemType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentItemDTO {
    private Long id;
    private Long documentId;
    private ItemType itemType;
    private Long itemId;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal price;
    private BigDecimal amount;

    public static DocumentItemDTO fromEntity(DocumentItem item) {
        return DocumentItemDTO.builder()
                .id(item.getId())
                .documentId(item.getDocument().getId())
                .itemType(item.getItemType())
                .itemId(item.getItemId())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .price(item.getPrice())
                .amount(item.getAmount())
                .build();
    }
}

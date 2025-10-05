package ru.holding.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.MaterialCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMaterialRequest {
    private String name;
    private MaterialCategory category;
    private String unit;
    private String description;
}

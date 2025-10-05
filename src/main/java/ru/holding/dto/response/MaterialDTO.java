package ru.holding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.entity.Material;
import ru.holding.enums.MaterialCategory;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialDTO {
    private Long id;
    private String name;
    private MaterialCategory category;
    private String unit;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MaterialDTO fromEntity(Material material) {
        return MaterialDTO.builder()
                .id(material.getId())
                .name(material.getName())
                .category(material.getCategory())
                .unit(material.getUnit())
                .description(material.getDescription())
                .createdAt(material.getCreatedAt())
                .updatedAt(material.getUpdatedAt())
                .build();
    }
}

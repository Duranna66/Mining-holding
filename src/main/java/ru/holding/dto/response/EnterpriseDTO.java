package ru.holding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.entity.Enterprise;
import ru.holding.enums.EnterpriseType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnterpriseDTO {
    private Long id;
    private String name;
    private EnterpriseType type;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EnterpriseDTO fromEntity(Enterprise enterprise) {
        return EnterpriseDTO.builder()
                .id(enterprise.getId())
                .name(enterprise.getName())
                .type(enterprise.getType())
                .location(enterprise.getLocation())
                .createdAt(enterprise.getCreatedAt())
                .updatedAt(enterprise.getUpdatedAt())
                .build();
    }
}

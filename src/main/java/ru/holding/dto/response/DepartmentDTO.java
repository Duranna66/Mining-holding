package ru.holding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.entity.Department;
import ru.holding.enums.DepartmentType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO {
    private Long id;
    private Long enterpriseId;
    private String name;
    private DepartmentType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DepartmentDTO fromEntity(Department department) {
        return DepartmentDTO.builder()
                .id(department.getId())
                .enterpriseId(department.getEnterpriseId())
                .name(department.getName())
                .type(department.getType())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
}

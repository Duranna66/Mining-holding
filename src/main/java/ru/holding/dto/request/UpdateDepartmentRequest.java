package ru.holding.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.DepartmentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDepartmentRequest {
    private Long enterpriseId;
    private String name;
    private DepartmentType type;
}

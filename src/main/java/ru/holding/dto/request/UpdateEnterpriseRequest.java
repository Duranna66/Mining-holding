package ru.holding.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.EnterpriseType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEnterpriseRequest {
    private String name;
    private EnterpriseType type;
    private String location;
}

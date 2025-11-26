package ru.holding.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.LandUsageType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLandPlotRequest {

    private String plotNumber;

    private String cadastralNumber;

    @Positive(message = "Area must be positive")
    private BigDecimal areaHectares;

    private String soilType;

    private String location;

    private LandUsageType usageType;
}

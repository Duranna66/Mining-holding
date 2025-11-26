package ru.holding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.LandUsageType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLandPlotRequest {

    @NotNull(message = "Enterprise ID is required")
    private Long enterpriseId;

    @NotBlank(message = "Plot number is required")
    private String plotNumber;

    private String cadastralNumber;

    @NotNull(message = "Area in hectares is required")
    @Positive(message = "Area must be positive")
    private BigDecimal areaHectares;

    private String soilType;

    private String location;

    @NotNull(message = "Usage type is required")
    private LandUsageType usageType;
}

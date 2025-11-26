package ru.holding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.entity.LandPlot;
import ru.holding.enums.LandUsageType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandPlotDTO {
    private Long id;
    private Long enterpriseId;
    private String enterpriseName;
    private String plotNumber;
    private String cadastralNumber;
    private BigDecimal areaHectares;
    private String soilType;
    private String location;
    private LandUsageType usageType;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LandPlotDTO fromEntity(LandPlot landPlot) {
        return LandPlotDTO.builder()
                .id(landPlot.getId())
                .enterpriseId(landPlot.getEnterprise().getId())
                .enterpriseName(landPlot.getEnterprise().getName())
                .plotNumber(landPlot.getPlotNumber())
                .cadastralNumber(landPlot.getCadastralNumber())
                .areaHectares(landPlot.getAreaHectares())
                .soilType(landPlot.getSoilType())
                .location(landPlot.getLocation())
                .usageType(landPlot.getUsageType())
                .isActive(landPlot.getIsActive())
                .createdAt(landPlot.getCreatedAt())
                .updatedAt(landPlot.getUpdatedAt())
                .build();
    }
}

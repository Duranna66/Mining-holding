package ru.holding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.holding.entity.LandPlot;
import ru.holding.enums.LandUsageType;

import java.util.List;
import java.util.Optional;

@Repository
public interface LandPlotRepository extends JpaRepository<LandPlot, Long> {
    List<LandPlot> findByEnterpriseId(Long enterpriseId);
    List<LandPlot> findByUsageType(LandUsageType usageType);
    List<LandPlot> findByIsActive(Boolean isActive);
    Optional<LandPlot> findByEnterpriseIdAndPlotNumber(Long enterpriseId, String plotNumber);
}

package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateLandPlotRequest;
import ru.holding.dto.request.UpdateLandPlotRequest;
import ru.holding.dto.response.LandPlotDTO;
import ru.holding.entity.Enterprise;
import ru.holding.entity.LandPlot;
import ru.holding.enums.LandUsageType;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.exception.ValidationException;
import ru.holding.repository.EnterpriseRepository;
import ru.holding.repository.LandPlotRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LandPlotService {

    private final LandPlotRepository landPlotRepository;
    private final EnterpriseRepository enterpriseRepository;

    @Transactional
    public LandPlotDTO create(CreateLandPlotRequest request) {
        log.info("Creating new land plot: {}", request.getPlotNumber());

        Enterprise enterprise = enterpriseRepository.findById(request.getEnterpriseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enterprise not found with id: " + request.getEnterpriseId()));

        // Check if plot number already exists for this enterprise
        landPlotRepository.findByEnterpriseIdAndPlotNumber(
                request.getEnterpriseId(), request.getPlotNumber()
        ).ifPresent(existing -> {
            throw new ValidationException("Land plot with number " + request.getPlotNumber() +
                    " already exists for this enterprise");
        });

        LandPlot landPlot = LandPlot.builder()
                .enterprise(enterprise)
                .plotNumber(request.getPlotNumber())
                .cadastralNumber(request.getCadastralNumber())
                .areaHectares(request.getAreaHectares())
                .soilType(request.getSoilType())
                .location(request.getLocation())
                .usageType(request.getUsageType())
                .isActive(true)
                .build();

        LandPlot saved = landPlotRepository.save(landPlot);
        log.info("Land plot created with ID: {}", saved.getId());

        return LandPlotDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public LandPlotDTO getById(Long id) {
        log.debug("Fetching land plot by ID: {}", id);

        LandPlot landPlot = landPlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Land plot not found with id: " + id));

        return LandPlotDTO.fromEntity(landPlot);
    }

    @Transactional(readOnly = true)
    public List<LandPlotDTO> getAll() {
        log.debug("Fetching all land plots");

        return landPlotRepository.findAll().stream()
                .map(LandPlotDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LandPlotDTO> getByEnterpriseId(Long enterpriseId) {
        log.debug("Fetching land plots by enterprise ID: {}", enterpriseId);

        return landPlotRepository.findByEnterpriseId(enterpriseId).stream()
                .map(LandPlotDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LandPlotDTO> getByUsageType(LandUsageType usageType) {
        log.debug("Fetching land plots by usage type: {}", usageType);

        return landPlotRepository.findByUsageType(usageType).stream()
                .map(LandPlotDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public LandPlotDTO update(Long id, UpdateLandPlotRequest request) {
        log.info("Updating land plot with ID: {}", id);

        LandPlot landPlot = landPlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Land plot not found with id: " + id));

        if (request.getPlotNumber() != null && !request.getPlotNumber().equals(landPlot.getPlotNumber())) {
            // Check if new plot number already exists for this enterprise
            landPlotRepository.findByEnterpriseIdAndPlotNumber(
                    landPlot.getEnterprise().getId(), request.getPlotNumber()
            ).ifPresent(existing -> {
                throw new ValidationException("Land plot with number " + request.getPlotNumber() +
                        " already exists for this enterprise");
            });
            landPlot.setPlotNumber(request.getPlotNumber());
        }
        if (request.getCadastralNumber() != null) {
            landPlot.setCadastralNumber(request.getCadastralNumber());
        }
        if (request.getAreaHectares() != null) {
            landPlot.setAreaHectares(request.getAreaHectares());
        }
        if (request.getSoilType() != null) {
            landPlot.setSoilType(request.getSoilType());
        }
        if (request.getLocation() != null) {
            landPlot.setLocation(request.getLocation());
        }
        if (request.getUsageType() != null) {
            landPlot.setUsageType(request.getUsageType());
        }

        LandPlot updated = landPlotRepository.save(landPlot);
        log.info("Land plot updated with ID: {}", updated.getId());

        return LandPlotDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deactivating land plot with ID: {}", id);

        LandPlot landPlot = landPlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Land plot not found with id: " + id));

        landPlot.setIsActive(false);
        landPlotRepository.save(landPlot);
        log.info("Land plot deactivated with ID: {}", id);
    }
}

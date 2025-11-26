package ru.holding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.request.CreateLandPlotRequest;
import ru.holding.dto.request.UpdateLandPlotRequest;
import ru.holding.dto.response.LandPlotDTO;
import ru.holding.enums.LandUsageType;
import ru.holding.service.LandPlotService;

import java.util.List;

@RestController
@RequestMapping("/api/land-plots")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Land Plots", description = "Land plot management API")
public class LandPlotController {

    private final LandPlotService landPlotService;

    @PostMapping
    @Operation(summary = "Create a new land plot")
    public ResponseEntity<LandPlotDTO> create(@Valid @RequestBody CreateLandPlotRequest request) {
        LandPlotDTO created = landPlotService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all land plots or filter by enterprise/usage type/active status")
    public ResponseEntity<List<LandPlotDTO>> getAll(
            @RequestParam(required = false) Long enterpriseId,
            @RequestParam(required = false) LandUsageType usageType,
            @RequestParam(required = false) Boolean active) {
        if (enterpriseId != null) {
            return ResponseEntity.ok(landPlotService.getByEnterpriseId(enterpriseId));
        }
        if (usageType != null) {
            return ResponseEntity.ok(landPlotService.getByUsageType(usageType));
        }
        return ResponseEntity.ok(landPlotService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get land plot by ID")
    public ResponseEntity<LandPlotDTO> getById(@PathVariable Long id) {
        LandPlotDTO landPlot = landPlotService.getById(id);
        return ResponseEntity.ok(landPlot);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update land plot by ID")
    public ResponseEntity<LandPlotDTO> update(@PathVariable Long id,
                                               @Valid @RequestBody UpdateLandPlotRequest request) {
        LandPlotDTO updated = landPlotService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate land plot by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        landPlotService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

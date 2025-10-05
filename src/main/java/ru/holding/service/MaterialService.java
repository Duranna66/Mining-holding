package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateMaterialRequest;
import ru.holding.dto.request.UpdateMaterialRequest;
import ru.holding.dto.response.MaterialDTO;
import ru.holding.entity.Material;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.repository.MaterialRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialService {

    private final MaterialRepository materialRepository;

    @Transactional
    public MaterialDTO create(CreateMaterialRequest request) {
        log.info("Creating new material: {}", request.getName());

        Material material = Material.builder()
                .name(request.getName())
                .category(request.getCategory())
                .unit(request.getUnit())
                .description(request.getDescription())
                .build();

        Material saved = materialRepository.save(material);
        log.info("Material created with ID: {}", saved.getId());

        return MaterialDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public MaterialDTO getById(Long id) {
        log.debug("Fetching material by ID: {}", id);

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + id));

        return MaterialDTO.fromEntity(material);
    }

    @Transactional(readOnly = true)
    public List<MaterialDTO> getAll() {
        log.debug("Fetching all materials");

        return materialRepository.findAll().stream()
                .map(MaterialDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public MaterialDTO update(Long id, UpdateMaterialRequest request) {
        log.info("Updating material with ID: {}", id);

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + id));

        if (request.getName() != null) {
            material.setName(request.getName());
        }
        if (request.getCategory() != null) {
            material.setCategory(request.getCategory());
        }
        if (request.getUnit() != null) {
            material.setUnit(request.getUnit());
        }
        if (request.getDescription() != null) {
            material.setDescription(request.getDescription());
        }

        Material updated = materialRepository.save(material);
        log.info("Material updated with ID: {}", updated.getId());

        return MaterialDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting material with ID: {}", id);

        if (!materialRepository.existsById(id)) {
            throw new ResourceNotFoundException("Material not found with id: " + id);
        }

        materialRepository.deleteById(id);
        log.info("Material deleted with ID: {}", id);
    }
}

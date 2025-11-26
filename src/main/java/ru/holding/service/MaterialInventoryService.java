package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateMaterialInventoryRequest;
import ru.holding.dto.request.UpdateMaterialInventoryRequest;
import ru.holding.dto.response.MaterialInventoryDTO;
import ru.holding.entity.Material;
import ru.holding.entity.MaterialInventory;
import ru.holding.entity.Warehouse;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.exception.ValidationException;
import ru.holding.repository.MaterialInventoryRepository;
import ru.holding.repository.MaterialRepository;
import ru.holding.repository.WarehouseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialInventoryService {

    private final MaterialInventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final MaterialRepository materialRepository;

    @Transactional
    public MaterialInventoryDTO create(CreateMaterialInventoryRequest request) {
        log.info("Creating material inventory for warehouse: {} and material: {}",
                request.getWarehouseId(), request.getMaterialId());

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found with id: " + request.getWarehouseId()));

        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Material not found with id: " + request.getMaterialId()));

        // Check if inventory already exists
        inventoryRepository.findByWarehouseIdAndMaterialId(
                request.getWarehouseId(), request.getMaterialId()
        ).ifPresent(existing -> {
            throw new ValidationException("Inventory already exists for this warehouse and material");
        });

        MaterialInventory inventory = MaterialInventory.builder()
                .warehouse(warehouse)
                .material(material)
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .build();

        MaterialInventory saved = inventoryRepository.save(inventory);
        log.info("Material inventory created with ID: {}", saved.getId());

        return MaterialInventoryDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public MaterialInventoryDTO getById(Long id) {
        log.debug("Fetching material inventory by ID: {}", id);

        MaterialInventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material inventory not found with id: " + id));

        return MaterialInventoryDTO.fromEntity(inventory);
    }

    @Transactional(readOnly = true)
    public List<MaterialInventoryDTO> getAll() {
        log.debug("Fetching all material inventories");

        return inventoryRepository.findAll().stream()
                .map(MaterialInventoryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MaterialInventoryDTO> getByWarehouseId(Long warehouseId) {
        log.debug("Fetching material inventories by warehouse ID: {}", warehouseId);

        return inventoryRepository.findByWarehouseId(warehouseId).stream()
                .map(MaterialInventoryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MaterialInventoryDTO> getByMaterialId(Long materialId) {
        log.debug("Fetching material inventories by material ID: {}", materialId);

        return inventoryRepository.findByMaterialId(materialId).stream()
                .map(MaterialInventoryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public MaterialInventoryDTO update(Long id, UpdateMaterialInventoryRequest request) {
        log.info("Updating material inventory with ID: {}", id);

        MaterialInventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material inventory not found with id: " + id));

        if (request.getQuantity() != null) {
            inventory.setQuantity(request.getQuantity());
        }
        if (request.getUnit() != null) {
            inventory.setUnit(request.getUnit());
        }

        MaterialInventory updated = inventoryRepository.save(inventory);
        log.info("Material inventory updated with ID: {}", updated.getId());

        return MaterialInventoryDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting material inventory with ID: {}", id);

        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Material inventory not found with id: " + id);
        }

        inventoryRepository.deleteById(id);
        log.info("Material inventory deleted with ID: {}", id);
    }
}

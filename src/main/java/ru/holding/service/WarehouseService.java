package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateWarehouseRequest;
import ru.holding.dto.request.UpdateWarehouseRequest;
import ru.holding.dto.response.WarehouseDTO;
import ru.holding.entity.Enterprise;
import ru.holding.entity.Warehouse;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.repository.EnterpriseRepository;
import ru.holding.repository.WarehouseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final EnterpriseRepository enterpriseRepository;

    @Transactional
    public WarehouseDTO create(CreateWarehouseRequest request) {
        log.info("Creating new warehouse: {}", request.getName());

        Enterprise enterprise = enterpriseRepository.findById(request.getEnterpriseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enterprise not found with id: " + request.getEnterpriseId()));

        Warehouse warehouse = Warehouse.builder()
                .enterprise(enterprise)
                .name(request.getName())
                .type(request.getType())
                .capacity(request.getCapacity())
                .address(request.getAddress())
                .build();

        Warehouse saved = warehouseRepository.save(warehouse);
        log.info("Warehouse created with ID: {}", saved.getId());

        return WarehouseDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public WarehouseDTO getById(Long id) {
        log.debug("Fetching warehouse by ID: {}", id);

        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

        return WarehouseDTO.fromEntity(warehouse);
    }

    @Transactional(readOnly = true)
    public List<WarehouseDTO> getAll() {
        log.debug("Fetching all warehouses");

        return warehouseRepository.findAll().stream()
                .map(WarehouseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WarehouseDTO> getByEnterpriseId(Long enterpriseId) {
        log.debug("Fetching warehouses by enterprise ID: {}", enterpriseId);

        return warehouseRepository.findByEnterpriseId(enterpriseId).stream()
                .map(WarehouseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public WarehouseDTO update(Long id, UpdateWarehouseRequest request) {
        log.info("Updating warehouse with ID: {}", id);

        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

        if (request.getName() != null) {
            warehouse.setName(request.getName());
        }
        if (request.getType() != null) {
            warehouse.setType(request.getType());
        }
        if (request.getCapacity() != null) {
            warehouse.setCapacity(request.getCapacity());
        }
        if (request.getAddress() != null) {
            warehouse.setAddress(request.getAddress());
        }

        Warehouse updated = warehouseRepository.save(warehouse);
        log.info("Warehouse updated with ID: {}", updated.getId());

        return WarehouseDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting warehouse with ID: {}", id);

        if (!warehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse not found with id: " + id);
        }

        warehouseRepository.deleteById(id);
        log.info("Warehouse deleted with ID: {}", id);
    }
}

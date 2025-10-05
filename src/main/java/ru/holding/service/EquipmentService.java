package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateEquipmentRequest;
import ru.holding.dto.request.UpdateEquipmentRequest;
import ru.holding.dto.response.EquipmentDTO;
import ru.holding.entity.Equipment;
import ru.holding.enums.EquipmentStatus;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.exception.ValidationException;
import ru.holding.repository.DepartmentRepository;
import ru.holding.repository.EquipmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public EquipmentDTO create(CreateEquipmentRequest request) {
        log.info("Creating new equipment: {}", request.getName());

        // Validate that department exists
        if (!departmentRepository.existsById(request.getDepartmentId())) {
            throw new ValidationException("Department not found with id: " + request.getDepartmentId());
        }

        // Validate inventory number uniqueness
        if (equipmentRepository.findByInventoryNumber(request.getInventoryNumber()).isPresent()) {
            throw new ValidationException("Equipment with inventory number " + request.getInventoryNumber() + " already exists");
        }

        Equipment equipment = Equipment.builder()
                .departmentId(request.getDepartmentId())
                .name(request.getName())
                .type(request.getType())
                .inventoryNumber(request.getInventoryNumber())
                .status(request.getStatus())
                .build();

        Equipment saved = equipmentRepository.save(equipment);
        log.info("Equipment created with ID: {}", saved.getId());

        return EquipmentDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public EquipmentDTO getById(Long id) {
        log.debug("Fetching equipment by ID: {}", id);

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));

        return EquipmentDTO.fromEntity(equipment);
    }

    @Transactional(readOnly = true)
    public List<EquipmentDTO> getAll() {
        log.debug("Fetching all equipment");

        return equipmentRepository.findAll().stream()
                .map(EquipmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EquipmentDTO> getByDepartmentId(Long departmentId) {
        log.debug("Fetching equipment by department ID: {}", departmentId);

        return equipmentRepository.findByDepartmentId(departmentId).stream()
                .map(EquipmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EquipmentDTO> getByStatus(EquipmentStatus status) {
        log.debug("Fetching equipment by status: {}", status);

        return equipmentRepository.findByStatus(status).stream()
                .map(EquipmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public EquipmentDTO update(Long id, UpdateEquipmentRequest request) {
        log.info("Updating equipment with ID: {}", id);

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));

        if (request.getDepartmentId() != null) {
            if (!departmentRepository.existsById(request.getDepartmentId())) {
                throw new ValidationException("Department not found with id: " + request.getDepartmentId());
            }
            equipment.setDepartmentId(request.getDepartmentId());
        }
        if (request.getName() != null) {
            equipment.setName(request.getName());
        }
        if (request.getType() != null) {
            equipment.setType(request.getType());
        }
        if (request.getInventoryNumber() != null) {
            // Check uniqueness if inventory number is being changed
            if (!equipment.getInventoryNumber().equals(request.getInventoryNumber())) {
                if (equipmentRepository.findByInventoryNumber(request.getInventoryNumber()).isPresent()) {
                    throw new ValidationException("Equipment with inventory number " + request.getInventoryNumber() + " already exists");
                }
            }
            equipment.setInventoryNumber(request.getInventoryNumber());
        }
        if (request.getStatus() != null) {
            equipment.setStatus(request.getStatus());
        }

        Equipment updated = equipmentRepository.save(equipment);
        log.info("Equipment updated with ID: {}", updated.getId());

        return EquipmentDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting equipment with ID: {}", id);

        if (!equipmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipment not found with id: " + id);
        }

        equipmentRepository.deleteById(id);
        log.info("Equipment deleted with ID: {}", id);
    }
}

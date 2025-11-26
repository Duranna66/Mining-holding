package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateSupplierRequest;
import ru.holding.dto.request.UpdateSupplierRequest;
import ru.holding.dto.response.SupplierDTO;
import ru.holding.entity.Supplier;
import ru.holding.enums.SupplierType;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.repository.SupplierRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Transactional
    public SupplierDTO create(CreateSupplierRequest request) {
        log.info("Creating new supplier: {}", request.getName());

        Supplier supplier = Supplier.builder()
                .name(request.getName())
                .legalName(request.getLegalName())
                .inn(request.getInn())
                .kpp(request.getKpp())
                .address(request.getAddress())
                .contactPerson(request.getContactPerson())
                .phone(request.getPhone())
                .email(request.getEmail())
                .supplierType(request.getSupplierType())
                .rating(request.getRating())
                .isActive(true)
                .build();

        Supplier saved = supplierRepository.save(supplier);
        log.info("Supplier created with ID: {}", saved.getId());

        return SupplierDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public SupplierDTO getById(Long id) {
        log.debug("Fetching supplier by ID: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        return SupplierDTO.fromEntity(supplier);
    }

    @Transactional(readOnly = true)
    public List<SupplierDTO> getAll() {
        log.debug("Fetching all suppliers");

        return supplierRepository.findAll().stream()
                .map(SupplierDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SupplierDTO> getByType(SupplierType type) {
        log.debug("Fetching suppliers by type: {}", type);

        return supplierRepository.findBySupplierType(type).stream()
                .map(SupplierDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SupplierDTO> getActiveSuppliers() {
        log.debug("Fetching active suppliers");

        return supplierRepository.findByIsActive(true).stream()
                .map(SupplierDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public SupplierDTO update(Long id, UpdateSupplierRequest request) {
        log.info("Updating supplier with ID: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        if (request.getName() != null) {
            supplier.setName(request.getName());
        }
        if (request.getLegalName() != null) {
            supplier.setLegalName(request.getLegalName());
        }
        if (request.getInn() != null) {
            supplier.setInn(request.getInn());
        }
        if (request.getKpp() != null) {
            supplier.setKpp(request.getKpp());
        }
        if (request.getAddress() != null) {
            supplier.setAddress(request.getAddress());
        }
        if (request.getContactPerson() != null) {
            supplier.setContactPerson(request.getContactPerson());
        }
        if (request.getPhone() != null) {
            supplier.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            supplier.setEmail(request.getEmail());
        }
        if (request.getSupplierType() != null) {
            supplier.setSupplierType(request.getSupplierType());
        }
        if (request.getRating() != null) {
            supplier.setRating(request.getRating());
        }

        Supplier updated = supplierRepository.save(supplier);
        log.info("Supplier updated with ID: {}", updated.getId());

        return SupplierDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deactivating supplier with ID: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        supplier.setIsActive(false);
        supplierRepository.save(supplier);
        log.info("Supplier deactivated with ID: {}", id);
    }
}

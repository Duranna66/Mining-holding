package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateEnterpriseRequest;
import ru.holding.dto.request.UpdateEnterpriseRequest;
import ru.holding.dto.response.EnterpriseDTO;
import ru.holding.entity.Enterprise;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.repository.EnterpriseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;

    @Transactional
    public EnterpriseDTO create(CreateEnterpriseRequest request) {
        log.info("Creating new enterprise: {}", request.getName());

        Enterprise enterprise = Enterprise.builder()
                .name(request.getName())
                .type(request.getType())
                .location(request.getLocation())
                .build();

        Enterprise saved = enterpriseRepository.save(enterprise);
        log.info("Enterprise created with ID: {}", saved.getId());

        return EnterpriseDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public EnterpriseDTO getById(Long id) {
        log.debug("Fetching enterprise by ID: {}", id);

        Enterprise enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enterprise not found with id: " + id));

        return EnterpriseDTO.fromEntity(enterprise);
    }

    @Transactional(readOnly = true)
    public List<EnterpriseDTO> getAll() {
        log.debug("Fetching all enterprises");

        return enterpriseRepository.findAll().stream()
                .map(EnterpriseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnterpriseDTO update(Long id, UpdateEnterpriseRequest request) {
        log.info("Updating enterprise with ID: {}", id);

        Enterprise enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enterprise not found with id: " + id));

        if (request.getName() != null) {
            enterprise.setName(request.getName());
        }
        if (request.getType() != null) {
            enterprise.setType(request.getType());
        }
        if (request.getLocation() != null) {
            enterprise.setLocation(request.getLocation());
        }

        Enterprise updated = enterpriseRepository.save(enterprise);
        log.info("Enterprise updated with ID: {}", updated.getId());

        return EnterpriseDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting enterprise with ID: {}", id);

        if (!enterpriseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enterprise not found with id: " + id);
        }

        enterpriseRepository.deleteById(id);
        log.info("Enterprise deleted with ID: {}", id);
    }
}

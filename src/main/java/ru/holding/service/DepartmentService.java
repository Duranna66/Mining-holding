package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateDepartmentRequest;
import ru.holding.dto.request.UpdateDepartmentRequest;
import ru.holding.dto.response.DepartmentDTO;
import ru.holding.entity.Department;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.exception.ValidationException;
import ru.holding.repository.DepartmentRepository;
import ru.holding.repository.EnterpriseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EnterpriseRepository enterpriseRepository;

    @Transactional
    public DepartmentDTO create(CreateDepartmentRequest request) {
        log.info("Creating new department: {}", request.getName());

        // Validate that enterprise exists
        if (!enterpriseRepository.existsById(request.getEnterpriseId())) {
            throw new ValidationException("Enterprise not found with id: " + request.getEnterpriseId());
        }

        Department department = Department.builder()
                .enterpriseId(request.getEnterpriseId())
                .name(request.getName())
                .type(request.getType())
                .build();

        Department saved = departmentRepository.save(department);
        log.info("Department created with ID: {}", saved.getId());

        return DepartmentDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public DepartmentDTO getById(Long id) {
        log.debug("Fetching department by ID: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        return DepartmentDTO.fromEntity(department);
    }

    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAll() {
        log.debug("Fetching all departments");

        return departmentRepository.findAll().stream()
                .map(DepartmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DepartmentDTO> getByEnterpriseId(Long enterpriseId) {
        log.debug("Fetching departments by enterprise ID: {}", enterpriseId);

        return departmentRepository.findByEnterpriseId(enterpriseId).stream()
                .map(DepartmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public DepartmentDTO update(Long id, UpdateDepartmentRequest request) {
        log.info("Updating department with ID: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (request.getEnterpriseId() != null) {
            if (!enterpriseRepository.existsById(request.getEnterpriseId())) {
                throw new ValidationException("Enterprise not found with id: " + request.getEnterpriseId());
            }
            department.setEnterpriseId(request.getEnterpriseId());
        }
        if (request.getName() != null) {
            department.setName(request.getName());
        }
        if (request.getType() != null) {
            department.setType(request.getType());
        }

        Department updated = departmentRepository.save(department);
        log.info("Department updated with ID: {}", updated.getId());

        return DepartmentDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting department with ID: {}", id);

        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }

        departmentRepository.deleteById(id);
        log.info("Department deleted with ID: {}", id);
    }
}

package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateCustomerRequest;
import ru.holding.dto.request.UpdateCustomerRequest;
import ru.holding.dto.response.CustomerDTO;
import ru.holding.entity.Customer;
import ru.holding.enums.CustomerType;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.repository.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerDTO create(CreateCustomerRequest request) {
        log.info("Creating new customer: {}", request.getName());

        Customer customer = Customer.builder()
                .name(request.getName())
                .legalName(request.getLegalName())
                .inn(request.getInn())
                .kpp(request.getKpp())
                .address(request.getAddress())
                .contactPerson(request.getContactPerson())
                .phone(request.getPhone())
                .email(request.getEmail())
                .customerType(request.getCustomerType())
                .isActive(true)
                .build();

        Customer saved = customerRepository.save(customer);
        log.info("Customer created with ID: {}", saved.getId());

        return CustomerDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public CustomerDTO getById(Long id) {
        log.debug("Fetching customer by ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        return CustomerDTO.fromEntity(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> getAll() {
        log.debug("Fetching all customers");

        return customerRepository.findAll().stream()
                .map(CustomerDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> getByType(CustomerType type) {
        log.debug("Fetching customers by type: {}", type);

        return customerRepository.findByCustomerType(type).stream()
                .map(CustomerDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> getActiveCustomers() {
        log.debug("Fetching active customers");

        return customerRepository.findByIsActive(true).stream()
                .map(CustomerDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerDTO update(Long id, UpdateCustomerRequest request) {
        log.info("Updating customer with ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        if (request.getName() != null) {
            customer.setName(request.getName());
        }
        if (request.getLegalName() != null) {
            customer.setLegalName(request.getLegalName());
        }
        if (request.getInn() != null) {
            customer.setInn(request.getInn());
        }
        if (request.getKpp() != null) {
            customer.setKpp(request.getKpp());
        }
        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }
        if (request.getContactPerson() != null) {
            customer.setContactPerson(request.getContactPerson());
        }
        if (request.getPhone() != null) {
            customer.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            customer.setEmail(request.getEmail());
        }
        if (request.getCustomerType() != null) {
            customer.setCustomerType(request.getCustomerType());
        }

        Customer updated = customerRepository.save(customer);
        log.info("Customer updated with ID: {}", updated.getId());

        return CustomerDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deactivating customer with ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setIsActive(false);
        customerRepository.save(customer);
        log.info("Customer deactivated with ID: {}", id);
    }
}

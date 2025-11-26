package ru.holding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.holding.entity.Customer;
import ru.holding.enums.CustomerType;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByIsActive(Boolean isActive);
    List<Customer> findByCustomerType(CustomerType type);
    Optional<Customer> findByInn(String inn);
}

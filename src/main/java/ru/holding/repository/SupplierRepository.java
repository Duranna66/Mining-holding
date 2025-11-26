package ru.holding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.holding.entity.Supplier;
import ru.holding.enums.SupplierType;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByIsActive(Boolean isActive);
    List<Supplier> findBySupplierType(SupplierType type);
    Optional<Supplier> findByInn(String inn);
}

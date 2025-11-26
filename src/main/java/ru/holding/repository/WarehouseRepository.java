package ru.holding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.holding.entity.Warehouse;
import ru.holding.enums.WarehouseType;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByEnterpriseId(Long enterpriseId);
    List<Warehouse> findByType(WarehouseType type);
}

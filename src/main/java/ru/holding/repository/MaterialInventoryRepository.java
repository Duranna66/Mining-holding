package ru.holding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.holding.entity.MaterialInventory;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialInventoryRepository extends JpaRepository<MaterialInventory, Long> {
    List<MaterialInventory> findByWarehouseId(Long warehouseId);
    List<MaterialInventory> findByMaterialId(Long materialId);
    Optional<MaterialInventory> findByWarehouseIdAndMaterialId(Long warehouseId, Long materialId);
}

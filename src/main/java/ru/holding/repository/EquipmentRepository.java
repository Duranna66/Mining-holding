package ru.holding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.holding.entity.Equipment;
import ru.holding.enums.EquipmentStatus;
import ru.holding.enums.EquipmentType;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByDepartmentId(Long departmentId);
    List<Equipment> findByType(EquipmentType type);
    List<Equipment> findByStatus(EquipmentStatus status);
    Optional<Equipment> findByInventoryNumber(String inventoryNumber);
}

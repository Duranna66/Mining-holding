package ru.holding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.holding.entity.Department;
import ru.holding.enums.DepartmentType;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByEnterpriseId(Long enterpriseId);
    List<Department> findByType(DepartmentType type);
}

package ru.holding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.holding.entity.Product;
import ru.holding.enums.ProductCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsActive(Boolean isActive);
    List<Product> findByCategory(ProductCategory category);
    Optional<Product> findByProductCode(String productCode);
}

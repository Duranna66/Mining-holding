package ru.holding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.request.CreateProductRequest;
import ru.holding.dto.request.UpdateProductRequest;
import ru.holding.dto.response.ProductDTO;
import ru.holding.entity.Product;
import ru.holding.enums.ProductCategory;
import ru.holding.exception.ResourceNotFoundException;
import ru.holding.exception.ValidationException;
import ru.holding.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductDTO create(CreateProductRequest request) {
        log.info("Creating new product: {}", request.getName());

        // Check if product code already exists
        productRepository.findByProductCode(request.getProductCode())
                .ifPresent(existing -> {
                    throw new ValidationException("Product with code " + request.getProductCode() + " already exists");
                });

        Product product = Product.builder()
                .name(request.getName())
                .productCode(request.getProductCode())
                .category(request.getCategory())
                .unit(request.getUnit())
                .description(request.getDescription())
                .standardCost(request.getStandardCost())
                .isActive(true)
                .build();

        Product saved = productRepository.save(product);
        log.info("Product created with ID: {}", saved.getId());

        return ProductDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public ProductDTO getById(Long id) {
        log.debug("Fetching product by ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return ProductDTO.fromEntity(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getAll() {
        log.debug("Fetching all products");

        return productRepository.findAll().stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getByCategory(ProductCategory category) {
        log.debug("Fetching products by category: {}", category);

        return productRepository.findByCategory(category).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getActiveProducts() {
        log.debug("Fetching active products");

        return productRepository.findByIsActive(true).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDTO update(Long id, UpdateProductRequest request) {
        log.info("Updating product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getProductCode() != null && !request.getProductCode().equals(product.getProductCode())) {
            // Check if new product code already exists
            productRepository.findByProductCode(request.getProductCode())
                    .ifPresent(existing -> {
                        throw new ValidationException("Product with code " + request.getProductCode() + " already exists");
                    });
            product.setProductCode(request.getProductCode());
        }
        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }
        if (request.getUnit() != null) {
            product.setUnit(request.getUnit());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getStandardCost() != null) {
            product.setStandardCost(request.getStandardCost());
        }

        Product updated = productRepository.save(product);
        log.info("Product updated with ID: {}", updated.getId());

        return ProductDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deactivating product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setIsActive(false);
        productRepository.save(product);
        log.info("Product deactivated with ID: {}", id);
    }
}

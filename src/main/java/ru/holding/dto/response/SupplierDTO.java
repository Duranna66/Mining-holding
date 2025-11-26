package ru.holding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.entity.Supplier;
import ru.holding.enums.SupplierType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDTO {
    private Long id;
    private String name;
    private String legalName;
    private String inn;
    private String kpp;
    private String address;
    private String contactPerson;
    private String phone;
    private String email;
    private SupplierType supplierType;
    private Integer rating;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SupplierDTO fromEntity(Supplier supplier) {
        return SupplierDTO.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .legalName(supplier.getLegalName())
                .inn(supplier.getInn())
                .kpp(supplier.getKpp())
                .address(supplier.getAddress())
                .contactPerson(supplier.getContactPerson())
                .phone(supplier.getPhone())
                .email(supplier.getEmail())
                .supplierType(supplier.getSupplierType())
                .rating(supplier.getRating())
                .isActive(supplier.getIsActive())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }
}

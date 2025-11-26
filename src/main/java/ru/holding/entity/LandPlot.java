package ru.holding.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.holding.enums.LandUsageType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "land_plots",
        uniqueConstraints = @UniqueConstraint(columnNames = {"enterprise_id", "plot_number"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandPlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;

    @Column(name = "plot_number", nullable = false, length = 50)
    private String plotNumber;

    @Column(name = "cadastral_number", length = 100)
    private String cadastralNumber;

    @Column(name = "area_hectares", nullable = false, precision = 10, scale = 4)
    private BigDecimal areaHectares;

    @Column(name = "soil_type", length = 100)
    private String soilType;

    @Column(length = 500)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "usage_type", nullable = false, length = 50)
    private LandUsageType usageType;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

-- Create enterprises table
CREATE TABLE IF NOT EXISTS enterprises (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT chk_enterprise_type CHECK (type IN ('MINING', 'PROCESSING', 'OFFICE'))
);

-- Create departments table
CREATE TABLE IF NOT EXISTS departments (
    id BIGSERIAL PRIMARY KEY,
    enterprise_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_department_enterprise FOREIGN KEY (enterprise_id) REFERENCES enterprises(id) ON DELETE CASCADE,
    CONSTRAINT chk_department_type CHECK (type IN ('MINE', 'WORKSHOP', 'REPAIR_SERVICE', 'WAREHOUSE', 'FINANCE', 'HR', 'ADMINISTRATION'))
);

-- Create materials table
CREATE TABLE IF NOT EXISTS materials (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT chk_material_category CHECK (category IN ('RAW_MATERIAL', 'SPARE_PART', 'REAGENT', 'FUEL', 'OTHER'))
);

-- Create equipment table
CREATE TABLE IF NOT EXISTS equipment (
    id BIGSERIAL PRIMARY KEY,
    department_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    inventory_number VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_equipment_department FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE,
    CONSTRAINT chk_equipment_type CHECK (type IN ('EXCAVATOR', 'TRUCK', 'FURNACE', 'REACTOR', 'CONVEYOR', 'OTHER')),
    CONSTRAINT chk_equipment_status CHECK (status IN ('OPERATIONAL', 'UNDER_REPAIR', 'IDLE', 'DECOMMISSIONED'))
);


CREATE TABLE IF NOT EXISTS warehouses (
                                          id BIGSERIAL PRIMARY KEY,
                                          enterprise_id BIGINT NOT NULL,
                                          name VARCHAR(255) NOT NULL,
                                          type VARCHAR(50) NOT NULL,
                                          capacity DECIMAL(15,2),
                                          address VARCHAR(500),
                                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          CONSTRAINT fk_warehouse_enterprise FOREIGN KEY (enterprise_id) REFERENCES enterprises(id) ON DELETE CASCADE,
                                          CONSTRAINT chk_warehouse_type CHECK (type IN ('RAW_MATERIALS', 'FINISHED_GOODS', 'EQUIPMENT', 'COLD_STORAGE'))
);

-- 2. Таблица остатков материалов (критична для операционной деятельности)
CREATE TABLE IF NOT EXISTS material_inventory (
                                                  id BIGSERIAL PRIMARY KEY,
                                                  warehouse_id BIGINT NOT NULL,
                                                  material_id BIGINT NOT NULL,
                                                  quantity DECIMAL(15,3) NOT NULL DEFAULT 0,
                                                  unit VARCHAR(20) NOT NULL,
                                                  last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                  CONSTRAINT fk_inventory_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE CASCADE,
                                                  CONSTRAINT fk_inventory_material FOREIGN KEY (material_id) REFERENCES materials(id) ON DELETE CASCADE,
                                                  CONSTRAINT chk_quantity_positive CHECK (quantity >= 0),
                                                  UNIQUE (warehouse_id, material_id)
);

-- 3. Таблица поставщиков (основа закупочной деятельности)
CREATE TABLE IF NOT EXISTS suppliers (
                                         id BIGSERIAL PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
                                         legal_name VARCHAR(500),
                                         inn VARCHAR(12),
                                         kpp VARCHAR(9),
                                         address VARCHAR(500),
                                         contact_person VARCHAR(255),
                                         phone VARCHAR(50),
                                         email VARCHAR(255),
                                         supplier_type VARCHAR(50),
                                         rating INTEGER,
                                         is_active BOOLEAN DEFAULT true,
                                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         CONSTRAINT chk_supplier_type CHECK (supplier_type IN ('SEEDS', 'FERTILIZERS', 'EQUIPMENT', 'FUEL', 'SERVICES', 'OTHER')),
                                         CONSTRAINT chk_rating CHECK (rating >= 1 AND rating <= 5)
);

-- 4. Таблица продукции (что производим и продаем)
CREATE TABLE IF NOT EXISTS products (
                                        id BIGSERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
                                        product_code VARCHAR(50) UNIQUE,
                                        category VARCHAR(100) NOT NULL,
                                        unit VARCHAR(20) NOT NULL,
                                        description TEXT,
                                        standard_cost DECIMAL(15,2),
                                        is_active BOOLEAN DEFAULT true,
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        CONSTRAINT chk_product_category CHECK (category IN ('GRAIN', 'VEGETABLES', 'MEAT', 'DAIRY', 'PROCESSED', 'OTHER'))
);

-- 5. Таблица земельных участков (основной актив агрохолдинга)
CREATE TABLE IF NOT EXISTS land_plots (
                                          id BIGSERIAL PRIMARY KEY,
                                          enterprise_id BIGINT NOT NULL,
                                          plot_number VARCHAR(50) NOT NULL,
                                          cadastral_number VARCHAR(100),
                                          area_hectares DECIMAL(10,4) NOT NULL,
                                          soil_type VARCHAR(100),
                                          location VARCHAR(500),
                                          usage_type VARCHAR(50),
                                          is_active BOOLEAN DEFAULT true,
                                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          CONSTRAINT fk_plot_enterprise FOREIGN KEY (enterprise_id) REFERENCES enterprises(id) ON DELETE CASCADE,
                                          CONSTRAINT chk_usage_type CHECK (usage_type IN ('ARABLE', 'PASTURE', 'HAY_FIELD', 'ORCHARD', 'FALLOW', 'OTHER')),
                                          UNIQUE (enterprise_id, plot_number)
);

-- Create indexes for foreign keys
CREATE INDEX IF NOT EXISTS idx_departments_enterprise_id ON departments(enterprise_id);
CREATE INDEX IF NOT EXISTS idx_equipment_department_id ON equipment(department_id);

CREATE INDEX IF NOT EXISTS idx_enterprises_type ON enterprises(type);
CREATE INDEX IF NOT EXISTS idx_departments_type ON departments(type);
CREATE INDEX IF NOT EXISTS idx_materials_category ON materials(category);
CREATE INDEX IF NOT EXISTS idx_equipment_type ON equipment(type);
CREATE INDEX IF NOT EXISTS idx_equipment_status ON equipment(status);

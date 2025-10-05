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

-- Create indexes for foreign keys
CREATE INDEX idx_departments_enterprise_id ON departments(enterprise_id);
CREATE INDEX idx_equipment_department_id ON equipment(department_id);

-- Create indexes for frequently queried columns
CREATE INDEX idx_enterprises_type ON enterprises(type);
CREATE INDEX idx_departments_type ON departments(type);
CREATE INDEX idx_materials_category ON materials(category);
CREATE INDEX idx_equipment_type ON equipment(type);
CREATE INDEX idx_equipment_status ON equipment(status);

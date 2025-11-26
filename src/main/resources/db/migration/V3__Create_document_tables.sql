-- Универсальная таблица документов
CREATE TABLE IF NOT EXISTS documents (
                                         id BIGSERIAL PRIMARY KEY,
                                         document_type VARCHAR(50) NOT NULL,
                                         document_number VARCHAR(100) NOT NULL,
                                         document_date DATE NOT NULL,
                                         enterprise_id BIGINT NOT NULL,
                                         supplier_id BIGINT,
                                         status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
                                         total_amount DECIMAL(15,2),
                                         currency VARCHAR(3) DEFAULT 'RUB',
                                         notes TEXT,
                                         created_by BIGINT NOT NULL,
                                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         CONSTRAINT fk_doc_enterprise FOREIGN KEY (enterprise_id) REFERENCES enterprises(id),
                                         CONSTRAINT fk_doc_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
                                         CONSTRAINT fk_doc_creator FOREIGN KEY (created_by) REFERENCES user_authenticated(id),
                                         CONSTRAINT chk_doc_type CHECK (document_type IN ('INVOICE', 'WAYBILL', 'PAYMENT_ORDER', 'CONTRACT', 'ACT', 'OTHER')),
                                         CONSTRAINT chk_doc_status CHECK (status IN ('DRAFT', 'APPROVED', 'CANCELLED', 'COMPLETED')),
                                         UNIQUE (document_type, document_number)
);

-- Позиции документов (детализация)
CREATE TABLE IF NOT EXISTS document_items (
                                              id BIGSERIAL PRIMARY KEY,
                                              document_id BIGINT NOT NULL,
                                              item_type VARCHAR(50) NOT NULL,
                                              item_id BIGINT NOT NULL,
                                              quantity DECIMAL(15,3) NOT NULL,
                                              unit VARCHAR(20) NOT NULL,
                                              price DECIMAL(15,2) NOT NULL,
                                              amount DECIMAL(15,2) NOT NULL,
                                              CONSTRAINT fk_item_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
                                              CONSTRAINT chk_item_type CHECK (item_type IN ('MATERIAL', 'PRODUCT', 'SERVICE'))
);
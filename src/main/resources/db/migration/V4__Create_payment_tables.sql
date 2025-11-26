-- Счета компании (расчетные счета)
CREATE TABLE IF NOT EXISTS bank_accounts (
                                             id BIGSERIAL PRIMARY KEY,
                                             enterprise_id BIGINT NOT NULL,
                                             account_number VARCHAR(20) NOT NULL UNIQUE,
                                             bank_name VARCHAR(255) NOT NULL,
                                             bik VARCHAR(9) NOT NULL,
                                             correspondent_account VARCHAR(20),
                                             currency VARCHAR(3) DEFAULT 'RUB',
                                             is_active BOOLEAN DEFAULT true,
                                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             CONSTRAINT fk_account_enterprise FOREIGN KEY (enterprise_id) REFERENCES enterprises(id) ON DELETE CASCADE
);

-- Платежи
CREATE TABLE IF NOT EXISTS payments (
                                        id BIGSERIAL PRIMARY KEY,
                                        payment_number VARCHAR(100) NOT NULL UNIQUE,
                                        payment_date DATE NOT NULL,
                                        bank_account_id BIGINT NOT NULL,
                                        payment_type VARCHAR(50) NOT NULL,
                                        counterparty_type VARCHAR(50) NOT NULL,
                                        counterparty_id BIGINT,
                                        amount DECIMAL(15,2) NOT NULL,
                                        currency VARCHAR(3) DEFAULT 'RUB',
                                        purpose TEXT NOT NULL,
                                        document_id BIGINT,
                                        status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                                        created_by BIGINT NOT NULL,
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        processed_at TIMESTAMP,
                                        CONSTRAINT fk_payment_account FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id),
                                        CONSTRAINT fk_payment_document FOREIGN KEY (document_id) REFERENCES documents(id),
                                        CONSTRAINT fk_payment_creator FOREIGN KEY (created_by) REFERENCES user_authenticated(id),
                                        CONSTRAINT chk_payment_type CHECK (payment_type IN ('INCOMING', 'OUTGOING')),
                                        CONSTRAINT chk_counterparty_type CHECK (counterparty_type IN ('SUPPLIER', 'CUSTOMER', 'EMPLOYEE', 'TAX', 'OTHER')),
                                        CONSTRAINT chk_payment_status CHECK (status IN ('PENDING', 'PROCESSED', 'CANCELLED', 'FAILED'))
);

-- Клиенты/покупатели
CREATE TABLE IF NOT EXISTS customers (
                                         id BIGSERIAL PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
                                         legal_name VARCHAR(500),
                                         inn VARCHAR(12),
                                         kpp VARCHAR(9),
                                         address VARCHAR(500),
                                         contact_person VARCHAR(255),
                                         phone VARCHAR(50),
                                         email VARCHAR(255),
                                         customer_type VARCHAR(50),
                                         is_active BOOLEAN DEFAULT true,
                                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         CONSTRAINT chk_customer_type CHECK (customer_type IN ('WHOLESALE', 'RETAIL', 'EXPORT', 'GOVERNMENT', 'OTHER'))
);
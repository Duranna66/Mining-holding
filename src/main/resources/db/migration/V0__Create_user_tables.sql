-- Create user_authenticated table
CREATE TABLE IF NOT EXISTS user_authenticated (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    account_enable BOOLEAN DEFAULT true,
    credentials_expired BOOLEAN DEFAULT false,
    account_expired BOOLEAN DEFAULT false,
    account_locked BOOLEAN DEFAULT false
);

-- Create role table
CREATE TABLE IF NOT EXISTS role (
    role_id BIGSERIAL PRIMARY KEY,
    role VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT role_user_fk FOREIGN KEY (user_id) REFERENCES user_authenticated(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_role_user_id ON role(user_id);

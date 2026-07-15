-- Redesign: switch to UUID primary keys, rename tables, add version column
DROP TABLE IF EXISTS verifications;
DROP TABLE IF EXISTS verification;
DROP TABLE IF EXISTS documents;
DROP TABLE IF EXISTS document;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS customer;

CREATE TABLE IF NOT EXISTS customers (
    id              UUID          PRIMARY KEY,
    full_name       VARCHAR(150)  NOT NULL,
    email           VARCHAR(150)  NOT NULL UNIQUE,
    phone           VARCHAR(20),
    created_at      TIMESTAMPTZ   NOT NULL,
    updated_at      TIMESTAMPTZ,
    version         BIGINT
);

CREATE INDEX idx_customer_email ON customers (email);

CREATE TABLE IF NOT EXISTS documents (
    id              UUID          PRIMARY KEY,
    customer_id     UUID          NOT NULL REFERENCES customers (id) ON DELETE CASCADE,
    file_name       VARCHAR(255)  NOT NULL,
    content_type    VARCHAR(100),
    file_size       BIGINT,
    s3_key          VARCHAR(500)  NOT NULL,
    status          VARCHAR(30)   NOT NULL,
    uploaded_at     TIMESTAMPTZ,
    created_at      TIMESTAMPTZ   NOT NULL,
    updated_at      TIMESTAMPTZ
);

CREATE INDEX idx_document_customer_id ON documents (customer_id);

CREATE TABLE IF NOT EXISTS verifications (
    id                  UUID          PRIMARY KEY,
    document_id         UUID          NOT NULL REFERENCES documents (id) ON DELETE CASCADE,
    customer_id         UUID          NOT NULL REFERENCES customers (id) ON DELETE CASCADE,
    status              VARCHAR(30)   NOT NULL DEFAULT 'PENDING',
    provider_reference  VARCHAR(255),
    remarks             VARCHAR(1000),
    created_at          TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_verification_document_id ON verifications (document_id);
CREATE INDEX idx_verification_customer_id ON verifications (customer_id);

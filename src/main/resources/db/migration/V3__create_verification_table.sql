CREATE TABLE IF NOT EXISTS verification (
    id                  BIGSERIAL PRIMARY KEY,
    document_id         BIGINT        NOT NULL REFERENCES document (id) ON DELETE CASCADE,
    customer_id         BIGINT        NOT NULL REFERENCES customer (id) ON DELETE CASCADE,
    status              VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    verification_type   VARCHAR(50),
    remarks             TEXT,
    verified_by         VARCHAR(100),
    verified_at         TIMESTAMP,
    created_at          TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_verification_document_id ON verification (document_id);
CREATE INDEX idx_verification_customer_id ON verification (customer_id);

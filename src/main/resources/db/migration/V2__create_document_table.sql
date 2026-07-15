CREATE TABLE IF NOT EXISTS document (
    id              BIGSERIAL PRIMARY KEY,
    customer_id     BIGINT        NOT NULL REFERENCES customer (id) ON DELETE CASCADE,
    document_type   VARCHAR(50)   NOT NULL,
    file_name       VARCHAR(255)  NOT NULL,
    s3_key          VARCHAR(500)  NOT NULL,
    file_size       BIGINT,
    mime_type       VARCHAR(100),
    status          VARCHAR(20)   NOT NULL DEFAULT 'UPLOADED',
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_document_customer_id ON document (customer_id);

CREATE TABLE IF NOT EXISTS role (
                                    id          BIGSERIAL PRIMARY KEY,
                                    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    state       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP
    );

-- Unicidad (sensible a mayúsculas/minúsculas)
ALTER TABLE role ADD CONSTRAINT uq_role_name UNIQUE (name);


-- V1: creación inicial de la tabla 'usuario'
-- Nota: nombres en snake_case para alinear con Hibernate (fullName -> full_name, etc.)

CREATE TABLE IF NOT EXISTS usuario (
                         id          BIGSERIAL PRIMARY KEY,
                         full_name   VARCHAR(255),
                         email       VARCHAR(255) NOT NULL,
                         password    VARCHAR(255),
                         created_at  TIMESTAMP     NOT NULL DEFAULT NOW(),
                         updated_at  TIMESTAMP
);

-- Unicidad por email (crea también su índice único subyacente)
ALTER TABLE usuario
    ADD CONSTRAINT uq_usuario_email UNIQUE (email);


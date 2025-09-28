CREATE TABLE IF NOT EXISTS usuario_roles (
                                             usuario_id BIGINT  NOT NULL,
                                             role_id    BIGINT  NOT NULL,
                                             active     BOOLEAN NOT NULL DEFAULT TRUE,
                                             created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,

    CONSTRAINT pk_usuario_roles PRIMARY KEY (usuario_id, role_id),

    CONSTRAINT fk_usrrole_usuario FOREIGN KEY (usuario_id)
    REFERENCES usuario(id) ON DELETE CASCADE,

    CONSTRAINT fk_usrrole_role FOREIGN KEY (role_id)
    REFERENCES role(id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS ix_usr_roles_usuario ON usuario_roles(usuario_id);
CREATE INDEX IF NOT EXISTS ix_usr_roles_role    ON usuario_roles(role_id);

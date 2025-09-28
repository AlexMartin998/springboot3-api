package com.ecommerce.api.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class UserRoleId implements Serializable {

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "role_id")
    private Long roleId;

    // requerido por JPA
    public UserRoleId() {
    }

    public UserRoleId(Long usuarioId, Long roleId) {
        this.usuarioId = usuarioId;
        this.roleId = roleId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    // obligatorio para PK compuesta
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleId that)) return false;
        return Objects.equals(usuarioId, that.usuarioId) &&
                Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, roleId);
    }
}

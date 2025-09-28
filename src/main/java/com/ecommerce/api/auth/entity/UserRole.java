package com.ecommerce.api.auth.entity;

import com.ecommerce.api.users.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario_roles",
        indexes = {
                @Index(name = "ix_usr_roles_usuario", columnList = "usuario_id"),
                @Index(name = "ix_usr_roles_role", columnList = "role_id")
        })
@EntityListeners(AuditingEntityListener.class)
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "fk_usrrole_usuario"))
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_usrrole_role"))
    private Role role;

    @Column(nullable = false)
    private boolean active = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


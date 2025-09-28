package com.ecommerce.api.users.entity;

import com.ecommerce.api.auth.entity.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;


@Data // getters/setters, toString, equals, hashCode, @RequiredArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incremental
    private Long id;

    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    @Column(columnDefinition = "boolean default true")
    private boolean active = true;


    // Relación con UserRole (M:N hacia Role)
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<UserRole> userRoles = new LinkedHashSet<>();


    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // // manual el createdAt y updatedAt con los callbacks de JPA @PrePersist y @PreUpdate
    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}

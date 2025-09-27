package com.ecommerce.api.users.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


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

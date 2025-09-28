package com.ecommerce.api.auth.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDTO {

    private Long id;
    private String name;
    private String description;
    private boolean state;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;

}

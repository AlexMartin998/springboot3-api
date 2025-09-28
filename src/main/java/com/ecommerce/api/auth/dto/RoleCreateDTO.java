package com.ecommerce.api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleCreateDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;

}

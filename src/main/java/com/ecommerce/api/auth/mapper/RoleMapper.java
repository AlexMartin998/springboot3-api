package com.ecommerce.api.auth.mapper;

import com.ecommerce.api.auth.dto.RoleCreateDTO;
import com.ecommerce.api.auth.dto.RoleResponseDTO;
import com.ecommerce.api.auth.dto.RoleUpdateDTO;
import com.ecommerce.api.auth.entity.Role;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toEntity(RoleCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(RoleUpdateDTO dto, @MappingTarget Role entity);

    RoleResponseDTO toResponse(Role entity);

}

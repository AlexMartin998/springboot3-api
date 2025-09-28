package com.ecommerce.api.auth.service;

import com.ecommerce.api.auth.dto.RoleCreateDTO;
import com.ecommerce.api.auth.dto.RoleResponseDTO;
import com.ecommerce.api.auth.dto.RoleUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RoleService {

    RoleResponseDTO create(RoleCreateDTO dto);

    RoleResponseDTO update(Long id, RoleUpdateDTO dto);

    void delete(Long id);

    RoleResponseDTO getById(Long id);

    Page<RoleResponseDTO> getAll(Pageable pageable);

    Page<RoleResponseDTO> getSearch(String q, Boolean state, Pageable pageable);

}

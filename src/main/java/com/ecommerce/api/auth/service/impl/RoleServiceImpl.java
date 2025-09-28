package com.ecommerce.api.auth.service.impl;

import com.ecommerce.api.auth.dto.RoleCreateDTO;
import com.ecommerce.api.auth.dto.RoleResponseDTO;
import com.ecommerce.api.auth.dto.RoleUpdateDTO;
import com.ecommerce.api.auth.entity.Role;
import com.ecommerce.api.auth.mapper.RoleMapper;
import com.ecommerce.api.auth.repository.RoleRepository;
import com.ecommerce.api.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    @Override
    public RoleResponseDTO create(RoleCreateDTO dto) {
        Role e = mapper.toEntity(dto);
        e = repository.save(e);
        return mapper.toResponse(e);
    }

    @Override
    public RoleResponseDTO update(Long id, RoleUpdateDTO dto) {
        Role e = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        mapper.updateEntityFromDto(dto, e);
        e = repository.save(e);
        return mapper.toResponse(e);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDTO getById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleResponseDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public Page<RoleResponseDTO> getSearch(String q, Boolean state, Pageable pageable) {
        return repository.findAllFiltered(q, state, pageable).map(mapper::toResponse);
    }

}

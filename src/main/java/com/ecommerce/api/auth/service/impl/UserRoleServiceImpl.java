package com.ecommerce.api.auth.service.impl;

import com.ecommerce.api.auth.entity.Role;
import com.ecommerce.api.auth.entity.UserRole;
import com.ecommerce.api.auth.entity.UserRoleId;
import com.ecommerce.api.auth.repository.RoleRepository;
import com.ecommerce.api.auth.repository.UserRoleRepository;
import com.ecommerce.api.auth.service.UserRoleService;
import com.ecommerce.api.shared.exceptions.ResourceNotFoundException;
import com.ecommerce.api.users.entity.Usuario;
import com.ecommerce.api.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    @Override
    public void assign(Long usuarioId, Long roleId) {
        if (userRoleRepo.existsByUsuario_IdAndRole_Id(usuarioId, roleId)) return; // idempotente

        Usuario u = userRepo.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + usuarioId + " not found"));
        Role r = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id " + roleId + " not found"));

        UserRole ur = UserRole.builder()
                .id(new UserRoleId(usuarioId, roleId))
                .usuario(u)
                .role(r)
                .active(true)
                .build();

        userRoleRepo.save(ur);
    }

    @Override
    public void unassign(Long usuarioId, Long roleId) {
        userRoleRepo.deleteByUsuario_IdAndRole_Id(usuarioId, roleId);
    }

}
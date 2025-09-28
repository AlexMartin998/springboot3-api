package com.ecommerce.api.auth.repository;

import com.ecommerce.api.auth.entity.UserRole;
import com.ecommerce.api.auth.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    boolean existsByUsuario_IdAndRole_Id(Long usuarioId, Long roleId);

    void deleteByUsuario_IdAndRole_Id(Long usuarioId, Long roleId);

}

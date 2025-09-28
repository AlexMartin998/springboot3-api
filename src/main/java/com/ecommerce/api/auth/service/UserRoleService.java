package com.ecommerce.api.auth.service;


public interface UserRoleService {

    void assign(Long usuarioId, Long roleId);

    void unassign(Long usuarioId, Long roleId);

}

package com.ecommerce.api.auth.service.impl;

import com.ecommerce.api.auth.entity.UserRole;
import com.ecommerce.api.users.entity.Usuario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final Usuario user;


    // Roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Construye authorities desde roles activos -> ROLE_{NAME}
        Set<UserRole> roles = user.getUserRoles();
        return roles.stream()
                .filter(UserRole::isActive)
                .map(ur -> "ROLE_" + ur.getRole().getName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();     // with we want to work (email, uuid, username)
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // disabled user validation
        return user.isActive();
    }

}

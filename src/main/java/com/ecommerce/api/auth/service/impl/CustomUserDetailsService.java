package com.ecommerce.api.auth.service.impl;

import com.ecommerce.api.users.entity.Usuario;
import com.ecommerce.api.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // @Autowired all FinalProps in auto by constructor thanks to @RequiredArgsConstructor
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Invalid Token")
        );
        return new UserDetailsImpl(user);
    }

}

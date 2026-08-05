package com.finexa.finexa.security;

import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.auth_users.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepo.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Email not found: " + username));

        return AuthUser.builder()
                .user(user)
                .build();
    }
}
package org.example.tmsserver.security;

import java.util.List;
import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public JpaUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    // Recebe o username (do token ou do login) e retorna todas as informações necessárias para autenticação e autorização.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Ache o usuário no banco e verifica se está ativo e qual role tem
        User u = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        boolean enabled = Boolean.TRUE.equals(u.getEnabled());
        List<GrantedAuthority> auths = List.of(new SimpleGrantedAuthority(u.getRole().getDescription()));

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(auths)
                .disabled(!enabled)
                .accountLocked(false)
                .build();
    }
}
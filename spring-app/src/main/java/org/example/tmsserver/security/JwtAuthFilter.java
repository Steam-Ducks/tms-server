package org.example.tmsserver.security;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService uds;

    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService uds) {
        this.jwtUtil = jwtUtil; this.uds = uds;
    }

    // Verifica se há um token JWT válido e autentica o usuário no contexto
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        // Pega o header Authorization da requisição
        String header = req.getHeader("Authorization");

        // Se tiver "Bearer ", ele remove
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                // Lê o user name do token
                String username = jwtUtil.extractUsername(token);

                // Valida os dados no banco
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails user = uds.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                    // Registra a autenticação no Spring
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception ignored) {
                // Se o token for inválido/expirado segue sem autenticar
            }
        }
        chain.doFilter(req, res);
    }
}

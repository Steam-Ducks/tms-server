package org.example.tmsserver.controller;

import org.example.tmsserver.dto.*;
import org.example.tmsserver.entity.Role;
import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.RoleRepository;
import org.example.tmsserver.repository.UserRepository;
import org.example.tmsserver.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder, AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepo.existsByUsername(req.username()))
            return ResponseEntity.status(409).body("username em uso");

        if (req.email() != null && userRepo.existsByEmail(req.email()))
            return ResponseEntity.status(409).body("email em uso");

        String normalizedPhone = null;
        if (req.phoneNumber() != null && !req.phoneNumber().isBlank()) {
            normalizedPhone = req.phoneNumber().replaceAll("\\D", "");
            // sua coluna é CHAR(11) → garanta 11 dígitos (Brasil sem DDI)
            if (normalizedPhone.length() != 11) {
                return ResponseEntity.badRequest().body("phone_number inválido (esperado 11 dígitos)");
            }
            if (userRepo.existsByPhoneNumber(normalizedPhone)) {
                return ResponseEntity.status(409).body("phone_number em uso");
            }
        }

        Role role = (req.roleId() != null)
                ? roleRepo.findById(req.roleId()).orElse(null)
                : roleRepo.findByDescription("ROLE_USER").orElse(null);

        if (role == null) return ResponseEntity.badRequest().body("role inválido");

        User u = new User();
        u.setUsername(req.username());
        u.setEmail(req.email());
        u.setPassword(encoder.encode(req.password()));
        u.setEnabled(true);
        u.setRole(role);

        // seta telefone somente se informado
        if (normalizedPhone != null) {
            u.setPhoneNumber(normalizedPhone);
        }

        userRepo.save(u);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );
        String token = jwtUtil.generateToken((UserDetails) auth.getPrincipal());
        return ResponseEntity.ok(new AuthResponse(token, "Bearer"));
    }
}

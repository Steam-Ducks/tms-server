package org.example.tmsserver.service;

import org.example.tmsserver.dto.ManagerRequestDTO;
import org.example.tmsserver.dto.ManagerResponseDTO;
import org.example.tmsserver.entity.Role;
import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.RoleRepository;
import org.example.tmsserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final String ADMIN_ROLE_DESCRIPTION = "ROLE_ADMIN";
    private static final String USER_ROLE_DESCRIPTION = "ROLE_USER";

    public ManagerResponseDTO createUser(ManagerRequestDTO requestDTO) {
        // Since all users are managers now, assign ROLE_ADMIN by default
        Role adminRole = roleRepository.findByDescription(ADMIN_ROLE_DESCRIPTION)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Role '" + ADMIN_ROLE_DESCRIPTION + "' não encontrado."));

        // Check for existing username, email, and phone
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username já existe.");
        }

        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já existe.");
        }

        String normalizedPhone = requestDTO.getPhoneNumber().replaceAll("\\D", "");
        if (normalizedPhone.length() != 11) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número de telefone inválido (esperado 11 dígitos)");
        }

        if (userRepository.existsByPhoneNumber(normalizedPhone)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Número de telefone já existe.");
        }

        User newUser = new User();
        newUser.setEmail(requestDTO.getEmail());
        newUser.setPhoneNumber(normalizedPhone);
        newUser.setUsername(requestDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(requestDTO.getPassword())); // Properly encode password
        newUser.setEnabled(true);
        newUser.setRole(adminRole);

        try {
            User savedUser = userRepository.save(newUser);
            return convertToResponseDTO(savedUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao criar usuário.", e);
        }
    }

    public List<ManagerResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ManagerResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        return convertToResponseDTO(user);
    }

    public ManagerResponseDTO updateUser(Integer id, ManagerRequestDTO requestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado para atualização."));

        // Check for existing username, email, and phone (excluding current user)
        if (!existingUser.getUsername().equals(requestDTO.getUsername()) &&
            userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username já existe.");
        }

        if (requestDTO.getRoleId() != null) {
            Role newRole = roleRepository.findById(requestDTO.getRoleId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role com ID " + requestDTO.getRoleId() + " não encontrado."));
            existingUser.setRole(newRole);
        }

        if (!existingUser.getEmail().equals(requestDTO.getEmail()) &&
            userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já existe.");
        }

        String normalizedPhone = requestDTO.getPhoneNumber().replaceAll("\\D", "");
        if (normalizedPhone.length() != 11) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número de telefone inválido (esperado 11 dígitos)");
        }

        if (!existingUser.getPhoneNumber().equals(normalizedPhone) &&
            userRepository.existsByPhoneNumber(normalizedPhone)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Número de telefone já existe.");
        }

        existingUser.setEmail(requestDTO.getEmail());
        existingUser.setPhoneNumber(normalizedPhone);
        existingUser.setUsername(requestDTO.getUsername());
        existingUser.setPassword(passwordEncoder.encode(requestDTO.getPassword())); // Properly encode password

        try {
            User updatedUser = userRepository.save(existingUser);
            return convertToResponseDTO(updatedUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao atualizar usuário.", e);
        }
    }

    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado para exclusão."));

        userRepository.delete(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    private ManagerResponseDTO convertToResponseDTO(User user) {
        String roleDescription = user.getRole() != null ? user.getRole().getDescription() : "N/A";
        return new ManagerResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), roleDescription);
    }
}

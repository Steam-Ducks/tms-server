package org.example.tmsserver.service;

import org.example.tmsserver.dto.ManagerRequestDTO;
import org.example.tmsserver.dto.ManagerResponseDTO;
import org.example.tmsserver.entity.Role;
import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.RoleRepository;
import org.example.tmsserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public ManagerService(UserRepository userRepository, RoleRepository roleRepository ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private static final String MANAGER_ROLE_DESCRIPTION = "ROLE_ADMIN";

    public ManagerResponseDTO createManager(ManagerRequestDTO requestDTO) {
        // 1. Verificar se o Role "ROLE_ADMIN" existe
        Role managerRole = roleRepository.findByDescription(MANAGER_ROLE_DESCRIPTION)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Role '" + MANAGER_ROLE_DESCRIPTION + "' não encontrado."));

        // 2. Criar e salvar o novo User
        User newUser = new User();
        newUser.setEmail(requestDTO.getEmail());
        newUser.setPhoneNumber(requestDTO.getPhoneNumber());
        newUser.setUsername(requestDTO.getUsername());
        // TODO: A senha DEVE ser criptografada (ex: BCrypt) antes de salvar para segurança.
        // newUser.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        newUser.setPassword(requestDTO.getPassword());
        newUser.setRole(managerRole);

        try {
            User savedUser = userRepository.save(newUser);
            return convertToDTO(savedUser);
        } catch (Exception e) {
            // Tratar exceções de violação de unicidade (email, phone_number ou username)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email, número de telefone ou nome de usuário já cadastrado.", e);
        }
    }

    public List<ManagerResponseDTO> getAllManagers() {
        // Buscar todos os usuários e filtrar pelo Role "ROLE_ADMIN"
        List<User> managers = userRepository.findAll().stream()
                .filter(user -> user.getRole() != null && MANAGER_ROLE_DESCRIPTION.equalsIgnoreCase(user.getRole().getDescription()))
                .collect(Collectors.toList());

        return managers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ManagerResponseDTO getManagerById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gestor não encontrado."));

        if (user.getRole() == null || !MANAGER_ROLE_DESCRIPTION.equalsIgnoreCase(user.getRole().getDescription())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID encontrado, mas não corresponde a um gestor.");
        }

        return convertToDTO(user);
    }

    public ManagerResponseDTO updateManager(Integer id, ManagerRequestDTO requestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gestor não encontrado para atualização."));

        if (existingUser.getRole() == null || !MANAGER_ROLE_DESCRIPTION.equalsIgnoreCase(existingUser.getRole().getDescription())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID encontrado, mas não corresponde a um gestor.");
        }

        existingUser.setEmail(requestDTO.getEmail());
        existingUser.setPhoneNumber(requestDTO.getPhoneNumber());
        existingUser.setUsername(requestDTO.getUsername());
        // TODO: A senha DEVE ser criptografada (ex: BCrypt) antes de salvar para segurança.
        // existingUser.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        existingUser.setPassword(requestDTO.getPassword());

        try {
            User updatedUser = userRepository.save(existingUser);
            return convertToDTO(updatedUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email, número de telefone ou nome de usuário já cadastrado.", e);
        }
    }

    public void deleteManager(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gestor não encontrado para exclusão."));

        if (user.getRole() == null || !MANAGER_ROLE_DESCRIPTION.equalsIgnoreCase(user.getRole().getDescription())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID encontrado, mas não corresponde a um gestor.");
        }

        userRepository.delete(user);
    }

    private ManagerResponseDTO convertToDTO(User user) {
        String roleDescription = user.getRole() != null ? user.getRole().getDescription() : "N/A";
        return new ManagerResponseDTO(user.getId(), user.getEmail(), user.getPhoneNumber(), roleDescription);
    }
}

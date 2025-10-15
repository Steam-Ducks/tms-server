// controller/UserController.java
package org.example.tmsserver.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.tmsserver.dto.ManagerRequestDTO;
import org.example.tmsserver.dto.ManagerResponseDTO;
import org.example.tmsserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        return Map.of(
                "username", auth.getName(),
                "roles", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<ManagerResponseDTO> createUser(@Valid @RequestBody ManagerRequestDTO requestDTO) {
        ManagerResponseDTO responseDTO = userService.createUser(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ManagerResponseDTO>> getAllUsers() {
        List<ManagerResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponseDTO> getUserById(@PathVariable Integer id) {
        ManagerResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagerResponseDTO> updateUser(@PathVariable Integer id, @Valid @RequestBody ManagerRequestDTO requestDTO) {
        ManagerResponseDTO updatedUser = userService.updateUser(id, requestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

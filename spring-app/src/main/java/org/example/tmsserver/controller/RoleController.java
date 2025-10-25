package org.example.tmsserver.controller;

import org.example.tmsserver.dto.RoleRequestDTO;
import org.example.tmsserver.dto.RoleResponseDTO;
import org.example.tmsserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(@Valid @RequestBody RoleRequestDTO dto) {
        return new ResponseEntity<>(roleService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> list() {
        return ResponseEntity.ok(roleService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(roleService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody RoleRequestDTO dto) {
        return ResponseEntity.ok(roleService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

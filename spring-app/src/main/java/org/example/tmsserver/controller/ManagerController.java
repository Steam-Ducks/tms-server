package org.example.tmsserver.controller;

import org.example.tmsserver.dto.ManagerRequestDTO;
import org.example.tmsserver.dto.ManagerResponseDTO;
import org.example.tmsserver.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping
    public ResponseEntity<ManagerResponseDTO> createManager(@Valid @RequestBody ManagerRequestDTO requestDTO) {
        ManagerResponseDTO responseDTO = managerService.createManager(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ManagerResponseDTO>> getAllManagers() {
        List<ManagerResponseDTO> managers = managerService.getAllManagers();
        return ResponseEntity.ok(managers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponseDTO> getManagerById(@PathVariable Integer id) {
        ManagerResponseDTO manager = managerService.getManagerById(id);
        return ResponseEntity.ok(manager);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagerResponseDTO> updateManager(@PathVariable Integer id, @Valid @RequestBody ManagerRequestDTO requestDTO) {
        ManagerResponseDTO updatedManager = managerService.updateManager(id, requestDTO);
        return ResponseEntity.ok(updatedManager);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable Integer id) {
        managerService.deleteManager(id);
        return ResponseEntity.noContent().build();
    }
}

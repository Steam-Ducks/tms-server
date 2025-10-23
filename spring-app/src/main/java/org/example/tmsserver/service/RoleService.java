package org.example.tmsserver.service;

import org.example.tmsserver.dto.RoleRequestDTO;
import org.example.tmsserver.dto.RoleResponseDTO;
import org.example.tmsserver.entity.Region;
import org.example.tmsserver.entity.Role;
import org.example.tmsserver.repository.RegionRepository;
import org.example.tmsserver.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RegionRepository regionRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, RegionRepository regionRepository) {
        this.roleRepository = roleRepository;
        this.regionRepository = regionRepository;
    }

    public RoleResponseDTO create(RoleRequestDTO dto) {
        if (roleRepository.existsByDescription(dto.getDescription())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Descrição já existe.");
        }
        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Região inválida."));

        Role role = new Role(dto.getDescription(), region);
        Role saved = roleRepository.save(role);
        return toResponse(saved);
    }

    public List<RoleResponseDTO> list() {
        return roleRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public RoleResponseDTO get(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role não encontrada."));
        return toResponse(role);
    }

    public RoleResponseDTO update(Integer id, RoleRequestDTO dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role não encontrada."));

        if (!role.getDescription().equals(dto.getDescription())
                && roleRepository.existsByDescription(dto.getDescription())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Descrição já existe.");
        }

        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Região inválida."));

        role.setDescription(dto.getDescription());
        role.setRegion(region);

        Role saved = roleRepository.save(role);
        return toResponse(saved);
    }

    public void delete(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role não encontrada."));
        roleRepository.delete(role);
    }

    private RoleResponseDTO toResponse(Role r) {
        return new RoleResponseDTO(
                r.getId(),
                r.getDescription(),
                r.getRegion() != null ? r.getRegion().getIdRegion() : null,
                r.getRegion() != null ? r.getRegion().getName() : null
        );
    }
}

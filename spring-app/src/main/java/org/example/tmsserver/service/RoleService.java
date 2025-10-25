package org.example.tmsserver.service;

import org.example.tmsserver.dto.RegionResponseDTO;
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
import java.util.stream.Collectors;

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

        List<Region> regions = regionRepository.findAllById(dto.getRegionIds());
        if (regions.size() != dto.getRegionIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uma ou mais regiões são inválidas.");
        }

        Role role = new Role(dto.getDescription(), regions);
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

        List<Region> regions = regionRepository.findAllById(dto.getRegionIds());
        if (regions.size() != dto.getRegionIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uma ou mais regiões são inválidas.");
        }

        role.setDescription(dto.getDescription());
        role.setRegions(regions);

        Role saved = roleRepository.save(role);
        return toResponse(saved);
    }

    public void delete(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role não encontrada."));
        roleRepository.delete(role);
    }

    private RoleResponseDTO toResponse(Role r) {
        List<RegionResponseDTO> regionDTOs = r.getRegions() != null ?
            r.getRegions().stream()
                .map(region -> new RegionResponseDTO(region.getIdRegion(), region.getName()))
                .collect(Collectors.toList()) :
            List.of();

        return new RoleResponseDTO(
                r.getId(),
                r.getDescription(),
                regionDTOs
        );
    }
}

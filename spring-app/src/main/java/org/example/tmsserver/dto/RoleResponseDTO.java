package org.example.tmsserver.dto;

import java.util.List;

public class RoleResponseDTO {
    private Integer id;
    private String description;
    private List<RegionResponseDTO> regions;

    public RoleResponseDTO(Integer id, String description, List<RegionResponseDTO> regions) {
        this.id = id;
        this.description = description;
        this.regions = regions;
    }

    public Integer getId() { return id; }
    public String getDescription() { return description; }
    public List<RegionResponseDTO> getRegions() { return regions; }

    public void setId(Integer id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setRegions(List<RegionResponseDTO> regions) { this.regions = regions; }
}

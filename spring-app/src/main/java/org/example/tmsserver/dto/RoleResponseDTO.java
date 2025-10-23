package org.example.tmsserver.dto;

public class RoleResponseDTO {
    private Integer id;
    private String description;
    private Integer regionId;
    private String regionName;

    public RoleResponseDTO(Integer id, String description, Integer regionId, String regionName) {
        this.id = id;
        this.description = description;
        this.regionId = regionId;
        this.regionName = regionName;
    }

    public Integer getId() { return id; }
    public String getDescription() { return description; }
    public Integer getRegionId() { return regionId; }
    public String getRegionName() { return regionName; }

    public void setId(Integer id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setRegionId(Integer regionId) { this.regionId = regionId; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
}

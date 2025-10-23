package org.example.tmsserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RoleRequestDTO {
    @NotBlank
    private String description;

    @NotNull
    private Integer regionId;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getRegionId() { return regionId; }
    public void setRegionId(Integer regionId) { this.regionId = regionId; }
}

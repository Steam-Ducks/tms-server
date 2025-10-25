package org.example.tmsserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class RoleRequestDTO {
    @NotBlank
    private String description;

    @NotNull
    private List<@NotNull Integer> regionIds;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Integer> getRegionIds() { return regionIds; }
    public void setRegionIds(List<Integer> regionIds) { this.regionIds = regionIds; }
}

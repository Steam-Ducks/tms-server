// RegionIndicatorLevelDTO.java
package org.example.tmsserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionIndicatorLevelDTO {
    private String regionName;
    private String indicatorName;
    private Integer value;
    private Integer level;
    private String change;

    public RegionIndicatorLevelDTO() {}

    public RegionIndicatorLevelDTO(String regionName, String indicatorName, Integer value, Integer level, String change) {
        this.regionName = regionName;
        this.indicatorName = indicatorName;
        this.value = value;
        this.level = level;
        this.change = change;
    }

    // Getters e Setters
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }

    public String getIndicatorName() { return indicatorName; }
    public void setIndicatorName(String indicatorName) { this.indicatorName = indicatorName; }

    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public String getChange() { return change; }
    public void setChange(String change) { this.change = change; }
}
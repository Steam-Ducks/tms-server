package org.example.tmsserver.dto;

public class WorstStreetByRegionDTO {

    private String regionId;
    private String regionName;
    private Integer regionLevel;

    private String streetId;
    private String streetAddress;
    private Double avgSpeed;
    private Integer speedLimit;
    private Double severity;

    public WorstStreetByRegionDTO() {}

    public WorstStreetByRegionDTO(
            String regionId,
            String regionName,
            Integer regionLevel,
            String streetId,
            String streetAddress,
            Double avgSpeed,
            Integer speedLimit,
            Double severity
    ) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.regionLevel = regionLevel;
        this.streetId = streetId;
        this.streetAddress = streetAddress;
        this.avgSpeed = avgSpeed;
        this.speedLimit = speedLimit;
        this.severity = severity;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Integer getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(Integer regionLevel) {
        this.regionLevel = regionLevel;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public Integer getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Integer speedLimit) {
        this.speedLimit = speedLimit;
    }

    public Double getSeverity() {
        return severity;
    }

    public void setSeverity(Double severity) {
        this.severity = severity;
    }
    
}


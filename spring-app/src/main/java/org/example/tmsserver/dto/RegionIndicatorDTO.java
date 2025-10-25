package org.example.tmsserver.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionIndicatorDTO {

    private String day;
    private Integer hour;
    private String regionName;
    private String indicatorName;
    private Double averageValue;

    public RegionIndicatorDTO(String day, Integer hour, String regionName, String indicatorName, Double averageValue) {
        this.day = day;
        this.hour = hour;
        this.regionName = regionName;
        this.indicatorName = indicatorName;
        this.averageValue = averageValue;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public Double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(Double averageValue) {
        this.averageValue = averageValue;
    }
}
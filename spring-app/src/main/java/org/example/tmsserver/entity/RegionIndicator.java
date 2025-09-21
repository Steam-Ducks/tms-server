package org.example.tmsserver.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@IdClass(RegionIndicatorId.class)
@Table(name = "region_indicator")
public class RegionIndicator {

    @Id
    @Column(name = "id_region")
    private Long idRegion;

    @Id
    @Column(name = "id_indicator")
    private Long idIndicator;

    @Column(name = "value")
    private Integer value;

    @Column(name = "time")
    private OffsetDateTime time;

    @Column(name = "change")
    private String change;

    @ManyToOne
    @JoinColumn(name = "id_region", insertable = false, updatable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "id_indicator", insertable = false, updatable = false)
    private Indicator indicator;

    public RegionIndicator() {}

    public RegionIndicator(Long idRegion, Long idIndicator, Integer value, OffsetDateTime time, String change, Region region, Indicator indicator) {
        this.idRegion = idRegion;
        this.idIndicator = idIndicator;
        this.value = value;
        this.time = time;
        this.change = change;
        this.region = region;
        this.indicator = indicator;
    }

    public Long getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(Long idRegion) {
        this.idRegion = idRegion;
    }

    public Long getIdIndicator() {
        return idIndicator;
    }

    public void setIdIndicator(Long idIndicator) {
        this.idIndicator = idIndicator;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public void setTime(OffsetDateTime time) {
        this.time = time;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }
}

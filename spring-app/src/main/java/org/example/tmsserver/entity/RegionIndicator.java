package org.example.tmsserver.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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
    private Double value;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "change")
    private Double change;

    @ManyToOne
    @JoinColumn(name = "id_region", insertable = false, updatable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "id_indicator", insertable = false, updatable = false)
    private Indicator indicator;

    public RegionIndicator() {}

    public RegionIndicator(Long idRegion, Long idIndicator, Double value, LocalDateTime timestamp, Double change, Region region, Indicator indicator) {
        this.idRegion = idRegion;
        this.idIndicator = idIndicator;
        this.value = value;
        this.timestamp = timestamp;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
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

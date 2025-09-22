package org.example.tmsserver.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "region_indicator")
public class RegionIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_region", nullable = false)
    private Integer idRegion;

    @Column(name = "id_indicator", nullable = false)
    private Integer idIndicator;

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

    public RegionIndicator(Integer idRegion, Integer idIndicator, Integer value, OffsetDateTime time, String change, Region region, Indicator indicator) {
        this.idRegion = idRegion;
        this.idIndicator = idIndicator;
        this.value = value;
        this.time = time;
        this.change = change;
        this.region = region;
        this.indicator = indicator;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(Integer idRegion) {
        this.idRegion = idRegion;
    }

    public Integer getIdIndicator() {
        return idIndicator;
    }

    public void setIdIndicator(Integer idIndicator) {
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

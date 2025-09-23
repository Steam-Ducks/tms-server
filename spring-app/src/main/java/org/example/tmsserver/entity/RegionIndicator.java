package org.example.tmsserver.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "region_indicator")
public class RegionIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_indicator", nullable = false)
    private Indicator indicator;

    @Column(name = "value")
    private Integer value;

    @Column(name = "time")
    private OffsetDateTime time;

    @Column(name = "change")
    private String change;

    public RegionIndicator() {}

    public RegionIndicator(Region region, Indicator indicator, Integer value, OffsetDateTime time, String change) {
        this.region = region;
        this.indicator = indicator;
        this.value = value;
        this.time = time;
        this.change = change;
    }

    public Integer getId() {
        return id;
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

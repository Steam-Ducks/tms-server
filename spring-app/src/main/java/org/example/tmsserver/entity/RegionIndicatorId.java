package org.example.tmsserver.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RegionIndicatorId implements Serializable {

    @Column(name = "id_region")
    private Integer idRegion;

    @Column(name = "id_indicator")
    private Integer idIndicator;

    public RegionIndicatorId() {}

    public RegionIndicatorId(Integer idRegion, Integer idIndicator) {
        this.idRegion = idRegion;
        this.idIndicator = idIndicator;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionIndicatorId)) return false;
        RegionIndicatorId that = (RegionIndicatorId) o;
        return Objects.equals(idRegion, that.idRegion) &&
                Objects.equals(idIndicator, that.idIndicator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRegion, idIndicator);
    }
}

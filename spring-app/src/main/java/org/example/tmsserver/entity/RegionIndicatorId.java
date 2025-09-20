package org.example.tmsserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RegionIndicatorId implements Serializable {

    @Id
    @Column(name = "id_region")
    private Long idRegion;

    @Id
    @Column(name = "id_indicator")
    private Long idIndicator;

    public RegionIndicatorId() {}

    public RegionIndicatorId(Long idRegion, Long idIndicator) {
        this.idRegion = idRegion;
        this.idIndicator = idIndicator;
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

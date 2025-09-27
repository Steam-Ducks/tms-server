package org.example.tmsserver.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Geometry;

import java.util.List;

@Entity
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_region")
    private Integer idRegion;

    @Column(name = "geolocation", columnDefinition = "SDO_GEOMETRY")
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Geometry geolocation;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "region")
    private List<Camera> cameras;

    @OneToMany(mappedBy = "region")
    private List<Level> levels;

    @OneToMany(mappedBy = "region")
    private List<Role> roles;

    @OneToMany(mappedBy = "region")
    private List<RegionIndicator> indicators;

    public Region() {
    }

    public Region(Integer idRegion, Geometry geolocation, String name,
            List<Camera> cameras, List<Level> levels,
            List<Role> roles, List<RegionIndicator> indicators) {
        this.idRegion = idRegion;
        this.geolocation = geolocation;
        this.name = name;
        this.cameras = cameras;
        this.levels = levels;
        this.roles = roles;
        this.indicators = indicators;
    }

    public Integer getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(Integer idRegion) {
        this.idRegion = idRegion;
    }

    public Geometry getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geometry geolocation) {
        this.geolocation = geolocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<RegionIndicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<RegionIndicator> indicators) {
        this.indicators = indicators;
    }

}

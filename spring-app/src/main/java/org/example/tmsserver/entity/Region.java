package org.example.tmsserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "REGION")
public class Region {

    @Id
    @Column(name = "ID_REGION", nullable = false)
    private Long idRegion;

    @Column(name = "GEOLOCATION", length = 255)
    private String geolocation;

    @Column(name = "NAME", nullable = false, length = 30)
    private String name;

    // Getters e Setters
    public Long getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(Long idRegion) {
        this.idRegion = idRegion;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

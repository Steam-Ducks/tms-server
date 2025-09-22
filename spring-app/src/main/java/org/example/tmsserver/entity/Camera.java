package org.example.tmsserver.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "camera")
public class Camera {

    @Id
    @Column(name = "id_camera")
    private String idCamera;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "speed_limit")
    private Integer speedLimit;

    @ManyToOne
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "camera")
    private List<SpeedRecord> speedRecords;

    public Camera() {}

    public Camera(String idCamera, BigDecimal latitude, BigDecimal longitude, String bairro, Integer speedLimit, Region region, List<SpeedRecord> speedRecords) {
        this.idCamera = idCamera;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bairro = bairro;
        this.speedLimit = speedLimit;
        this.region = region;
        this.speedRecords = speedRecords;
    }

    // Getters e Setters
    public String getIdCamera() {
        return idCamera;
    }

    public void setIdCamera(String idCamera) {
        this.idCamera = idCamera;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Integer speedLimit) {
        this.speedLimit = speedLimit;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public List<SpeedRecord> getSpeedRecords() {
        return speedRecords;
    }

    public void setSpeedRecords(List<SpeedRecord> speedRecords) {
        this.speedRecords = speedRecords;
    }
}

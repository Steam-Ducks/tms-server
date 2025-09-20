package org.example.tmsserver.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "CAMERA")
public class Camera {

    @Id
    @Column(name = "ID_CAMERA", length = 50)
    private String idCamera;

    // Relacionamento com Region
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_REGION")
    private Region region;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "BAIRRO", length = 30)
    private String bairro;

    @Column(name = "SPEED_LIMIT", precision = 5, scale = 2)
    private BigDecimal speedLimit;

    public Camera() {}

    public Camera(String idCamera, Region region, Double latitude, Double longitude, String bairro, BigDecimal speedLimit) {
        this.idCamera = idCamera;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bairro = bairro;
        this.speedLimit = speedLimit;
    }

    public String getIdCamera() {
        return idCamera;
    }

    public void setIdCamera(String idCamera) {
        this.idCamera = idCamera;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public BigDecimal getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(BigDecimal speedLimit) {
        this.speedLimit = speedLimit;
    }
}
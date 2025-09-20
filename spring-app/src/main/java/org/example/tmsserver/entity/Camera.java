package org.example.tmsserver.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "camera")
public class Camera {

    @Id
    @Column(name = "id_camera")
    private String idCamera;

    private String latitude;
    private String longitude;
    private String bairro;

    @Column(name = "speed_limit")
    private Integer speedLimit;

    @ManyToOne
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "camera")
    private List<SpeedRecord> speedRecords;

    // Construtor vazio
    public Camera() {}

    // Construtor completo
    public Camera(String idCamera, String latitude, String longitude, String bairro, Integer speedLimit, Region region, List<SpeedRecord> speedRecords) {
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
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

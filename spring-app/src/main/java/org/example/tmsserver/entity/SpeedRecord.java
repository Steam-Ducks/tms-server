package org.example.tmsserver.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "speed_record")
public class SpeedRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_speed_record")
    private Long id;

    @Column(name = "speed")
    private BigDecimal speed;

    @Column(name = "time")
    private OffsetDateTime time;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_camera")
    private Camera camera;

    public SpeedRecord() {
    }

    public SpeedRecord(Long id, BigDecimal speed, java.time.OffsetDateTime time, String vehicleType, Camera camera) {
        this.id = id;
        this.speed = speed;
        this.time = time;
        this.vehicleType = vehicleType;
        this.camera = camera;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public void setTime(OffsetDateTime time) {
        this.time = time;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
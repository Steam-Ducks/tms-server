package org.example.tmsserver.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "SPEED_RECORD")
public class SpeedRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "speed_record_seq")
    @SequenceGenerator(name = "speed_record_seq", sequenceName = "SPEED_RECORD_SEQ", allocationSize = 1)
    @Column(name = "ID_SPEED_RECORD")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CAMERA", nullable = false)
    private Camera camera;

    @Column(name = "SPEED", precision = 38, scale = 2)
    private BigDecimal speed;

    @Column(name = "TIME")
    private OffsetDateTime time;

    @Column(name = "VEHICLE_TYPE", length = 255)
    private String vehicleType;

    public SpeedRecord() {}

    public SpeedRecord(Camera camera, BigDecimal speed, OffsetDateTime time, String vehicleType) {
        this.camera = camera;
        this.speed = speed;
        this.time = time;
        this.vehicleType = vehicleType;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
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
}
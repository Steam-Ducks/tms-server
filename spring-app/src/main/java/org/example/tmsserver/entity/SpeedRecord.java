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

    @Column(name = "ID_CAMERA")
    private Long cameraId;

    @Column(name = "SPEED")
    private BigDecimal speed;

    @Column(name = "TIME")
    private OffsetDateTime time;

    @Column(name = "VEHICLE_TYPE")
    private String vehicleType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCameraId() {
        return cameraId;
    }

    public void setCameraId(Long cameraId) {
        this.cameraId = cameraId;
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
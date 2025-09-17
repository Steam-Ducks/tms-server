package org.example.tmsserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class SpeedRecord {
    @Id
    private Long idSpeedRecord;

    private Integer speed;
    private LocalDateTime timestamp;
    private String vehicleType;

    @ManyToOne
    @JoinColumn(name = "id_camera")
    private Camera camera;
}

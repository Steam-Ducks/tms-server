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
}
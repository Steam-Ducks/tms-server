package org.example.tmsserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Level {
    @Id
    private Long idLevel;

    private Double value;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "id_region")
    private Region region;
}

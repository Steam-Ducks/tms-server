package org.example.tmsserver.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@IdClass(RegionIndicatorId.class)
public class RegionIndicator {

    @Id
    private Long idRegion;

    @Id
    private Long idIndicator;

    private Double value;
    private LocalDateTime timestamp;
    private Double change;

    @ManyToOne
    @JoinColumn(name = "id_region", insertable = false, updatable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "id_indicator", insertable = false, updatable = false)
    private Indicator indicator;
}

package org.example.tmsserver.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class RegionIndicatorId implements Serializable {
    private Long idRegion;
    private Long idIndicator;
}

package org.example.tmsserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Indicator {
    @Id
    private Long idIndicator;

    private String name;
    private String description;

    @OneToMany(mappedBy = "indicator")
    private List<RegionIndicator> regionIndicators;

    @OneToMany(mappedBy = "indicator")
    private List<Protocol> protocols;
}

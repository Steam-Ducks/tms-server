package org.example.tmsserver.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "indicator")
public class Indicator {

    @Id
    @Column(name = "id_indicator")
    private Long idIndicator;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "indicator")
    private List<RegionIndicator> regionIndicators;

    @OneToMany(mappedBy = "indicator")
    private List<Protocol> protocols;

    public Indicator() {}

    public Indicator(Long idIndicator, String name, String description, List<RegionIndicator> regionIndicators, List<Protocol> protocols) {
        this.idIndicator = idIndicator;
        this.name = name;
        this.description = description;
        this.regionIndicators = regionIndicators;
        this.protocols = protocols;
    }

    public Long getIdIndicator() {
        return idIndicator;
    }

    public void setIdIndicator(Long idIndicator) {
        this.idIndicator = idIndicator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RegionIndicator> getRegionIndicators() {
        return regionIndicators;
    }

    public void setRegionIndicators(List<RegionIndicator> regionIndicators) {
        this.regionIndicators = regionIndicators;
    }

    public List<Protocol> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<Protocol> protocols) {
        this.protocols = protocols;
    }
}

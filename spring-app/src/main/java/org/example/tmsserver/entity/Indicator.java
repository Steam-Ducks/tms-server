package org.example.tmsserver.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "indicator")
public class Indicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_indicator")
    private Long idIndicator;


    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "indicator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegionIndicator> regionIndicators = new ArrayList<>();

    @OneToMany(mappedBy = "indicator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Protocol> protocols = new ArrayList<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Indicator)) return false;
        Indicator that = (Indicator) o;
        return Objects.equals(idIndicator, that.idIndicator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIndicator);
    }
}

package org.example.tmsserver.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "REGION_INDICATOR")
public class RegionIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "region_indicator_seq")
    @SequenceGenerator(name = "region_indicator_seq", sequenceName = "REGION_INDICATOR_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_REGION", nullable = false)
    private Long idRegion;

    @Column(name = "ID_INDICATOR", nullable = false)
    private Long idIndicator; // FK para tabela de indicadores (ex: 1 = média velocidade)

    @Column(name = "VALUE", precision = 38, scale = 6)
    private BigDecimal value;

    @Column(name = "TIME")
    private OffsetDateTime time;

    @Column(name = "CHANGE", length = 10)
    private String change;

    public RegionIndicator() {}

    public RegionIndicator(Long idRegion, Long idIndicator, BigDecimal value, OffsetDateTime time, String change) {
        this.idRegion = idRegion;
        this.idIndicator = idIndicator;
        this.value = value;
        this.time = time;
        this.change = change;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public Long getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(Long idRegion) {
        this.idRegion = idRegion;
    }

    public Long getIdIndicator() {
        return idIndicator;
    }

    public void setIdIndicator(Long idIndicator) {
        this.idIndicator = idIndicator;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public void setTime(OffsetDateTime time) {
        this.time = time;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }
}

package org.example.tmsserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "protocol")
public class Protocol {

    @Id
    @Column(name = "id_protocol")
    private Long idProtocol;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_indicator", nullable = false)
    private Indicator indicator;

    public Protocol() {}

    public Protocol(Long idProtocol, String description, Indicator indicator) {
        this.idProtocol = idProtocol;
        this.description = description;
        this.indicator = indicator;
    }

    public Long getIdProtocol() {
        return idProtocol;
    }

    public void setIdProtocol(Long idProtocol) {
        this.idProtocol = idProtocol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }
}

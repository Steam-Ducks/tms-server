package org.example.tmsserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Protocol {
    @Id
    private Long idProtocol;

    private String description;

    @ManyToOne
    @JoinColumn(name = "id_indicator")
    private Indicator indicator;
}

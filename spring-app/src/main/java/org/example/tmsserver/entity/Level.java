package org.example.tmsserver.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "app_level")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_level")
    private Integer idLevel;


    @Column(name = "value")
    private Integer value;

    @Column(name = "time")
    private OffsetDateTime time;

    @ManyToOne
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;

    public Level() {}

    public Level(Integer idLevel, Integer value, OffsetDateTime time, Region region) {
        this.idLevel = idLevel;
        this.value = value;
        this.time = time;
        this.region = region;
    }

    public Integer getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(Integer idLevel) {
        this.idLevel = idLevel;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public void setTime(OffsetDateTime time) {
        this.time = time;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}

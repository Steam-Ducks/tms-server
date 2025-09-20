package org.example.tmsserver.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_level")
public class Level {

    @Id
    @Column(name = "id_level")
    private Long idLevel;

    @Column(name = "value")
    private float value;

    @Column(name = "time")
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;

    public Level() {}

    public Level(Long idLevel, float value, LocalDateTime time, Region region) {
        this.idLevel = idLevel;
        this.value = value;
        this.time = time;
        this.region = region;
    }

    public Long getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(Long idLevel) {
        this.idLevel = idLevel;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}

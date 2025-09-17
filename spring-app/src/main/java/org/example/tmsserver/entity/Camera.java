package org.example.tmsserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;

import java.util.List;

@Entity
public class Camera {
    @Id
    private String idCamera;

    private String latitude;
    private String longitude;
    private String bairro;
    private Integer speedLimit;

    @ManyToOne
    @JoinColumn(name = "id_region")
    private Region region;

    // uma camera pode estar vinculada a vários Speed Record,
    // porém uma Speed Record está vinculada somente a uma camera
    @OneToMany(mappedBy = "camera")
    private List<SpeedRecord> speedRecords;
}

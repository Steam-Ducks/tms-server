package org.example.tmsserver.entity;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    private Long idUser;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;
}

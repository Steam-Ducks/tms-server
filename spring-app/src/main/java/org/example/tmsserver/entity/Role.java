package org.example.tmsserver.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Integer idRole;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_region",
        joinColumns = @JoinColumn(name = "id_role"),
        inverseJoinColumns = @JoinColumn(name = "id_region")
    )
    private List<Region> regions;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<User> users;

    public Role() {
    }

    public Role(String description, List<Region> regions) {
        this.description = description;
        this.regions = regions;
    }

    public Integer getId() {
        return idRole;
    }

    public void setId(Integer id) {
        this.idRole = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
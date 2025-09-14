@Entity
public class Role {
    @Id
    private Long idRole;

    private String description;

    @ManyToOne
    @JoinColumn(name = "id_region")
    private Region region;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}

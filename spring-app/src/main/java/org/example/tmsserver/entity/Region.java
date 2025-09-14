@Entity
public class Region {
    @Id
    private Long idRegion;

    private String geolocation;
    private String name;

    @OneToMany(mappedBy = "region")
    private List<Camera> cameras;

    @OneToMany(mappedBy = "region")
    private List<Level> levels;

    @OneToMany(mappedBy = "region")
    private List<Role> roles;

    @OneToMany(mappedBy = "region")
    private List<RegionIndicator> indicators;
}

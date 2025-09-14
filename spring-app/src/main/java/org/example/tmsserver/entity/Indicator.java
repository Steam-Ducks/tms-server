@Entity
public class Indicator {
    @Id
    private Long idIndicator;

    private String name;
    private String description;

    @OneToMany(mappedBy = "indicator")
    private List<RegionIndicator> regionIndicators;

    @OneToMany(mappedBy = "indicator")
    private List<Protocol> protocols;
}

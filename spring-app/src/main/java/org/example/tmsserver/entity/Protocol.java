@Entity
public class Protocol {
    @Id
    private Long idProtocol;

    private String description;

    @ManyToOne
    @JoinColumn(name = "id_indicator")
    private Indicator indicator;
}

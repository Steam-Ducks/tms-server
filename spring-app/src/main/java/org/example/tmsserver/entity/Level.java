@Entity
public class Level {
    @Id
    private Long idLevel;

    private Double value;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "id_region")
    private Region region;
}

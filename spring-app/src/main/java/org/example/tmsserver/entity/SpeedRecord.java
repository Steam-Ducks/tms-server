@Entity
public class SpeedRecord {
    @Id
    private Long idSpeedRecord;

    private Integer speed;
    private LocalDateTime timestamp;
    private String vehicleType;

    @ManyToOne
    @JoinColumn(name = "id_camera")
    private Camera camera;
}

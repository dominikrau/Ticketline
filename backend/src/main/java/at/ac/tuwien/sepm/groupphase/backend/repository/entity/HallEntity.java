package at.ac.tuwien.sepm.groupphase.backend.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;


@Data
@Table(name = "hall")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class HallEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Instant createdAt;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "width")
    private Integer width;

    @Column(nullable = false, name = "height")
    private Integer height;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "venue_id")
    private VenueEntity venue;

    @OneToMany(mappedBy = "hall")
    private List<SeatingChartEntity> seatingCharts;
}

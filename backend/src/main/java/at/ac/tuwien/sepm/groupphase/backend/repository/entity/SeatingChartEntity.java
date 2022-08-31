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
@Table(name = "seating_chart")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class SeatingChartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Instant createdAt;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "hall_id")
    private HallEntity hall;

    @OneToMany(mappedBy = "seatingChart", cascade = CascadeType.ALL)
    private List<SectorEntity> sectors;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "stage_id")
    private StageEntity stage;

    @OneToMany(mappedBy ="seatingChart")
    private List<ShowEntity> shows;
}

package at.ac.tuwien.sepm.groupphase.backend.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Table(name = "show")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class ShowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showId;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "start_time")
    private LocalDateTime startTime;

    @Column(nullable = false, name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "venue_id", referencedColumnName = "venueId")
    private VenueEntity venue;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @OneToMany(mappedBy = "show")
    private List<PricingEntity> pricings;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "seating_chart_id")
    private SeatingChartEntity seatingChart;

    @OneToMany(mappedBy = "show")
    private List<OrderEntity> order;
}
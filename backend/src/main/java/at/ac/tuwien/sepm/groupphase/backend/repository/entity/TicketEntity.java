package at.ac.tuwien.sepm.groupphase.backend.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Data
@Table(name = "ticket")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Instant createdAt;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String status;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "sector_id")
    private SectorEntity sector;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = true, name = "seat_id")
    private SeatEntity seat;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "user_id")
    private ApplicationUserEntity user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "show_id")
    private ShowEntity show;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = true, name = "order_id")
    private OrderEntity ticketOrder;
}

package at.ac.tuwien.sepm.groupphase.backend.repository.entity;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Table(name = "orders")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant orderDate;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "ticketOrder", cascade = CascadeType.MERGE)
    private List<TicketEntity> tickets;

    @ManyToOne()
    @JoinColumn(nullable = false, name = "show_id")
    private ShowEntity show;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "user_id")
    private ApplicationUserEntity user;

}

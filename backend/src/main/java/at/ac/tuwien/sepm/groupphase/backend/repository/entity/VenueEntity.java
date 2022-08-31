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
@Table(name = "venue")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class VenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venueId;

    @Column(nullable = false, name = "name")
    private String name;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Instant createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "address_id")
    private AddressEntity address;

    @OneToMany(mappedBy = "venue")
    private List<HallEntity> halls;

}
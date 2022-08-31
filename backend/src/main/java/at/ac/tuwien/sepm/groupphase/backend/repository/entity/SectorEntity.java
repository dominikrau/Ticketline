package at.ac.tuwien.sepm.groupphase.backend.repository.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "sector")
@Inheritance
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class SectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "seatingChart_id")
    private SeatingChartEntity seatingChart;

    @OneToMany(mappedBy = "sector")
    private List<PricingEntity> pricings;

    @OneToMany(mappedBy = "sector")
    private List<TicketEntity> tickets;


}

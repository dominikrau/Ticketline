package at.ac.tuwien.sepm.groupphase.backend.repository.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@ToString
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SittingSectorEntity extends SectorEntity {

    @OneToMany(mappedBy = "sector")
    private List<SeatEntity> seats;
}

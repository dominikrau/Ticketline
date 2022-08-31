package at.ac.tuwien.sepm.groupphase.backend.repository.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@ToString
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StandingSectorEntity extends SectorEntity{

    @Column
    private Integer x;

    @Column
    private Integer y;

    @Column
    private Integer width;

    @Column
    private Integer height;

    @Column
    private Integer capacity;
}

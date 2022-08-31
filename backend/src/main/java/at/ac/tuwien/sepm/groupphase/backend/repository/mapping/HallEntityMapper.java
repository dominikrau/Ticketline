package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Hall;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.HallEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = VenueEntityMapper.class)
public interface HallEntityMapper {

    /**
     * Maps the given Hall Entity to a Domain representation of the Hall
     *
     * @param hallEntity the Hall Entity to be mapped
     * @return the mapped Hall
     */
    @Named("hall")
    Hall toDomain(HallEntity hallEntity);

    /**
     * Maps the given Halls Entities in a List to a List of Domain representations of the Halls
     *
     * @param hallEntities the List of Hall Entities to be mapped
     * @return the mapped Hall List
     */
    @IterableMapping(qualifiedByName = "hall")
    List<Hall> toDomain(List<HallEntity> hallEntities);


    /**
     * Maps the given Hall to a Repository Entity representation of the Hall
     *
     * @param hall the Hall to be mapped
     * @return the mapped Hall Entity
     */
    HallEntity toEntity(Hall hall);
}

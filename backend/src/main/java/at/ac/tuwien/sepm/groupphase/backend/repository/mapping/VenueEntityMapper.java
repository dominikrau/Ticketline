package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Venue;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.VenueEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = AddressEntityMapper.class)
public interface VenueEntityMapper {

    /**
     * Maps the given Venue Entity to a Domain representation of the Venue
     *
     * @param venueEntity the Venue Entity to be mapped
     * @return the mapped Venue
     */
    @Named("venue")
    Venue toDomain(VenueEntity venueEntity);

    /**
     * Maps the given Venues Entities in a List to a List of Domain representations of the Venues
     *
     * @param venues the List of Venue Entities to be mapped
     * @return the mapped Venue List
     */
    @IterableMapping(qualifiedByName = "venue")
    List<Venue> toDomain(List<VenueEntity> venues);


    /**
     * Maps the given Venue to a Repository Entity representation of the Venue
     *
     * @param venue the Venue to be mapped
     * @return the mapped Venue Entity
     */
    VenueEntity toEntity(Venue venue);

}

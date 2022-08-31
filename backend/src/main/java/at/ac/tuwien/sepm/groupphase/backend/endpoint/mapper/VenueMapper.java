package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Venue;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = AddressMapper.class)
public interface VenueMapper {
    /**
     * Maps the given Venue DTO to a Domain representation of the Venue
     *
     * @param venueDto the Venue DTO to be mapped
     * @return the mapped Venue
     */
    Venue venueDtoToVenue(VenueDto venueDto);

    /**
     * Maps the given Venue to a Data Transfer Object representation of the Venue
     *
     * @param venue the Venue to be mapped
     * @return the mapped Venue DTO
     */
    @Named("venue")
    VenueDto venueToVenueDto(Venue venue);

    /**
     * Maps the given Venues in a List to a List of Data Transfer Object representations of the Venues
     *
     * @param venues the List of Venues to be mapped
     * @return the mapped Venue DTO List
     */
    @IterableMapping(qualifiedByName = "venue")
    List<VenueDto> venueToVenueDto(List<Venue> venues);
}

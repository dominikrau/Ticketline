package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.domain.model.Hall;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.HallDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = VenueMapper.class)
public interface HallMapper {

    /**
     * Maps the given Hall Dto to a Domain representation of the Hall
     *
     * @param hallDto the Hall DTO to be mapped
     * @return the mapped Hall
     */
    Hall hallDtoToHall(HallDto hallDto);


    /**
     * Maps the given Hall to a Data Transfer Object representation of the Hall
     *
     * @param hall the Hall to be mapped
     * @return the mapped Hall DTO
     */
    @Named("hall")
    HallDto hallToHallDto(Hall hall);

    /**
     * Maps the given Halls in a List to a List of Data Transfer Object representations of the Halls
     *
     * @param halls the List of Halls to be mapped
     * @return the mapped Hall DTO List
     */
    @IterableMapping(qualifiedByName = "hall")
    List<HallDto> hallToHallDto(List<Hall> halls);
}

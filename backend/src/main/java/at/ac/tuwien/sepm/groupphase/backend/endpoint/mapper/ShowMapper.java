package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Show;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {VenueMapper.class, EventMapper.class, PricingMapper.class, SeatingChartMapper.class})
public interface ShowMapper {

    /**
     * Maps the given Show DTO to a Domain representation of the Show
     *
     * @param showDto the Show DTO to be mapped
     * @return the mapped Show
     */
    @Named("show")
    @Mapping(source = "pricings", target = "pricings", qualifiedByName = "pricings")
    @Mapping(source = "seatingChart", target = "seatingChart", qualifiedByName = "seatingChart")
    Show showDtoToShow(ShowDto showDto);


    /**
     * Maps the given Show to a Data Transfer Object representation of the Show
     *
     * @param show the Show to be mapped
     * @return the mapped Show DTO
     */
    @Named("showDto")
    @Mapping(source = "seatingChart", target = "seatingChart", qualifiedByName = "seatingChartDto")
    ShowDto showToShowDto(Show show);

    /**
     * Maps the given Shows in a List to a List of Data Transfer Object representations of the Shows
     *
     * @param shows the List of Shows to be mapped
     * @return the mapped Show DTO List
     */
    @IterableMapping(qualifiedByName = "showDto")
    List<ShowDto> showToShowDto(List<Show> shows);

}

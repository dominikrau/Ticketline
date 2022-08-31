package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Event;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = { ArtistMapper.class })
public interface EventMapper {
    /**
     * Maps the given Event Dto to a Domain representation of the Event
     *
     * @param eventDto the Event DTO to be mapped
     * @return the mapped Event
     */
    Event eventDtoToEvent(EventDto eventDto);


    /**
     * Maps the given Event to a Data Transfer Object representation of the Event
     *
     * @param event the Event to be mapped
     * @return the mapped Event DTO
     */
    @Named("event")
    EventDto eventToEventDto(Event event);

    /**
     * Maps the given Events in a List to a List of Data Transfer Object representations of the Events
     *
     * @param events the List of Events to be mapped
     * @return the mapped Event DTO List
     */
    @IterableMapping(qualifiedByName = "event")
    List<EventDto> eventToEventDto(List<Event> events);

}

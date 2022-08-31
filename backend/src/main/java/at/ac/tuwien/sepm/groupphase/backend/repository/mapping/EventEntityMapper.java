package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.EventEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = { ArtistEntityMapper.class})
public interface EventEntityMapper {

    /**
     * Maps the given Event Entity to a Domain representation of the Event
     *
     * @param event the Event Entity to be mapped
     * @return the mapped Event
     */
    @Named("event")
    @Mapping(target = "imageUrl",
        expression = "java(event.getImageUrl().startsWith(\"http\")?event.getImageUrl():\"http://localhost:8080/uploads/images/\" + event.getImageUrl() )")
    Event toDomain(EventEntity event);

    /**
     * Maps the given Events Entities in a List to a List of Domain representations of the Events
     *
     * @param eventEntities the List of Event Entities to be mapped
     * @return the mapped Event List
     */
    @IterableMapping(qualifiedByName = "event")
    List<Event> toDomain(List<EventEntity> eventEntities);


    /**
     * Maps the given Event to a Repository Entity representation of the Event
     *
     * @param event the Event to be mapped
     * @return the mapped Event Entity
     */
    EventEntity toEntity(Event event);

}

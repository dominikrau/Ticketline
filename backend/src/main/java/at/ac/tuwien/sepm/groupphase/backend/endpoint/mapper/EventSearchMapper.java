package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.EventType;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.ArtistSearch;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.EventSearch;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.ShowSearch;
import io.vavr.control.Try;

import java.time.Duration;
import java.time.LocalTime;

public class EventSearchMapper {

    private EventSearchMapper() {
    }

    /**
     * Creates a domain representation of an Event Search with the specified attributes
     *
     * @param name of the event to be searched for
     * @param description of the event to be searched for
     * @param eventType of the event to be searched for
     * @param firstName of the event to be searched for
     * @param lastName of the event to be searched for
     * @param pseudonym of the event to be searched for
     * @param startTime of the event to be searched for
     * @param endTime of the event to be searched for
     * @param duration of the event to be searched for
     * @param minPrice of the event to be searched for
     * @param maxPrice of the event to be searched for
     * @param location of the event to be searched for
     * @param hall of the event to be searched for
     * @return the mapped Event Search object
     */
    public static EventSearch toEventSearch(
        String name,
        String description,
        String eventType,
        String firstName,
        String lastName,
        String pseudonym,
        String startTime,
        String endTime,
        String duration,
        Double minPrice,
        Double maxPrice,
        String location,
        String hall
    ) {
        final LocalTime start = Try.of(() -> LocalTime.parse(startTime)).getOrElse(() -> null);
        final LocalTime end = Try.of(() -> LocalTime.parse(endTime)).getOrElse(() -> null);
        final Duration dur = Try.of(() -> Duration.parse(duration)).getOrElse(() -> null);
        return EventSearch.builder()
            .name(name)
            .description(description)
            .eventType(EventType.fromCode(eventType))
            .artistSearch(ArtistSearch.builder()
                .firstName(firstName)
                .lastName(lastName)
                .pseudonym(pseudonym)
                .build())
            .showSearch(ShowSearch.builder()
                .startTime(start)
                .endTime(end)
                .duration(dur)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .location(location)
                .hall(hall)
                .build())
            .build();
    }
}

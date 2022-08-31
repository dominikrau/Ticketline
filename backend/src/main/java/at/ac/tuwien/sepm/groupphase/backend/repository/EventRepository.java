package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Event;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.EventType;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.EventSearch;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.EventEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.EventEntityMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.specification.EventSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class EventRepository {

    private final EventJpaRepository repository;
    private final EventEntityMapper mapper;

    /**
     * Find all event entries ordered by name (asscending).
     *
     * @return ordered list of all event entries
     */
    public List<Event> findAllEvents() {
        return repository.findAllByOrderByNameAsc()
            .stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    /**
     * Find specific event entry by ID.
     *
     * @return specified event
     * @throws NotFoundException will be thrown if the event could not be found in the system
     */
    public Event findById(Long id) {
        return repository.findById(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new NotFoundException(
                format("No Event with given ID \"%s\" exists", id)
            ));
    }

    /**
     * Find all event entries ordered by name (asscending).
     *
     * @return the saved event
     */
    public Event saveEvent(Event event) {
        EventEntity saved = repository.save(mapper.toEntity(event));
        return mapper.toDomain(saved);
    }

    /**
     * Get the amount of events in the system.
     *
     * @return total number of events
     */
    public long numberOfEvents() {
        return repository.count();
    }

    /**
     * search for a Page of Events in the database matching the provided search attributes
     *
     * @param search EventSearch object containing the attributes to be searched for
     * @param page specification for page (page number and page size)
     * @return specified Page of Events matching the search attributes
     */
    public Page<Event> search(final EventSearch search, final Pageable page) {
        log.debug("Searching for parameters: {}", search);
        return repository.findAll(EventSpecification.of(search), page)
            .map(mapper::toDomain);
    }

    /**
     * search for a List of Events in the database matching the provided search attributes
     *
     * @param search EventSearch object containing the attributes to be searched for
     * @return List of Events matching the search attributes
     */

    public List<Event> search(final EventSearch search) {
        return search(search, Pageable.unpaged())
            .toList();
    }


    /**
     * Find top ten  event entries ordered by bought/reserved tickets.
     *
     * @param endTime events with shows starting before endTime will be selected
     * @param startTime events with shows starting after startTime will be selected
     * @param type events with shows with given eventType will be selected
     * @return ordered list of top ten events
     */
    public List<Event> findTopTenEventsByTypeAndDate(EventType type, LocalDate startTime, LocalDate endTime) {
        List<EventEntity> events = repository.findTopTenEventsByCategory(type == null ? null : type.getCode(), startTime, endTime);
        if (events.size() > 10) {
            events = events.subList(0,10);
        }
        return mapper.toDomain(events);
    }

}

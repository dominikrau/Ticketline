package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Event;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.EventType;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.EventSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {

    /**
     * Find all event entries ordered by name (ascending).
     *
     * @return ordered list of all event entries
     */
    List<Event> findAll();

    /**
     * Find a single event entry by id.
     *
     * @param id the id of the event entry to be retrieved
     * @return event entry with the specified id
     */
    Event findOne(Long id);

    /**
     * Create a single event entry
     *
     * @param event to save
     * @return saved event entry
     */
    Event createEvent(Event event);

    /**
     * search for a Page of Events matching the provided search attributes
     *
     * @param search EventSearch object containing the attributes to be searched for
     * @param pageable specification for page (page number and page size)
     * @return specified Page of Events matching the search attributes
     */
    Page<Event> searchEvents(EventSearch search, Pageable pageable);


    /**
     * Find top ten  event entries ordered by bought/reserved tickets.
     *
     * @param month events in this month will be selected
     * @param type events with shows with given eventType will be selected
     * @return ordered list of top ten events
     */
    List<Event> findTopEventsByType(EventType type, String month);

}

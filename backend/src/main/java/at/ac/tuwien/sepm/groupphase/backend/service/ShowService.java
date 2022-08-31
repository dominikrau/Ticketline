package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Show;

import java.util.List;

public interface ShowService {
    /**
     * Find all show entries ordered by creation date (descending).
     *
     * @return ordered list of all show entries
     */
    List<Show> findAll();


    /**
     * Find a single show by id.
     *
     * @param id the id of the show
     * @return the show
     */
    Show findOne(Long id);

    /**
     * Create a single show
     *
     * @param show to publish
     * @return published show
     */
    Show createShow(Show show);

    /**
     * Create a single show for a corresponding event.
     *
     * @param show to publish
     * @param eventId id of the corresponding event.
     * @return published show
     */
    Show createShow(Show show, Long eventId);

    /**
     * Find all shows for event by event id.
     *
     * @param eventId the id of the event
     * @return list of shows
     */
    List<Show> findShowsForEventWithId(Long eventId);

    /**
     * Find a single show by id with all the un/-available places for the seating chart.
     *
     * @param id the id of the show
     * @return the show with a seating chart that shows all un-/available places
     */
    Show findByIdWithAvailablePlaces(Long id);

}

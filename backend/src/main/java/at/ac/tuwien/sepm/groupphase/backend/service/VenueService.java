package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Venue;

import java.util.List;

public interface VenueService {
    /**
     * Find all venue entries ordered by name (ascending).
     *
     * @return ordered list of all venue entries
     */
    List<Venue> findAll();


    /**
     * Find a single venue entry by id.
     *
     * @param id the id of the venue entry
     * @return the venue entry
     */
    Venue findById(Long id);

    /**
     * Find a single venue entry by name.
     *
     * @param name the name of the venue entry
     * @return the venue entry
     */
    Venue findByName(String name);

    /**
     * Create a single venue entry
     *
     * @param venue to save
     * @return saved venue entry
     */
    Venue createVenue(Venue venue);

    /**
     * Finds all Venues with the given substring in it's name
     *
     * @param search The substring to be searched for in the venue's name
     * @return all Venues with a name containing the given search parameter ignoring Case
     */
    List<Venue> search(String search);

}

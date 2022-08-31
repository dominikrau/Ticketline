package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Hall;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.HallSearch;

import java.util.List;

public interface HallService {

    /**
     * Find all hall entries ordered by name (ascending).
     *
     * @return ordered list of all hall entries
     */
    List<Hall> findAll();

    /**
     * Find a single hall entry by id.
     *
     * @param id the id of the hall entry
     * @return the hall entry
     */
    Hall findById(Long id);

    /**
     * Create a single hall entry
     *
     * @param hall to be saved
     * @return saved hall entry
     */
    Hall createHall(Hall hall);

    /**
     * Searches for Halls that fit the given criteria
     *
     * @param search The Criteria to Search for
     * @return All Halls matching the Criteria
     */
    List<Hall> search(HallSearch search);
}

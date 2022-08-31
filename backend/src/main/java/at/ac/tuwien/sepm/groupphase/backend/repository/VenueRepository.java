package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.VenueEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.VenueEntityMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.specification.VenueSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
public class VenueRepository {

    private final VenueJpaRepository repository;
    private final VenueEntityMapper mapper;


    /**
     * Find a single venue entry by name.
     *
     * @param name the name of the venue entry
     * @return the venue entry
     */
    public Venue findByName(String name) {
        return repository.findByName(name)
            .map(mapper::toDomain)
            .orElseThrow(() -> new NotFoundException(
                format("No Venue with given Name \"%s\"  found", name)
            ));
    }

    /**
     * Find all venue entries ordered by name (ascending).
     *
     * @return ordered list of all venue entries
     */
    public List<Venue> findAllByOrderNameAsc() {
        return repository.findAllByOrderByNameAsc()
            .stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    /**
     * Saves a single venue entry in the database
     *
     * @param venue to save
     * @return saved venue entry
     */
    public Venue saveVenue(Venue venue) {
        VenueEntity saved = repository.save(mapper.toEntity(venue));
        return mapper.toDomain(saved);
    }

    /**
     * Find a single venue entry by id.
     *
     * @param id the id of the venue entry
     * @return the venue entry
     */
    public Venue findById(Long id) {
        return repository.findById(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new NotFoundException(
                format("No Venue with given Id \"%s\"  found", id)
            ));
    }

    /**
     * Get the amount of venues in the database.
     *
     * @return total number of venues
     */
    public long numberOfVenues() {
        return repository.count();
    }

    /**
     * Finds all Venues with the given substring in it's name
     *
     * @param search The substring to be searched for in the venue's name
     * @return all Venues with a name containing the given search parameter ignoring Case
     */
    public List<Venue> search(String search) {
        return repository.findAll(VenueSpecification.of(search)).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

}

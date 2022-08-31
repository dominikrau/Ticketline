package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.repository.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueJpaRepository extends JpaRepository<VenueEntity, Long>, JpaSpecificationExecutor<VenueEntity> {

    /**
     * Find all venue entries ordered by name ascending.
     *
     * @return ordered list of all venue entries
     */
    List<VenueEntity> findAllByOrderByNameAsc();

    /**
     * Find all venue filtered by name entries ordered by name (ascending).
     *
     * @return ordered list of all venues entries
     */
    List<VenueEntity> findAllByNameContainingOrderByNameAsc(String searchTerm);

    /**
     * Find a single venue entry by name.
     *
     * @param name the name of the venue entry
     * @return the venue entry
     */
    Optional<VenueEntity> findByName(String name);

}

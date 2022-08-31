package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Hall;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.HallSearch;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.HallEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.HallEntityMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.specification.HallSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Transactional
@Service
@RequiredArgsConstructor
public class HallRepository {

    private final HallJpaRepository repository;
    private final HallEntityMapper mapper;

    /**
     * Find all hall entries in the database ordered by name (ascending).
     *
     * @return ordered list of all hall entries
     */
    public List<Hall> findAllByOrderByNameAsc() {
        return repository.findAllByOrderByNameAsc()
            .stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    /**
     * Find a single hall entry in the database by id.
     *
     * @param id the id of the venue entry
     * @return the hall entry
     */
    public Hall findById(Long id) {
        return repository.findById(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new NotFoundException(
                format("No hall with given id \"%s\" found", id)
            ));
    }


    /**
     * Save a single hall entry in the database
     *
     * @param hall to be saved
     * @return saved hall entry
     */
    public Hall saveHall(Hall hall){
        HallEntity saved = repository.save(mapper.toEntity(hall));
        return mapper.toDomain(saved);
    }

    /**
     * Searches for Halls in the database that fit the given criteria
     *
     * @param search The Criteria to Search for
     * @return All Halls matching the Criteria
     */
    public List<Hall> search(HallSearch search) {
        return repository.findAll(HallSpecification.of(search)).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

}

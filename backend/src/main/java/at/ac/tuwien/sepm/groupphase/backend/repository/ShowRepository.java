package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Show;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ShowEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.ShowEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ShowRepository {

    private final ShowJpaRepository repository;
    private final ShowEntityMapper mapper;

    /**
     * Find all Show entries in the database ordered by created at date (descending).
     *
     * @return ordered list of all Show entries
     */
    public List<Show> findAllByOrderByCreatedAt() {
        return repository.findAllByOrderByCreatedAt()
            .stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    /**
     * Save a show entry in the database
     *
     * @return the saved show
     */
    @SneakyThrows
    public Show saveShow(Show show) {
        ShowEntity mapped = mapper.toEntity(show);
        ShowEntity saved = repository.save(mapped);
        return mapper.toDomain(saved);
    }

    /**
     * Find specific Show entry by ID.
     *
     * @param id of the Show
     * @return specified Show
     */
    public Show findById(Long id) {
        return repository.findById(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new NotFoundException(
                format("No Show with given Id \"%s\"  found", id)
            ));
    }

    /**
     * Get the amount of shows in the database.
     *
     * @return total number of shows
     */
    public long numberOfShows() {
        return repository.count();
    }

    /**
     * Find all Show entries in the database.
     *
     * @return list of all Show entries
     */
    public List<Show> findAll() {
        return mapper.toDomain(repository.findAll());
    }

    /**
     * Find all Shows of a specified Event
     *
     * @param eventId Id of the event whose shows shall be retrieved
     * @return List of Shows
     */
    public List<Show> findShowsForEventWithId(Long eventId) {
        return mapper.toDomain(repository.findAllByEvent_IdOrderByStartTime(eventId));
    }

    /**
     * Find all Show ids in the database.
     *
     * @return list of all Show ids
     */
    public List<Long> findAllShowIds() {
        return repository.findAllShowIds();
    }

    /**
     * Find all show entries in a hall overlapping the given time.
     * @param endTime endTime of another show
     * @param startTime startTime of another show
     * @param hallId id of the hall that the shows shall be selected for
     * @return list of overlapping show entities
     */
    public List<Show> findOverlappingShows(Long hallId, LocalDateTime startTime, LocalDateTime endTime) {
        return mapper.toDomain(repository.findAllShowsByHallAndDateTime(startTime, endTime, hallId));
    }
}

package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ShowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowJpaRepository extends JpaRepository<ShowEntity, Long> {


    /**
     * Find all show entries ordered by created at date (descending).
     *
     * @return ordered list of all show entries
     */
    List<ShowEntity> findAllByOrderByCreatedAt();

    /**
     * Find specific Show entry in the database by ID.
     *
     * @param id of the Show
     * @return specified Show if it exists
     */
    Optional<ShowEntity> findById(Long id);

    /**
     * Find all Shows of a specified Event
     *
     * @param id of the event whose shows shall be retrieved
     * @return List of Shows
     */
    List<ShowEntity> findAllByEvent_IdOrderByStartTime(Long id);

    /**
     * Find all Show ids in the database.
     *
     * @return list of all Show ids
     */
    @Query("SELECT s.showId FROM ShowEntity s")
    List<Long> findAllShowIds();

    /**
     * Find all show entries in a hall overlapping the given time.
     * @param endTime endTime of another show
     * @param startTime startTime of another show
     * @param hallId id of the hall that the shows shall be selected for
     * @return list of overlapping show entities
     */
    @Query("Select s " +
        "FROM ShowEntity s " +
        "WHERE s.seatingChart.hall.id = :hallId " +
        "AND NOT(s.startTime >= :endTime or s.endTime <= :startTime)")
    List<ShowEntity> findAllShowsByHallAndDateTime(@Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime")LocalDateTime endTime,
                                                   @Param("hallId") Long hallId);

}
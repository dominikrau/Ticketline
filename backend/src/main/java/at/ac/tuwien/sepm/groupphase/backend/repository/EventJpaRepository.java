package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.repository.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    /**
     * Find all event entries ordered by name (descending).
     *
     * @return ordered list of all event entries
     */
    List<EventEntity> findAllByOrderByNameDesc();

    /**
     * Find all event entries ordered by name (ascending).
     *
     * @return ordered list of all event entries
     */
    List<EventEntity> findAllByOrderByNameAsc();



    /**
     * search for the most popular Events of a certain type between two points in time
     *
     * @param eventType of events to be looked for
     * @param startTime after which the Events are to happen
     * @param endTime before which the Events are to happen
     * @return List of most popular Events of the given type and in the given time
     */
    @Query("SELECT e " +
        "FROM EventEntity e " +
        "LEFT OUTER JOIN ShowEntity s ON e.id = s.event.id " +
        "LEFT OUTER JOIN TicketEntity t ON s.showId = t.show.showId " +
        "AND t.status = 'BOUGHT'" +
        "WHERE e.eventType LIKE COALESCE(CONCAT('%',:eventType,'%'),'%') " +
        "AND s.startTime >= COALESCE(:startTime, '2000-01-01') " +
        "AND s.startTime <= COALESCE(:endTime,'3000-01-01') " +
        "GROUP BY e " +
        "ORDER BY count(t) desc")
    List<EventEntity> findTopTenEventsByCategory(@Param("eventType") String eventType,
                                                 @Param("startTime") LocalDate startTime,
                                                 @Param("endTime") LocalDate endTime);

}

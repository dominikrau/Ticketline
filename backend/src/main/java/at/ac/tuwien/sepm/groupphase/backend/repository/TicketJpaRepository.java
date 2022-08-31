package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.repository.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketJpaRepository extends JpaRepository<TicketEntity, Long> {

    /**
     * Find all ticket entries ordered by ID.
     *
     * @return ordered list of all ticket entries
     */
    List<TicketEntity> findAllByOrderByCreatedAt();

    /**
     * Find a single ticket entry by id.
     *
     * @param id the id of the ticket entry
     * @return the ticket entry
     */
    Optional<TicketEntity> findByTicketId(Long id);

    /**
     * Find all ticket entries associated with the specified Order.
     *
     * @param orderId ID of Order to which the Tickets belong
     * @return List of all ticket entries associated with the Order
     */
    List<TicketEntity> findAllByTicketOrder_OrderId(Long orderId);

    /**
     * Find all ticket entries associated with the specified User.
     *
     * @param id ID of User to which the Tickets belong
     * @return List of all ticket entries associated with the User
     */
    List<TicketEntity> findAllByUser_Id(Long id);


    /**
     * Get the amount of places in the specified sector and show whose status are not like the provided string
     * @param sectorId Id of the sector whose places amount with unequal status shall be counted
     * @param showId Id of the corresponding show
     * @param status to be compared to
     * @return amount of places with unmatching status
     */
    int countAllBySector_IdAndShow_showIdAndStatusNotLike(Long sectorId, Long showId, String status);

    /**
     * Checks if a specified seat matches a status
     * @param seatId Id of seat whose status shall be checked
     * @param showId Id of show to which the seat belongs
     * @return true if specified seat's status matches
     */
    boolean existsTicketEntityBySeat_IdAndShow_showIdAndStatusNotLike(Long seatId, Long showId, String status);

    /**
     * Updates the status of multiple Ticket entries.
     *
     * @param ids List of the IDs of all tickets to be updated
     * @param status to be assigned to tickets
     */
    @Modifying
    @Query("UPDATE TicketEntity ticket set ticket.status = :status where ticket.ticketId in :ticket_ids")
    void updateTicketStatus(@Param("ticket_ids") List<Long> ids, @Param("status") String status);

    /**
     * Updates the status of a single Ticket entry.
     *
     * @param id of the ticket to be updated
     * @param status to be assigned to tickets
     */
    @Modifying
    @Query("UPDATE TicketEntity ticket set ticket.status = :status where ticket.ticketId = :ticket_id")
    void updateTicketStatus(@Param("ticket_id") Long id, @Param("status") String status);
}

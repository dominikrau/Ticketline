package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.TicketStatus;

import java.util.List;

public interface TicketService {

    /**
     * Find all ticket entries ordered by name (ascending).
     *
     * @return ordered list of all ticket entries
     */
    List<Ticket> findAll();

    /**
     * Find a single ticket entry by id.
     *
     * @param id the id of the ticket entry
     * @return the ticket entry
     */
    Ticket findById(Long id);

    /**
     * Creates a Pdf representation of the Ticket
     *
     * @param id The Id of the Ticket to generate the Pdf from
     * @return The Pdf as a Byte Array
     */
    byte[] createTicketPdf(Long id);

    /**
     * Find all ticket entries for a corresponding order.
     *
     * @param id of the corresponding order.
     * @return list of all ticket entries for specified order.
     */
    List<Ticket> findTicketsForOrderWithId(Long id);

    /**
     * Save multiple Ticket entries
     *
     * @param tickets List of Tickets to be saved
     * @return List of saved Ticket entries
     */
    List<Ticket> saveTicket(final List<Ticket> tickets);

    /**
     * Cancel a Ticket. Ticket entry is deleted if the Ticket was reserved,
     * otherwise its' status is updated to Cancelled
     *
     * @param ticketId of the Ticket to be cancelled
     */
    void cancelTicket(Long ticketId);

    /**
     * Updates the status of multiple Ticket entries.
     *
     * @param ticketIds List of the IDs of all tickets to be updated
     * @param status to be assigned to tickets
     */
    void updateTicketStatus(List<Long> ticketIds, TicketStatus status);

    /**
     * Creates a the Cancellation Invoice of the Ticket with this id as a pdf, if the ticket is cancelled
     * @param id The id of the Ticket
     * @return The Cancellation Invoice
     */
    byte[] createCancellationPdf(Long id);

}
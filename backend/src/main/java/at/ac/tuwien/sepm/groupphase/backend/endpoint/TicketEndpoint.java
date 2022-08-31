package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/tickets")
public class TicketEndpoint {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @Autowired
    public TicketEndpoint(TicketService ticketService, TicketMapper ticketMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }


    /**
     * Accepts the GET-Request and proceeds to retrieve all Ticket entries in the database
     *
     * @return A List of Ticket DTOs containing all saved Tickets
     */
    @GetMapping
    @ApiOperation(value = "Get list of all tickets", authorizations = {@Authorization(value = "apiKey")})
    public List<TicketDto> findAll() {
        log.info("GET /api/v1/tickets");
        return ticketMapper.ticketToTicketDto(ticketService.findAll());
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve a single Ticket entry
     *
     * @param id of the Ticket to be retrieved
     * @return the Ticket DTO of the specified Ticket
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get ticket by id",
        authorizations = {@Authorization(value = "apiKey")})
    public TicketDto getTicketById(@PathVariable("id") Long id) {
        log.info("GET /api/v1/tickets/{}", id);
        return ticketMapper.ticketToTicketDto(ticketService.findById(id));
    }

    /**
     * Accepts the DELETE-Request and proceeds to delete or cancel the specified Ticket entry.
     * Ticket entry is deleted if the Ticket was reserved, otherwise its' status is updated to Cancelled
     *
     * @param id of the Ticket to be cancelled or deleted
     */
    @DeleteMapping("{id}")
    public void cancelTicket(@PathVariable("id") Long id) {
        ticketService.cancelTicket(id);
    }

    @GetMapping(value = "/{id}/cancellation", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getCancellationPdf(@PathVariable("id") Long id) {
        return ticketService.createCancellationPdf(id);
    }
}

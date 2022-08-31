package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Order;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OrderMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PaymentMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PurchaseIntentMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/orders")
@RequiredArgsConstructor
public class OrderEndpoint {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final TicketMapper ticketMapper;
    private final TicketService ticketService;
    private final PurchaseIntentMapper purchaseIntentMapper;
    private final PaymentMapper paymentMapper;


    /**
     * Accepts the GET-Request and proceeds to retrieve all Order entries in the database
     *
     * @return A List of Order DTOs containing all saved Orders
     */
    @GetMapping
    @ApiOperation(value = "Get list of all orders", authorizations = {@Authorization(value = "apiKey")})
    public List<OrderDto> findAll() {
        log.info("GET /api/v1/orders");
        return orderMapper.orderToOrderDto(orderService.findAll());
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve a single Order entry
     *
     * @param id of the Order to be retrieved
     * @return the Order DTO of the specified Order
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get order by id",
        authorizations = {@Authorization(value = "apiKey")})
    public OrderDto getOrderById(@PathVariable("id") Long id) {
        log.info("GET /api/v1/orders/{}", id);
        return orderMapper.orderToOrderDto(orderService.findById(id));
    }

    /**
     * Accepts the POST-Request and proceeds to create a new Order entry
     *
     * @param purchaseIntentDto of the Order to be created
     * @return the Order DTO of the newly created Order (including ID and createdAt)
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create new order", authorizations = {@Authorization(value = "apiKey")})
    public OrderDto create(@Valid @RequestBody PurchaseIntentDto purchaseIntentDto) {
        log.info("POST /api/v1/orders body: {}", purchaseIntentDto);
        Order order = orderService.placeOrder(purchaseIntentMapper.toDomain(purchaseIntentDto));
        return orderMapper.orderToOrderDto(order);
    }

    /**
     * Finishes the buying process of reserved Tickets.
     *
     * @param paymentDto containing the payment information to be updated
     * @param id of the Order to be finished
     */
    @PutMapping("{id}/purchase")
    public void buyReservedTickets(@Valid @RequestBody PaymentDto paymentDto, @PathVariable("id") Long id) {
        log.info("PUT /api/v1/orders/{}", id);
        orderService.purchaseReservedOrder(id, paymentMapper.toDomain(paymentDto));
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve all Ticket entries of the specified Order
     *
     * @param id of the Order, whose Tickets are to be retrieved
     * @return A List of Ticket DTOs containing all Tickets of the Order
     */
    @GetMapping(value = "/{id}/tickets")
    @ApiOperation(value = "Get all tickets for order with id")
    public List<TicketDto> getTickets(@PathVariable("id") Long id) {
        log.info("GET /api/v1/orders/{}", id);
        return ticketMapper.ticketToTicketDto(ticketService.findTicketsForOrderWithId(id));
    }


    /**
     * Accepts the GET-Request and proceeds to search for the Orders of the current User
     *
     * @param page number of the Page to be retrieved
     * @param size of the Page to be retrieved
     * @return A Page of the Order DTOs of the current User
     */
    @GetMapping(value = "/user")
    @ApiOperation(value = "Get all orders for current user")
    public CustomPage<OrderDto> getOrdersByUser(
        @RequestParam("page") int page,
        @RequestParam("size") int size
    ) {
        log.info("GET /api/v1/orders/user");
        return CustomPage.of(orderService.pageOrdersOfUser(PageRequest.of(page, size))
            .map(orderMapper::orderToOrderDto));
    }

    /**
     * Accepts the GET-Request and proceeds to create the PDFs for the Tickets of the specified Order
     *
     * @param id of the Order, whose Ticket PDFs shall be created
     * @return the created Ticket PDFs
     */
    @GetMapping(value = "{id}/tickets/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @ApiOperation("")
    public byte[] createTicketPdfs(@PathVariable("id") Long id) {
        return ticketService.createTicketPdf(id);
    }


    /**
     * Accepts the GET-Request and proceeds to create the PDFs for the Receipt of the specified Order
     *
     * @param id of the Order, whose Receipt PDFs shall be created
     * @return the created Receipt PDFs
     */
    @GetMapping(value = "{id}/receipt", produces = MediaType.APPLICATION_PDF_VALUE)
    @ApiOperation("")
    public byte[] createReceiptPdf(@PathVariable("id") Long id) {
        return orderService.createOrderReceipt(id);
    }
}

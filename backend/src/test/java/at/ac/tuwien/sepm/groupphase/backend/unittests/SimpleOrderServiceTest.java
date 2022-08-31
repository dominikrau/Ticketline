package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.SimpleOrderService;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.buildSeatingChart;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SimpleOrderServiceTest implements TestData {

    @MockBean
    Validator validator;
    @MockBean
    UserService userService;
    @Autowired
    private SimpleOrderService simpleOrderService;
    @Autowired
    private TicketRepository ticketService;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private SeatingChartRepository seatingChartRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private EventService eventRepository;

    @Autowired
    private PricingRepository pricingRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private Show show;
    private ApplicationUser user;
    private List<Seat> seats;
    private List<StandingSector> standingSectors;

    @BeforeEach
    void setUp() {
        Venue savedVenue = venueRepository.saveVenue(Venue.builder()
            .name("TEST")
            .address(Address.builder()
                .city("TEST")
                .country("TEST")
                .postalCode("TEST")
                .street("TEST")
                .build())
            .build());
        Hall savedHall = hallRepository.saveHall(Hall.builder()
            .width(100)
            .height(100)
            .name("TEST")
            .venue(savedVenue)
            .build());
        SeatingChart seatingChart = buildSeatingChart().toBuilder()
            .hall(savedHall)
            .build();
        SeatingChart savedSeatingChart = seatingChartRepository.saveSeatingChart(seatingChart);
        List<Sector> sectors = seatingChart.getSectors().stream()
            .map(a -> sectorRepository.saveSector(a, savedSeatingChart))
            .collect(Collectors.toList());
        Event event = Event.builder()
            .name("TEST")
            .eventType(EventType.COMEDY)
            .imageUrl(TEST_EVENT_IMAGEURL)
            .description("TEST")
            .build();
        show = showRepository.saveShow(Show.builder()
            .event(eventRepository.createEvent(event))
            .venue(savedVenue)
            .startTime(LocalDateTime.of(2020, 12, 1, 12, 0, 0))
            .endTime(LocalDateTime.of(2020, 12, 2, 12, 0, 0))
            .seatingChart(savedSeatingChart)
            .build()
        );
        for (Sector savedSector : sectors) {
            pricingRepository.savePricing(Pricing.builder()
                    .price(20.0)
                    .sector(savedSector)
                    .build(),
                show
            );
        }
        user = userRepository.saveUser(ApplicationUser.builder()
            .firstName("TEST")
            .lastName("MAXIM")
            .emailAddress("test@email.com")
            .dateOfBirth(LocalDate.of(1999, 1, 1))
            .gender(Gender.OTHER)
            .password("password")
            .address(Address.builder()
                .country("TEST")
                .city("TEST")
                .postalCode("TEST")
                .street("TEST")
                .build())
            .build());
        when(userService.getCurrentUser()).thenReturn(user);
        show = showRepository.findById(show.getShowId());
        standingSectors = sectors.stream()
            .filter(a -> a instanceof StandingSector)
            .map(StandingSector.class::cast)
            .collect(Collectors.toList());
        seats = sectors.stream()
            .filter(a -> a instanceof SittingSector)
            .map(SittingSector.class::cast)
            .flatMap(sector -> sector.getSeats().stream())
            .collect(Collectors.toList());
    }

    @Test
    void findAll() {
        // just tests that it passes through from repository
        List<Order> orders = List.of(
            Order.builder().paymentMethod(PaymentMethod.RESERVATION).show(show).user(user).orderDate(Instant.now()).build(),
            Order.builder().paymentMethod(PaymentMethod.BANK_TRANSFER).show(show).user(user).orderDate(Instant.now()).build(),
            Order.builder().paymentMethod(PaymentMethod.CREDIT_CARD).show(show).user(user).orderDate(Instant.now()).build()
        ).stream()
            .map(orderRepository::saveOrder)
            .collect(Collectors.toList());
        List<Order> actual = simpleOrderService.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(orders);
    }

    @Test
    void findById() {
        List<Order> orders = List.of(
            Order.builder().paymentMethod(PaymentMethod.RESERVATION).show(show).user(user).orderDate(Instant.now()).build(),
            Order.builder().paymentMethod(PaymentMethod.BANK_TRANSFER).show(show).user(user).orderDate(Instant.now()).build(),
            Order.builder().paymentMethod(PaymentMethod.CREDIT_CARD).show(show).user(user).orderDate(Instant.now()).build()
        ).stream()
            .map(orderRepository::saveOrder)
            .collect(Collectors.toList());
        assertAll(orders.stream()
            .map(order -> () -> assertEquals(order, simpleOrderService.findById(order.getOrderId())))
        );
    }

    @ParameterizedTest()
    @MethodSource("placeOrderTestData")
    void placeOrderPurchasing(PaymentMethod paymentMethod, TicketStatus ticketStatus) {
        long noOfOrders = orderRepository.numberOfOrders();
        long noOfTickets = ticketService.numberOfTickets();
        Order order = placeOrder(paymentMethod, 3);
        assertEquals(noOfOrders + 1, orderRepository.numberOfOrders());
        assertEquals(noOfTickets + seats.size() + 3 * standingSectors.size(), ticketService.numberOfTickets());
        assertEquals(show, order.getShow());
        assertEquals(user, order.getUser());
        assertEquals(order, orderRepository.findById(order.getOrderId()));
        assertEquals(paymentMethod, order.getPaymentMethod());
        List<Ticket> tickets = ticketService.findTicketsForOrderWithId(order.getOrderId());
        assertTrue(tickets.stream().allMatch(ticket -> ticket.getStatus() == ticketStatus));
    }

    private static Stream<Arguments> placeOrderTestData() {
        return Stream.of(
          Arguments.of(PaymentMethod.BANK_TRANSFER, TicketStatus.BOUGHT),
          Arguments.of(PaymentMethod.CREDIT_CARD, TicketStatus.BOUGHT),
          Arguments.of(PaymentMethod.PAY_PAL, TicketStatus.BOUGHT),
          Arguments.of(PaymentMethod.RESERVATION, TicketStatus.RESERVED)
        );
    }

    private Order placeOrder(PaymentMethod paymentMethod, int standingAmount) {
        PurchaseValidation validation = PurchaseValidation.builder()
            .show(show)
            .standingSectors(standingSectors)
            .seats(seats)
            .build();
        when(validator.validatePurchaseIntent(any())).thenReturn(validation);
        PurchaseIntent intent = PurchaseIntent.builder()
            .payment(Payment.builder()
                .method(paymentMethod)
                .build())
            .seats(List.of())
            .showId(1)
            .standing(standingSectors.stream()
                .map(a -> StandingPurchaseIntent.builder().id(a.getId()).amount(standingAmount).build())
                .collect(Collectors.toList())
            )
            .build();
        return simpleOrderService.placeOrder(intent);
    }

    @Test
    void purchaseReservedOrder() {
        final Order order = placeOrder(PaymentMethod.RESERVATION, 2);
        assertEquals(PaymentMethod.RESERVATION, order.getPaymentMethod());
        List<Ticket> tickets = ticketService.findTicketsForOrderWithId(order.getOrderId());
        when(validator.validatePurchaseReserved(any(), eq(order.getOrderId()), any())).thenReturn(tickets);
        simpleOrderService.purchaseReservedOrder(order.getOrderId(), Payment.builder()
            .method(PaymentMethod.BANK_TRANSFER)
            .build());
        entityManager.flush();
        entityManager.clear();
        Order newOrder = orderRepository.findById(order.getOrderId());
        List<Ticket> newTickets = ticketService.findTicketsForOrderWithId(order.getOrderId());
        assertTrue(newTickets.stream().allMatch(ticket -> ticket.getStatus() == TicketStatus.BOUGHT));
        assertEquals(PaymentMethod.BANK_TRANSFER, newOrder.getPaymentMethod());
    }

    @Test
    void createOrderReceipt() {
        final Order order = orderRepository.saveOrder(Order.builder()
            .paymentMethod(PaymentMethod.BANK_TRANSFER)
            .show(show)
            .user(user)
            .orderDate(Instant.now())
            .build());
        assertDoesNotThrow(() -> simpleOrderService.createOrderReceipt(order.getOrderId()));
    }

    @Test
    void pageOrdersOfUser() {
        List<Order> orders = List.of(
            Order.builder().paymentMethod(PaymentMethod.RESERVATION).show(show).user(user).orderDate(Instant.now()).build(),
            Order.builder().paymentMethod(PaymentMethod.BANK_TRANSFER).show(show).user(user).orderDate(Instant.now()).build(),
            Order.builder().paymentMethod(PaymentMethod.CREDIT_CARD).show(show).user(user).orderDate(Instant.now()).build()
        ).stream()
            .map(orderRepository::saveOrder)
            .collect(Collectors.toList());
        final Order order = orderRepository.saveOrder(Order.builder()
            .paymentMethod(PaymentMethod.BANK_TRANSFER)
            .show(show)
            .user(userRepository.saveUser(ApplicationUser.builder()
                .gender(Gender.OTHER)
                .emailAddress("test2@email.com")
                .password("password1")
                .dateOfBirth(LocalDate.of(2020, 12, 2))
                .lastName("Testus")
                .firstName("Marxissa")
                .build()))
            .orderDate(Instant.now())
            .build());
        Page<Order> page = simpleOrderService.pageOrdersOfUser(Pageable.unpaged());
        assertThat(page.getContent()).containsExactlyInAnyOrderElementsOf(orders);
        assertThat(page.getContent()).doesNotContain(order);
    }
}
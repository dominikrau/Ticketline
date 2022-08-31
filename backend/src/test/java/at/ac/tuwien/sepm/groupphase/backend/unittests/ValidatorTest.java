package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidatorTest implements TestData {

    private SeatingChartRepository seatingChartRepository;
    private SeatRepository seatRepository;
    private TicketRepository ticketRepository;
    private SectorRepository sectorRepository;
    private ShowRepository showRepository;
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        seatingChartRepository = mock(SeatingChartRepository.class);
        seatRepository = mock(SeatRepository.class);
        ticketRepository = mock(TicketRepository.class);
        sectorRepository = mock(SectorRepository.class);
        showRepository = mock(ShowRepository.class);
        orderRepository = mock(OrderRepository.class);
    }

    private Validator toTest(Clock clock) {
        return new Validator(
            seatingChartRepository,
            seatRepository,
            ticketRepository,
            sectorRepository,
            showRepository,
            orderRepository,
            clock
        );
    }

    @Test
    void validatePurchaseIntent_AllGood() {
        AtomicLong i = new AtomicLong();
        List<Sector> sectorList = buildSectors();
        sectorList.forEach(sector -> {
            sector.setId(i.getAndIncrement());
            if (sector instanceof SittingSector) {
                List<Seat> seats = ((SittingSector) sector).getSeats().stream()
                    .map(s -> s.toBuilder().sector(sector).id(i.getAndIncrement()).build())
                    .collect(Collectors.toList());
                ((SittingSector) sector).setSeats(seats);
            }
        });
        SeatingChart chart = buildSeatingChart()
            .toBuilder()
            .sectors(sectorList)
            .build();
        Show show = Show.builder()
            .startTime(LocalDateTime.of(2020, 8, 1, 12, 0))
            .seatingChart(chart)
            .build();
        List<Sector> sectors = show.getSeatingChart().getSectors().stream()
            .filter(a -> a instanceof StandingSector)
            .collect(Collectors.toList());
        List<Seat> seats = show.getSeatingChart().getSectors().stream()
            .filter(a -> a instanceof SittingSector)
            .map(SittingSector.class::cast)
            .flatMap(sector -> sector.getSeats().stream())
            .collect(Collectors.toList());
        when(showRepository.findById(any())).thenReturn(show);
        when(sectorRepository.findByIds(any())).thenReturn(sectors);
        when(seatRepository.findByIds(any())).thenReturn(seats);
        when(ticketRepository.placesRemaining(any(), any())).thenReturn(100);
        when(ticketRepository.isSeatAvailable(any(), any())).thenReturn(true);
        Validator validator = toTest(Clock.fixed(Instant.parse("2020-01-01T10:15:30.00Z"), ZoneId.systemDefault()));
        PurchaseIntent intent = PurchaseIntent.builder()
            .payment(Payment.builder()
                .method(PaymentMethod.PAY_PAL)
                .build())
            .seats(List.of())
            .showId(1)
            .standing(sectors.stream()
                .map(a -> StandingPurchaseIntent.builder().id(a.getId()).amount(2).build())
                .collect(Collectors.toList())
            )
            .build();

        assertDoesNotThrow(() -> validator.validatePurchaseIntent(intent));
    }

    @Test
    void validatePurchaseIntent_OnlyInFuture() {
        AtomicLong i = new AtomicLong();
        List<Sector> sectorList = buildSectors();
        sectorList.forEach(sector -> {
            sector.setId(i.getAndIncrement());
            if (sector instanceof SittingSector) {
                List<Seat> seats = ((SittingSector) sector).getSeats().stream()
                    .map(s -> s.toBuilder().sector(sector).id(i.getAndIncrement()).build())
                    .collect(Collectors.toList());
                ((SittingSector) sector).setSeats(seats);
            }
        });
        SeatingChart chart = buildSeatingChart()
            .toBuilder()
            .sectors(sectorList)
            .build();
        Show show = Show.builder()
            .startTime(LocalDateTime.of(2020, 8, 1, 12, 0))
            .seatingChart(chart)
            .build();
        List<Sector> sectors = show.getSeatingChart().getSectors().stream()
            .filter(a -> a instanceof StandingSector)
            .collect(Collectors.toList());
        List<Seat> seats = show.getSeatingChart().getSectors().stream()
            .filter(a -> a instanceof SittingSector)
            .map(SittingSector.class::cast)
            .flatMap(sector -> sector.getSeats().stream())
            .collect(Collectors.toList());
        when(showRepository.findById(any())).thenReturn(show);
        when(sectorRepository.findByIds(any())).thenReturn(sectors);
        when(seatRepository.findByIds(any())).thenReturn(seats);
        when(ticketRepository.placesRemaining(any(), any())).thenReturn(100);
        when(ticketRepository.isSeatAvailable(any(), any())).thenReturn(true);
        Validator validator = toTest(Clock.fixed(Instant.parse("2021-01-01T10:15:30.00Z"), ZoneId.systemDefault()));
        PurchaseIntent intent = PurchaseIntent.builder()
            .payment(Payment.builder()
                .method(PaymentMethod.PAY_PAL)
                .build())
            .seats(List.of())
            .showId(1)
            .standing(sectors.stream()
                .map(a -> StandingPurchaseIntent.builder().id(a.getId()).amount(2).build())
                .collect(Collectors.toList())
            )
            .build();
        assertThrows(ValidationException.class, () -> validator.validatePurchaseIntent(intent));
    }

    @Test
    void validatePurchaseReserved_AllGood() {
        when(orderRepository.isOrderReserved(any())).thenReturn(true);
        Show show = Show.builder()
            .startTime(LocalDateTime.of(2020, 8, 1, 12, 0))
            .seatingChart(buildSeatingChart())
            .build();
        List<Ticket> tickets = List.of(
            Ticket.builder().user(buildApplicationUserWith("TEST")).show(show).build(),
            Ticket.builder().user(buildApplicationUserWith("TEST")).show(show).build(),
            Ticket.builder().user(buildApplicationUserWith("TEST")).show(show).build()
        );
        when(ticketRepository.findTicketsForOrderWithId(any())).thenReturn(tickets);
        Validator validator = toTest(Clock.fixed(Instant.parse("2020-01-01T10:15:30.00Z"), ZoneId.systemDefault()));
        assertDoesNotThrow(() -> validator.validatePurchaseReserved(
            Payment.builder().method(PaymentMethod.BANK_TRANSFER).build(),
            2L,
            buildApplicationUserWith("TEST")
        ));
    }

}

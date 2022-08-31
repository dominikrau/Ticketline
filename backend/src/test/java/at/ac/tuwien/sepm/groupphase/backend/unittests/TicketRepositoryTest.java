package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatingChartService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TicketRepositoryTest implements TestData {


    @Autowired
    private SeatingChartService seatingChartService;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private OrderRepository orderRepository;

    private SeatingChart seatingChart = TestData.buildSeatingChart();


    @Test
    public void givenTicket_whenSave_thenReturnCorrectEntity() {
        Venue venue = venueRepository.saveVenue(TestData.buildVenue());
        Hall hall = TestData.buildHall(200,400).toBuilder().venue(venue).build();
        Hall newHall = hallRepository.saveHall(hall);
        SeatingChart newSeatingChart = seatingChart.toBuilder().hall(newHall).build();
        SeatingChart savedSeatingChart  = seatingChartService.createSeatingChart(newSeatingChart);
        Event event = eventRepository.saveEvent(Event.builder()
            .imageUrl("testImage")
            .description("description1")
            .artist(Artist.builder()
                .firstName("Testname1")
                .lastName("Lastname1")
                .pseudonym("Tester1")
                .build())
            .name("name1")
            .eventType(EventType.SPORTS)
            .build());
        List<Pricing> pricings = new ArrayList<>();
        for (Sector sector: savedSeatingChart.getSectors()
        ) {
            Pricing pricing = Pricing.builder()
                .sector(sector)
                .price(10.00)
                .build();
            pricings.add(pricing);
        }
        Show savedShow = showRepository.saveShow(
            Show.builder()
                .pricings(pricings)
                .venue(venue)
                .event(event)
                .endTime(LocalDateTime.of(2021,1,1,8,10))
                .startTime(LocalDateTime.of(2021,1,1,12,10))
                .seatingChart(savedSeatingChart)
                .build());
        String emailOne = "test@test.at";
        ApplicationUser userOne = userRepository.saveUser(TestData.buildApplicationUserWith(emailOne));
        Order order = orderRepository.saveOrder(
            Order.builder()
            .paymentMethod(PaymentMethod.RESERVATION)
            .show(savedShow)
            .user(userOne)
            .orderDate(Instant.now())
            .build());
        Ticket savedTicket = ticketRepository.saveTicket(
            Ticket.builder()
            .price(10.00)
            .sector(savedShow.getSeatingChart().getSectors().get(1))
            .user(userOne)
            .show(savedShow)
            .ticketOrder(order)
            .status(TicketStatus.RESERVED)
            .build());

        assertAll(
            () -> assertNotNull(savedTicket.getTicketId()),
            () -> assertEquals(savedShow, savedTicket.getShow()),
            () -> assertEquals(userOne, savedTicket.getUser()),
            () -> assertEquals(TicketStatus.RESERVED, savedTicket.getStatus())
        );
    }
}

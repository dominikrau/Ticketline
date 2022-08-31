package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ShowRepositoryTest implements TestData {


    @Autowired
    private SeatingChartRepository seatingChartRepository;

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
    private ShowMapper showMapper;

    private SeatingChart seatingChart = TestData.buildSeatingChart();


    @Test
    public void givenShow_whenSave_thenReturnCorrectEntity() {
        Venue venue = venueRepository.saveVenue(TestData.buildVenue());
        Hall hall = TestData.buildHall(200,400).toBuilder().venue(venue).build();
        Hall newHall = hallRepository.saveHall(hall);
        SeatingChart newSeatingChart = seatingChart.toBuilder().hall(newHall).build();
        SeatingChart savedSeatingChart  = seatingChartRepository.saveSeatingChart(newSeatingChart);
        /*
        Artist artist = artistRepository.save(Artist.builder()
        .firstName("Testname1")
        .lastName("Lastname1")
        .pseudonym("Tester1")
        .build());

         */
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
        Show saved = showRepository.saveShow(
            Show.builder()
                .pricings(pricings)
                .venue(venue)
                .event(event)
                .endTime(LocalDateTime.of(2021,1,1,8,10))
                .startTime(LocalDateTime.of(2021,1,1,12,10))
                .seatingChart(savedSeatingChart)
                .build());

        assertAll(
            () -> assertNotNull(saved.getShowId()),
            () -> assertEquals(saved.getSeatingChart(), savedSeatingChart),
            () -> assertEquals(saved.getEvent(), event),
            () -> assertEquals(saved.getVenue(), venue),
            () -> assertEquals(saved.getPricings(), pricings)
        );
    }
}

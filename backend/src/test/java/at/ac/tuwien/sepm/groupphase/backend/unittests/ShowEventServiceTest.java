package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventJpaRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowJpaRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ArtistEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.EventEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.ArtistEntityMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.buildSeatingChart;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ShowEventServiceTest implements TestData {

    @Autowired
    EventJpaRepository eventJpaRepository;

    @Autowired
    ShowJpaRepository showRepository;

    @Autowired
    ShowService showService;

    @Autowired
    VenueService venueService;

    @Autowired
    EventService eventService;

    @Autowired
    ImageService imageService;

    @Autowired
    ArtistEntityMapper artistEntityMapper;

    @Autowired
    SeatingChartService seatingChartService;

    @Autowired
    HallService hallService;

    @AfterEach
    public void afterEach() {
        List<EventEntity> events = this.eventJpaRepository.findAll();
        events.forEach(event -> this.imageService.deleteByFileName(event.getImageUrl()));
    }

    @Test
    public void givenEventAndShow_whenSave_thenReturnCorrectShow() {

        ArtistEntity artist = ArtistEntity.builder()
            .firstName(TEST_ARTIST_FIRST_NAME)
            .lastName(TEST_ARTIST_LAST_NAME)
            .pseudonym(TEST_ARTIST_PSEUDONYM)
            .createdAt(TEST_ARTIST_CREATED_AT)
            .build();

        List<ArtistEntity> artists = new ArrayList<>();
        artists.add(artist);

        List<Artist> artists2 = new ArrayList<>();
        artists2.add(Artist.builder().firstName(TEST_ARTIST_FIRST_NAME).lastName(TEST_ARTIST_LAST_NAME).pseudonym(TEST_ARTIST_PSEUDONYM).build());

        /*Venue venue = Venue.builder()
            .name(TEST_VENUE_NAME)
            .address(Address.builder()
                .country(TEST_ADDRESS_COUNTRY)
                .city(TEST_ADDRESS_CITY)
                .postalCode(TEST_ADDRESS_POSTCODE)
                .street(TEST_ADDRESS_STREET)
                .build())
            .build();*/

        Event event2 = Event.builder()
            .description(TEST_EVENT_DESCRIPTION)
            .eventType(TEST_EVENT_TYPE)
            .name(TEST_EVENT_NAME)
            .artists(artists2)
            .imageUrl(TEST_EVENT_IMAGEURL)
            .build();


        SeatingChart seatingChart = buildSeatingChart();

        Venue savedVenue = venueService.createVenue(seatingChart.getHall().getVenue());

        Hall savedHall = hallService.createHall(seatingChart.getHall().toBuilder().venue(savedVenue).build());

        seatingChart = seatingChart.toBuilder().hall(savedHall).build();

        SeatingChart savedSeatingChart = seatingChartService.createSeatingChart(seatingChart);

        Event savedEvent = eventService.createEvent(event2);

        List<Pricing> pricingList = new ArrayList<>();
        for (Sector sector : savedSeatingChart.getSectors()) {
            pricingList.add(Pricing.builder().price(10.00).sector(sector).build());
        }


        Show show = Show.builder()
            .endTime(TEST_SHOW_END_TIME)
            .startTime(TEST_SHOW_START_TIME)
            .event(savedEvent)
            .venue(savedVenue)
            .seatingChart(savedSeatingChart)
            .pricings(pricingList)
            .build();

        Show saved = showService.createShow(show, savedEvent.getId());
        assertAll(
            () -> assertNotNull(saved),
            () -> assertNotNull(saved.getShowId()),
            () -> assertNotNull(saved.getEvent().getId()),
            () -> assertEquals(savedEvent.getId(), saved.getEvent().getId()),
            () -> assertEquals("Pitbull", saved.getEvent().getArtists().get(0).getPseudonym()),
            () -> assertEquals(TEST_SHOW_START_TIME, saved.getStartTime()),
            () -> assertEquals(TEST_SHOW_END_TIME, saved.getEndTime()),
            () -> assertEquals(TEST_VENUE_NAME, saved.getVenue().getName()),
            () -> assertEquals(TEST_ADDRESS_CITY, saved.getVenue().getAddress().getCity())
        );
    }
}

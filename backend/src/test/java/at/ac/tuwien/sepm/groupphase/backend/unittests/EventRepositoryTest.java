package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.ArtistSearch;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.EventSearch;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.ShowSearch;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ArtistEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.EventEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.buildSeatingChart;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class EventRepositoryTest implements TestData {

    @Autowired
    EventJpaRepository eventJpaRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ShowRepository showRepository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    HallRepository hallRepository;

    @Autowired
    SeatingChartRepository seatingChartRepository;

    @Autowired
    PricingRepository pricingRepository;

    @Autowired
    SectorRepository sectorRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    EntityManager entityManager;

    static Stream<Arguments> eventSearch() {
        return Stream.of(
            Arguments.of("TEST", "", null, List.of(0)),
            Arguments.of("TEST", "TEST", null, List.of()),
            Arguments.of("", "DESCRIPTION", null, List.of(1, 4, 5)),
            Arguments.of("", "", EventType.SPORTS, List.of(1, 3)),
            Arguments.of("", "TEST", EventType.SPORTS, List.of(1)),
            Arguments.of("whatever", "description", null, List.of(5))
        );
    }

    static Stream<Arguments> artistSearch() {
        return Stream.of(
            Arguments.of("Semin", "", "", List.of(1)),
            Arguments.of("", "NOT", "", List.of(2)),
            Arguments.of("", "", "er", List.of(1, 2, 4)),
            Arguments.of("Semin", "", "er", List.of(1)),
            Arguments.of("Nin", TEST_ARTIST_ROSSI, "", List.of(4)),
            Arguments.of("emi", TEST_ARTIST_ROSSI, "Schönster", List.of(1)),
            Arguments.of("", "", "Pitbull", List.of(0, 1, 2, 3, 4))
        );
    }

    @Test
    public void givenNothing_whenSaveEvent_thenFindListWithOneElementAndFindAddressById() {

        EventEntity eventEntity = EventEntity.builder()
            .name(TEST_EVENT_NAME + "DEBUG-STATEMENT")
            .description(TEST_EVENT_DESCRIPTION)
            .eventType(TEST_EVENT_TYPE_STRING)
            .imageUrl(TEST_EVENT_IMAGEURL)
            .build();

        eventJpaRepository.save(eventEntity);

        List<EventEntity> all = eventJpaRepository.findAll();
        assertAll(
            () -> assertEquals(1, eventJpaRepository.findAll().size(), "all " + all),
            () -> assertNotNull(eventJpaRepository.findById(eventEntity.getId()))
        );

    }

    @Test
    public void givenNothing_whenSaveEventWithoutName_thenFindListWithZeroElements() {
        EventEntity eventEntity = EventEntity.builder()
            .description(TEST_ARTIST_PSEUDONYM)
            .eventType(TEST_EVENT_TYPE_STRING)
            .name(TEST_EVENT_NAME)
            .build();

        assertThrows(DataIntegrityViolationException.class, () -> eventJpaRepository.save(eventEntity));
    }

    @Test
    public void givenEvent_whenSave_thenReturnCorrectEntity() {
        ArtistEntity artist = ArtistEntity.builder()
            .firstName(TEST_ARTIST_FIRST_NAME)
            .lastName(TEST_ARTIST_LAST_NAME)
            .pseudonym(TEST_ARTIST_PSEUDONYM)
            .createdAt(TEST_ARTIST_CREATED_AT)
            .build();

        List<ArtistEntity> artists = new ArrayList<>();
        artists.add(artist);

        EventEntity event = EventEntity.builder()
            .description(TEST_EVENT_DESCRIPTION)
            .eventType(TEST_EVENT_TYPE_STRING)
            .name(TEST_EVENT_NAME)
            .artists(artists)
            .imageUrl(TEST_EVENT_IMAGEURL)
            .build();

        EventEntity savedEntity = eventJpaRepository.save(event);

        assertAll(
            () -> assertNotNull(savedEntity.getId()),
            () -> assertEquals("Sheesh", savedEntity.getDescription()),
            () -> assertEquals("Pitbull", savedEntity
                .getArtists().get(0).getPseudonym())
        );
    }

    @Test
    void givenEmptySearch_whenSearch_thenReturnAll() {
        List<Event> saved = LongStream.range(1, 11)
            .mapToObj(
                a -> Event.builder()
                    .description(TEST_EVENT_DESCRIPTION)
                    .eventType(TEST_EVENT_TYPE)
                    .name(TEST_EVENT_NAME)
                    .imageUrl(TEST_EVENT_IMAGEURL)
                    .build())
            .map(eventRepository::saveEvent)
            .collect(Collectors.toList());

        List<EventEntity> all = eventJpaRepository.findAll();
        assertEquals(10L, eventRepository.numberOfEvents(), "saved, " + saved + " all, " + all);
        EventSearch search = EventSearch.builder().build();
        List<Event> found = eventRepository.search(search);
        assertThat(found).containsExactlyInAnyOrderElementsOf(saved);
    }

    @MethodSource("eventSearch")
    @ParameterizedTest()
    void givenEventSearchWithEventParams_whenSearch_returnOnlySearched(
        String name,
        String description,
        EventType eventType,
        List<Integer> expectedIndices
    ) {
        List<EventEntity> all = eventJpaRepository.findAll();
        assertEquals(0L, eventRepository.numberOfEvents(), "all " + all);
        List<Event> testSet = Stream.of(
            basicEvent().toBuilder().name("TEST_NAME").build(),
            basicEvent().toBuilder().description("TEST DESCRIPTION").eventType(EventType.SPORTS).build(),
            basicEvent().toBuilder().name("OTHER_NAME").eventType(EventType.COMEDY).build(),
            basicEvent().toBuilder().eventType(EventType.SPORTS).build(),
            basicEvent().toBuilder().description("SOME OTHER DESCRIPTION").build(),
            basicEvent().toBuilder().name("WHATEVER").description("SOME OTHER DESCRIPTION").eventType(EventType.MUSICAL).build()
        )
            .map(eventRepository::saveEvent)
            .collect(Collectors.toList());
        EventSearch search = EventSearch.builder()
            .name(name)
            .description(description)
            .eventType(eventType)
            .build();
        var expected = expectedIndices.stream()
            .map(testSet::get)
            .collect(Collectors.toList());
        var actual = eventRepository.search(search);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @MethodSource("artistSearch")
    @ParameterizedTest()
    void givenEventSearchWithArtistParams_whenSearch_returnOnlySearched(
        String firstName,
        String lastName,
        String pseudonym,
        List<Integer> expectedIndices
    ) {
        List<EventEntity> all = eventJpaRepository.findAll();
        assertEquals(0L, eventRepository.numberOfEvents(), "all " + all);
        List<Event> testSet = Stream.of(
            Artist.builder().firstName("TEST_NAME").lastName("tester").pseudonym("").build(),
            Artist.builder().firstName("Semino").lastName(TEST_ARTIST_ROSSI).pseudonym("Schönster Mann der Welt").build(),
            Artist.builder().pseudonym("Cher").lastName("Not").firstName("Dead").build(),
            Artist.builder().firstName("Tante").lastName("Elsa").pseudonym("die Dritte").build(),
            Artist.builder().firstName("Nino").lastName(TEST_ARTIST_ROSSI).pseudonym("Schönster Sohn der Welt").build()
        )
            .map(artistRepository::save)
            .map(artist -> basicEvent().toBuilder().artist(artist).build())
            .map(eventRepository::saveEvent)
            .collect(Collectors.toList());
        EventSearch search = EventSearch.builder()
            .artistSearch(ArtistSearch.builder()
                .firstName(firstName)
                .lastName(lastName)
                .pseudonym(pseudonym)
                .build())
            .build();
        var expected = expectedIndices.stream()
            .map(testSet::get)
            .collect(Collectors.toList());
        var actual = eventRepository.search(search);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @MethodSource("showSearch")
    @ParameterizedTest()
    void givenEventSearchWithShowParams_whenSearch_returnOnlySearched(
        LocalTime startTime,
        LocalTime endTime,
        Duration duration,
        Double minPrice,
        Double maxPrice,
        String location,
        String hall,
        List<Integer> expectedIndices
    ) {
        assertEquals(0L, eventRepository.numberOfEvents());
        // todo create testset
        EventSearch search = EventSearch.builder()
            .showSearch(
                ShowSearch.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .duration(duration)
                    .minPrice(minPrice)
                    .maxPrice(maxPrice)
                    .location(location)
                    .hall(hall)
                    .build()
            )
            .build();
        List<Show> shows = List.of(
            basicShow().toBuilder().startTime(LocalDateTime.parse("2019-05-01T10:00:00")).build(),
            basicShow().toBuilder().startTime(LocalDateTime.parse("2019-05-01T08:00:00")).build(),
            basicShow()
        );
        List<Venue> venues = List.of(
            venueFor("Stadthalle"),
            venueFor("Wienerberg"),
            venueFor("Oper")
        );
        List<Hall> halls = List.of(
            hallFor("A"),
            hallFor("B"),
            hallFor("C")
        );
        List<Double> prices = List.of(
            10.0,
            50.0,
            25.0
        );
        List<Event> testSet = new ArrayList<>();
        for (int i = 0; i < shows.size(); i++) {
            SeatingChart seatingChart = buildSeatingChart();
            Venue savedVenue = venueRepository.saveVenue(venues.get(i % venues.size()));
            Hall savedHall = hallRepository.saveHall(halls.get(i % halls.size()).toBuilder()
                .venue(savedVenue)
                .build());
            seatingChart = seatingChart.toBuilder()
                .hall(savedHall)
                .build();
            var savedSeatingChart = seatingChartRepository.saveSeatingChart(seatingChart);
            List<Sector> savedSectors = seatingChart.getSectors().stream()
                .map(a -> sectorRepository.saveSector(a, savedSeatingChart))
                .collect(Collectors.toList());
            Show show = shows.get(i).toBuilder()
                .event(eventRepository.saveEvent(basicEvent()))
                .venue(savedVenue)
                .seatingChart(savedSeatingChart)
                .build();
            Show saved = showRepository.saveShow(show);
            for (Sector sector : savedSectors) {
                pricingRepository.savePricing(Pricing.builder()
                        .price(prices.get(i % prices.size()))
                        .sector(sector)
                        .build(),
                    saved
                );
            }
            testSet.add(saved.getEvent());
        }
        var expected = expectedIndices.stream()
            .map(testSet::get)
            .collect(Collectors.toList());
        var actual = eventRepository.search(search);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    static Stream<Arguments> showSearch() {
        return Stream.of(
            Arguments.of(LocalTime.of(9, 0), null, null, null, null, null, null, List.of(0)),
            Arguments.of(null, null, Duration.parse("P1D"), null, null, null, null, List.of(2)),
            Arguments.of(null, null, null, 20D, null, null, null, List.of(1, 2)),
            Arguments.of(null, null, null, null, 30D, null, null, List.of(2, 0)),
            Arguments.of(null, null, null, 20D, 40D, null, null, List.of(2)),
            Arguments.of(null, null, null, null, null, "Stadt", null, List.of(0)),
            Arguments.of(null, null, null, null, null, null, "B", List.of(1)),
            // Arguments.of(null, null, null, null, null, null, "c", List.of(2)), // halls not yet connected to show
            Arguments.of(null, null, null, null, null, null, null, List.of(0, 1, 2))
        );
    }

    Event basicEvent() {
        return Event.builder()
            .description(TEST_EVENT_DESCRIPTION)
            .eventType(TEST_EVENT_TYPE)
            .name(TEST_EVENT_NAME)
            .imageUrl(TEST_EVENT_IMAGEURL)
            .artist(
                artistRepository.save(Artist.builder()
                    .firstName(TEST_ARTIST_FIRST_NAME)
                    .lastName(TEST_ARTIST_LAST_NAME)
                    .pseudonym(TEST_ARTIST_PSEUDONYM)
                    .build()
            ))
            .build();
    }

    Show basicShow() {
        return Show.builder()
            .startTime(LocalDateTime.parse("2020-01-01T00:00:00"))
            .endTime(LocalDateTime.parse("2020-01-02T00:00:00"))
            .build();
    }

    private Venue venueFor(String name) {
        return Venue.builder()
            .name(name)
            .address(Address.builder()
                .city("TEST")
                .country("TEST")
                .postalCode("TEST")
                .street("TEST")
                .build())
            .build();
    }

    private Hall hallFor(String name) {
        return Hall.builder()
            .name(name)
            .height(200)
            .width(200)
            .build();
    }

}

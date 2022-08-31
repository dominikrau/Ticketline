package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Address;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.SimpleVenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleVenueServiceTest {

    private VenueService venueService;
    private VenueRepository repo;
    @BeforeEach
    void setUp() {
        repo = mock(VenueRepository.class);
        venueService = new SimpleVenueService(repo);
    }

    @Test
    void loadVenueByName() {
        Venue venue = Venue.builder()
            .name("hero")
            .address(Address.builder()
                .country(TEST_ADDRESS_COUNTRY)
                .city(TEST_ADDRESS_CITY)
                .postalCode(TEST_ADDRESS_POSTCODE)
                .street(TEST_ADDRESS_STREET)
                .build())
            .build();
        when(repo.findByName(anyString())).thenReturn(venue);
        Venue found = venueService.findByName("hero");
        assertAll(
            () -> assertEquals(venue.getName(), found.getName()),
            () -> assertEquals(venue.getAddress(),found.getAddress())
        );

    }


    @Test
    void findVenueByVenuenameNoneFound() {
        when(repo.findByName(anyString())).thenThrow(new NotFoundException());
        assertThrows(
            NotFoundException.class,
            () -> venueService.findByName("whatever")
        );
    }


    @Test
    void createNewVenue() {
        when(repo.saveVenue(any(Venue.class))).thenAnswer(i -> i.getArguments()[0]);
        Venue venue = Venue.builder()
            .name(TEST_VENUE_NAME)
            .address(Address.builder()
                .country(TEST_ADDRESS_COUNTRY)
                .city(TEST_ADDRESS_CITY)
                .postalCode(TEST_ADDRESS_POSTCODE)
                .street(TEST_ADDRESS_STREET)
                .build())
            .build();
        Venue created = venueService.createVenue(venue);
        assertEquals(venue, created);
    }
}
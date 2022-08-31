package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.domain.model.Address;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class VenueRepositoryTest {

    @Autowired
    private VenueRepository venueRepository;

    private Venue venue = Venue.builder()
        .name(TEST_VENUE_NAME)
        .address(Address.builder()
            .country(TEST_ADDRESS_COUNTRY)
            .city(TEST_ADDRESS_CITY)
            .postalCode(TEST_ADDRESS_POSTCODE)
            .street(TEST_ADDRESS_STREET)
            .build())
        .build();

    @Test
    void findUserByName_returnsUserWithGivenName() {

        String nameOne = "testitest";
        String nameTwo = "perfectName";

        Venue venue1 = venueRepository.saveVenue(venue.toBuilder().name(nameOne).build());
        Venue venue2 = venueRepository.saveVenue(venue.toBuilder().name(nameTwo).build());

        Venue found = venueRepository.findByName(nameOne);
        assertEquals(venue1, found);

        found = venueRepository.findByName(nameTwo);
        assertEquals(venue2, found);
    }

    @Test
    void findUserByName_notExisting_shouldThrowNotFoundException() {
        assertEquals(0, venueRepository.numberOfVenues());
        assertThrows(NotFoundException.class, () -> venueRepository.findByName("notAvailable"));
    }

    @Test
    public void givenNothing_whenSaveVenue_thenFindListWithOneElementAndFindVenueById() {
        Venue saved = venueRepository.saveVenue(venue);
        assertEquals(1,venueRepository.numberOfVenues());
        Venue inDb = venueRepository.findByName(TEST_VENUE_NAME);
        assertEquals(saved,inDb);
    }

    @Test
    void numberOfVenues_returnsNumberofVenues() {
        assertEquals(0, venueRepository.numberOfVenues());
        venueRepository.saveVenue(venue);
        assertEquals(1, venueRepository.numberOfVenues());
    }
}

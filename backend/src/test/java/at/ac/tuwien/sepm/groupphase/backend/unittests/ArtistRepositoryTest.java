package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistJpaRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ArtistEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class ArtistRepositoryTest {

    @Autowired
    private ArtistJpaRepository artistJpaRepository;

    @Test
    public void givenNothing_whenSaveArtist_thenFindListWithOneElementAndFindArtistById() {
        ArtistEntity artistEntity = ArtistEntity.builder()
            .firstName(TEST_ARTIST_FIRST_NAME)
            .lastName(TEST_ARTIST_LAST_NAME)
            .pseudonym(TEST_ARTIST_PSEUDONYM)
            .createdAt(TEST_ARTIST_CREATED_AT)
            .build();

        artistJpaRepository.save(artistEntity);

        assertAll(
            () -> assertEquals(1, artistJpaRepository.findAll().size()),
            () -> assertNotNull(artistJpaRepository.findById(artistEntity.getId()))
        );
    }

    @Test
    public void givenNothing_whenSaveArtistWithoutFirstName_thenFindListWithZeroElements() {
        ArtistEntity artistEntity = ArtistEntity.builder()
            .lastName(TEST_ARTIST_LAST_NAME)
            .pseudonym(TEST_ARTIST_PSEUDONYM)
            .createdAt(TEST_ARTIST_CREATED_AT)
            .build();

        assertThrows(DataIntegrityViolationException.class, () -> artistJpaRepository.save(artistEntity));
    }
}

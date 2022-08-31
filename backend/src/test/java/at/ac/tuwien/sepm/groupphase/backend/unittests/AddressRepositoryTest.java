package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.AddressEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class AddressRepositoryTest implements TestData {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void givenNothing_whenSaveAddress_thenFindListWithOneElementAndFindAddressById() {
        AddressEntity address = AddressEntity.builder()
            .country(TEST_ADDRESS_COUNTRY)
            .city(TEST_ADDRESS_CITY)
            .postalCode(TEST_ADDRESS_POSTCODE)
            .street(TEST_ADDRESS_STREET)
            .build();

        addressRepository.save(address);

        assertAll(
            () -> assertEquals(1, addressRepository.findAll().size()),
            () -> assertNotNull(addressRepository.findById(address.getId()))
        );
    }

    @Test
    public void givenNothing_whenSaveAddressWithoutCountry_thenFindListWithZeroElements() {
        AddressEntity address = AddressEntity.builder()
            .city(TEST_ADDRESS_CITY)
            .postalCode(TEST_ADDRESS_POSTCODE)
            .street(TEST_ADDRESS_STREET)
            .build();

        assertThrows(DataIntegrityViolationException.class, () -> addressRepository.save(address));
    }

}

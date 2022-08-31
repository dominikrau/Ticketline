package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Venue;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleVenueService implements VenueService {

    private final VenueRepository venueRepository;

    @Override
    public List<Venue> findAll() {
        log.debug("Find all venues");
        return venueRepository.findAllByOrderNameAsc();
    }

    @Override
    public Venue findById(Long id) {
        log.debug("Find venue with id {}", id);
        return venueRepository.findById(id);
    }

    @Override
    public Venue findByName(String name) {
        log.debug("Find venue with name {}", name);
        return venueRepository.findByName(name);
    }

    @Override
    public Venue createVenue(Venue newVenue) {
        log.debug("Create venue {}", newVenue);
        return venueRepository.saveVenue(newVenue);
    }

    @Override
    public List<Venue> search(String search) {
        return venueRepository.search(search);
    }
}

package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.domain.model.Hall;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.HallSearch;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.HallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleHallService implements HallService {

    private final HallRepository hallRepository;


    @Override
    public List<Hall> findAll() {
        log.debug("Find all halls");
        return hallRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Hall findById(Long id) {
        log.debug("Find hall by id{}", id);
        return hallRepository.findById(id);
    }

    @Override
    public Hall createHall(Hall hall) {
        log.debug("Create hall {}", hall);
        return hallRepository.saveHall(hall);
    }

    @Override
    public List<Hall> search(HallSearch search) {
        return hallRepository.search(search);
    }
}

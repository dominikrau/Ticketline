package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleShowService implements ShowService {
    private final ShowRepository showRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final SimplePricingService simplePricingService;
    private final Validator validator;

    @Override
    public List<Show> findAll() {
        log.debug("Find all shows");
        log.debug(showRepository.findAllByOrderByCreatedAt().toString());
        return showRepository.findAllByOrderByCreatedAt();
    }

    @Override
    public Show findOne(Long id) {
        log.debug("Find show with id {}", id);
        return showRepository.findById(id);
    }

    @Transactional
    @Override
    public Show createShow(Show show) {
        log.debug("Create new show {}", show);
        validator.validateNewShow(show);
        return showRepository.saveShow(show);
    }

    @Transactional
    @Override
    public Show createShow(Show show, Long eventId) {
        log.debug("Create Show {} for event with id {}", show, eventId);
        validator.validateNewShow(show);
        Event found = eventRepository.findById(eventId);
        Show toSave = show.toBuilder().event(found).build();
        Show saved = showRepository.saveShow(toSave);
        List<Pricing> pricings = simplePricingService.createPricing(show.getPricings(),saved);
        saved = saved.toBuilder().pricings(pricings).build();
        return saved;
    }


    public List<Show> findShowsForEventWithId(Long eventId) {
        log.debug("Find shows for event with id {}", eventId);
        return showRepository.findShowsForEventWithId(eventId);
    }

    @Override
    public Show findByIdWithAvailablePlaces(Long id) {
        log.debug("Find by id {} with available places", id);
        Show show = findOne(id);
        SeatingChart seatingChart = show.getSeatingChart();
        List<Sector> sectors = seatingChart.getSectors();
        List<Sector> list = new ArrayList<>();
        for (Sector sector: sectors
        ) {
            if(sector instanceof StandingSector) {
                Integer available = ((StandingSector) sector).getCapacity() - ticketRepository.numberOfPlacesOccupied(sector.getId(),show.getShowId());
                ((StandingSector) sector).setAvailable(available);
                list.add(sector);
            }
            if(sector instanceof SittingSector) {
                List<Seat> seats = new ArrayList<>();
                for (Seat seat: ((SittingSector) sector).getSeats()
                     ) {
                    boolean available = false;
                    if (ticketRepository.isSeatAvailable(seat.getId(), show.getShowId())) {
                         available = true;
                    }
                    Seat availableSeat = seat.toBuilder().available(available).build();
                    seats.add(availableSeat);
                }
                ((SittingSector) sector).setSeats(seats);
                list.add(sector);
            }
        }
        seatingChart = seatingChart.toBuilder().sectors(list).build();
        show = show.toBuilder().seatingChart(seatingChart).build();
        return show;
    }


}

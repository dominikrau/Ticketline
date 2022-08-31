package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Event;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.EventType;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.EventSearch;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleEventService implements EventService {

    private final EventRepository eventRepository;
    private final ImageService imageService;

    @Override
    public List<Event> findAll() {
        log.debug("Find all events");
        return eventRepository.findAllEvents();
    }

    @Override
    public Event findOne(Long id) {
        log.debug("Find event with id {}", id);
        return  eventRepository.findById(id);
    }

    @Override
    public Event createEvent(Event event) {
        log.debug("Create new event {}", event);
        String savedImage = imageService.save(event.getImageUrl());
        return eventRepository.saveEvent(event.toBuilder().imageUrl(savedImage).build());
    }

    @Override
    public Page<Event> searchEvents(EventSearch search, Pageable pageable) {
        return eventRepository.search(search, pageable);
    }

    @Override
    public List<Event> findTopEventsByType(EventType type, String month) {
        final LocalDate selectedMonth = Try.of(() -> LocalDate.parse(month)).getOrElse(() -> null);
        LocalDate startTime = null;
        LocalDate endTime = null;
        if(selectedMonth != null) {
            startTime = selectedMonth.with(firstDayOfMonth());
            endTime = selectedMonth.with(lastDayOfMonth());
        }
        return eventRepository.findTopTenEventsByTypeAndDate(type, startTime, endTime);
    }


}

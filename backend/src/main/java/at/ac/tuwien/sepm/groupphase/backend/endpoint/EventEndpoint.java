package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.domain.model.Event;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CustomPage;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventSearchMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/events")
public class EventEndpoint {

    private final EventService eventService;
    private final ShowService showService;
    private final EventMapper eventMapper;
    private final ShowMapper showMapper;

    @Autowired
    public EventEndpoint(EventService eventService, ShowService showService, EventMapper eventMapper, ShowMapper showMapper) {
        this.eventService = eventService;
        this.showService = showService;
        this.eventMapper = eventMapper;
        this.showMapper = showMapper;
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve all Event entries in the database
     *
     * @return A List of Event DTOs containing all saved events
     */
    @GetMapping
    @ApiOperation(value = "Get list of all events by name (ascending)", authorizations = {@Authorization(value = "apiKey")})
    public List<EventDto> findAll() {
        log.info("GET /api/v1/events");
        return eventMapper.eventToEventDto(eventService.findAll());
    }

    /**
     * Accepts the GET-Request and proceeds to search for the provided attributes in the saved Event entries
     *
     * @param page        number of the Page to be retrieved
     * @param size        of the Page to be retrieved
     * @param name        of the Event to be retrieved
     * @param description of the Event to be retrieved
     * @param eventType   of the Event to be retrieved
     * @param firstName   of an Artist of the Event to be retrieved
     * @param lastName    of an Artist of the Event to be retrieved
     * @param pseudonym   of an Artist of the Event to be retrieved
     * @param startTime   of a Show of the Event to be retrieved
     * @param endTime     of a Show of the Event to be retrieved
     * @param duration    of a Show of the Event to be retrieved
     * @param minPrice    of a Show of the Event to be retrieved
     * @param maxPrice    of a Show of the Event to be retrieved
     * @param location    of a Show of the Event to be retrieved
     * @param hall        of a Show of the Event to be retrieved
     * @return A Page of Event DTOs whose properties match the attributes
     */
    @GetMapping(value = "/search")
    @ApiOperation(value = "Searches for Events based on the given Search parameters", authorizations = {@Authorization(value = "apiKey")})
    public CustomPage<EventDto> search(
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "eventType", required = false) String eventType,
        @RequestParam(value = "firstName", required = false) String firstName,
        @RequestParam(value = "lastName", required = false) String lastName,
        @RequestParam(value = "pseudonym", required = false) String pseudonym,
        @RequestParam(value = "startTime", required = false) String startTime,
        @RequestParam(value = "endTime", required = false) String endTime,
        @RequestParam(value = "duration", required = false) String duration,
        @RequestParam(value = "minPrice", required = false) Double minPrice,
        @RequestParam(value = "maxPrice", required = false) Double maxPrice,
        @RequestParam(value = "location", required = false) String location,
        @RequestParam(value = "hall", required = false) String hall
    ) {
        log.info("GET /api/v1/events");
        return CustomPage.of(eventService.searchEvents(EventSearchMapper.toEventSearch(
            name,
            description,
            eventType,
            firstName,
            lastName,
            pseudonym,
            startTime,
            endTime,
            duration,
            minPrice,
            maxPrice,
            location,
            hall
        ), PageRequest.of(page, size))
            .map(eventMapper::eventToEventDto));
    }


    /**
     * Accepts the GET-Request and proceeds to retrieve a single Event entry
     *
     * @param id of the Event to be retrieved
     * @return the Event DTO of the specified Event
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get event by id (ascending)",
        authorizations = {@Authorization(value = "apiKey")})
    public EventDto getEventById(@PathVariable("id") Long id) {
        log.info("GET /api/v1/events/{}", id);
        return eventMapper.eventToEventDto(eventService.findOne(id));
    }

    /**
     * Accepts the POST-Request and proceeds to create a new Event entry
     *
     * @param eventDto of the Event to be created
     * @return the Event DTO of the newly created Event (including ID and createdAt)
     */
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create new event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto create(@Valid @RequestBody EventDto eventDto) {
        log.info("POST /api/v1/events body: {}", eventDto);
        Event event = eventService.createEvent(eventMapper.eventDtoToEvent(eventDto));
        return eventMapper.eventToEventDto(event);
    }

    /**
     * Accepts the POST-Request and proceeds to create a new Show for an Event entry
     *
     * @param showDto of the Show to be added
     * @param id      of the Event to which the Show shall be added
     * @return the Show DTO of the newly created Show (including ID and createdAt)
     */
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{id}/shows")
    @ApiOperation(value = "Create new show for event with id")
    public ShowDto createShow(@Valid @RequestBody ShowDto showDto, @PathVariable Long id) {
        log.info("POST /api/v1/events/" + id + "/shows");
        return showMapper.showToShowDto(
            showService.createShow(showMapper.showDtoToShow(showDto), id));
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve all Show entries of the specified Event
     *
     * @param id of the Event, whose Shows are to be retrieved
     * @return A List of Show DTOs containing all Shows of the Event
     */
    @GetMapping(value = "/{id}/shows")
    @ApiOperation(value = "Get all shows for event with id")
    public List<ShowDto> getShows(@PathVariable("id") Long id) {
        log.info("GET /api/v1/events/{}", id);
        return showMapper.showToShowDto(showService.findShowsForEventWithId(id));
    }


    /**
     * Accepts the GET-Request and proceeds to retrieve the Top 10 Events for a certain EventType and Month
     *
     * @param eventType of the Top Events to be retrieved
     * @param month     of the Top Events to be retrieved
     * @return A List of Event DTOs containing the Top 10 Events
     */
    @GetMapping(value = "/charts")
    @ApiOperation(value = "Get top ten Events")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<EventDto> getTopTenEvents(
        @RequestParam(value = "eventType", required = false) String eventType,
        @RequestParam(value = "month", required = false) String month
    ) {
        log.info("GET /api/v1/events/topTen searchParams: {} {}", eventType, month);
        return eventMapper.eventToEventDto(eventService.findTopEventsByType(EventType.fromCode(eventType), month));
    }


}

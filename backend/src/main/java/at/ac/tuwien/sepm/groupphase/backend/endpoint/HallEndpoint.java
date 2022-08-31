package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.HallSearch;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.HallDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingChartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.HallMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatingChartMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.HallService;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatingChartService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "api/v1/halls")
public class HallEndpoint {

    private final HallService hallService;
    private final HallMapper hallMapper;
    private final SeatingChartMapper seatingChartMapper;
    private final SeatingChartService seatingChartService;

    @Autowired
    public HallEndpoint(HallService hallService, HallMapper hallMapper, SeatingChartMapper seatingChartMapper, SeatingChartService seatingChartService) {
        this.hallService = hallService;
        this.hallMapper = hallMapper;
        this.seatingChartMapper = seatingChartMapper;
        this.seatingChartService = seatingChartService;

    }

    /**
     * Accepts the GET-Request and proceeds to retrieve all Hall entries in the database
     *
     * @return A List of Hall DTOs containing all saved Halls
     */
    @GetMapping
    @ApiOperation(value = "Get list of halls", authorizations = {@Authorization(value = "apiKey")})
    public List<HallDto> findAll() {
        log.info("GET /api/v1/halls");
        return hallMapper.hallToHallDto(hallService.findAll());
    }


    /**
     * Accepts the GET-Request and proceeds to retrieve a single Hall entry
     *
     * @param id of the Hall to be retrieved
     * @return the Hall DTO of the specified Hall
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get hall with specified id", authorizations = {@Authorization(value = "apiKey")})
    public HallDto find(@PathVariable Long id) {
        log.info("GET /api/v1/halls/{}", id);
        return hallMapper.hallToHallDto(hallService.findById(id));
    }


    /**
     * Accepts the POST-Request and proceeds to create a new Hall entry
     *
     * @param hallDto of the Hall to be created
     * @return the Hall DTO of the newly created Hall (including ID and createdAt)
     */
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create a new hall", authorizations = {@Authorization(value = "apiKey")})
    public HallDto create(@Valid @RequestBody HallDto hallDto) {
        log.info("POST /api/v1/halls body: {}", hallDto);
        return hallMapper.hallToHallDto(
            hallService.createHall(hallMapper.hallDtoToHall(hallDto)));
    }

    /**
     * Accepts the POST-Request and proceeds to create a new Seating Chart for an Hall entry
     *
     * @param id of the Hall to which the Seating Chart shall be added
     * @param seatingChartDto of the Seating Chart to be added
     * @return the Seating Chart DTO of the newly created Seating Chart (including ID and createdAt)
     */
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{id}/seatingCharts")
    @ApiOperation(value = "Create a new seating chart", authorizations = {@Authorization(value = "apiKey")})
    public SeatingChartDto create(@Valid @RequestBody SeatingChartDto seatingChartDto, @PathVariable Long id) {
        log.info("Post /api/v1/halls/{}/seatingCharts body: {}", id, seatingChartDto);
        SeatingChartDto newSeatingChartDto = seatingChartDto.toBuilder().hall(hallMapper.hallToHallDto(hallService.findById(id))).build();
        return seatingChartMapper.toDto(
            seatingChartService.createSeatingChart(seatingChartMapper.toDomain(newSeatingChartDto)));
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve all Seating Charts entries of the specified Hall
     *
     * @param id of the Hall, whose Seating Charts are to be retrieved
     * @return A List of Seating Charts DTOs containing all Seating Charts of the Hall
     */
    @GetMapping(value = "/{id}/seatingCharts")
    @ApiOperation(value = "Get all seatingCharts from specific hall", authorizations = {@Authorization(value = "apiKey")})
    public List<SeatingChartDto> getSeatingCharts(@PathVariable Long id) {
        log.info("GET /api/v1/halls/{}/seatingCharts", id);
        return seatingChartMapper.toDto(
            seatingChartService.findAllByHallId(id)
        );
    }

    /**
     * Accepts the GET-Request and proceeds to search for the provided attributes in the saved Hall entries
     *
     * @param venueId of the Venue to which a Hall belongs
     * @param name of the Hall to be retrieved
     * @return A List of Hall DTOs whose properties match the attributes
     */
    @GetMapping("search")
    public List<HallDto> search(
        @RequestParam(value = "venueId", required = false) Long venueId,
        @RequestParam(value = "name", required = false) String name
    ) {
        return hallMapper.hallToHallDto(
            hallService.search(HallSearch.builder()
                .venueId(venueId)
                .name(name)
                .build()
            )
        );
    }

}

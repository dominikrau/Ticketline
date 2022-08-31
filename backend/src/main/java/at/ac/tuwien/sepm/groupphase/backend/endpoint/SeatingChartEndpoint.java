package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingChartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatingChartMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatingChartService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "api/v1/seatingCharts")
public class SeatingChartEndpoint {

    private final SeatingChartMapper seatingChartMapper;
    private final SeatingChartService seatingChartService;

    @Autowired
    public SeatingChartEndpoint(SeatingChartService seatingChartService, SeatingChartMapper seatingChartMapper) {
        this.seatingChartMapper = seatingChartMapper;
        this.seatingChartService = seatingChartService;
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve all Seating Chart entries in the database
     *
     * @return A List of Seating Chart DTOs containing all saved Seating Charts
     */
    @GetMapping
    @ApiOperation(value = "Get list of seating charts", authorizations = {@Authorization(value = "apiKey")})
    public List<SeatingChartDto> findAll() {
        log.info("GET /api/v1/seatingCharts");
        return seatingChartMapper.toDto(seatingChartService.findAll());
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve a single Seating Chart entry
     *
     * @param id of the Seating Chart to be retrieved
     * @return the Seating Chart DTO of the specified Seating Chart
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get seating chart by id", authorizations = {@Authorization(value = "apiKey")})
    public SeatingChartDto findById(@PathVariable Long id) {
        log.info("GET /api/v1/seatingCharts/{}", id);
        return seatingChartMapper.toDto(seatingChartService.findById(id));
    }

}

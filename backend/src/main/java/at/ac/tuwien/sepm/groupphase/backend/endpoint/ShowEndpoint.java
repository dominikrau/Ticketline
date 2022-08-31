package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(value ="/api/v1/shows")
public class ShowEndpoint {

    private final ShowService showService;
    private final ShowMapper showMapper;

    @Autowired
    public ShowEndpoint(ShowService showService, ShowMapper showMapper) {
        this.showService = showService;
        this.showMapper = showMapper;
    }


    /**
     * Accepts the GET-Request and proceeds to retrieve all Show entries in the database
     *
     * @return A List of Show DTOs containing all saved Shows
     */
    @GetMapping
    @ApiOperation(value = "Get list of Shows", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDto> findAll() {
        log.info("GET /api/v1/shows");
        return showMapper.showToShowDto(showService.findAll());
    }


    /**
     * Accepts the POST-Request and proceeds to create a new Show entry
     *
     * @param showDto of the Show to be created
     * @return the Show DTO of the newly created Show (including ID and createdAt)
     */
    @PostMapping
    @ApiOperation(value = "Create a new show", authorizations = {@Authorization(value = "apiKey")})
    public ShowDto create(@Valid @RequestBody ShowDto showDto) {
        log.info("POST /api/v1/shows body: {}", showDto);
        return showMapper.showToShowDto(
            showService.createShow(showMapper.showDtoToShow(showDto)));
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve a single Show entry
     *
     * @param id of the Show to be retrieved
     * @return the Show DTO of the specified Show
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get Show with specified id", authorizations = {@Authorization(value = "apiKey")})
    public ShowDto findById(@PathVariable Long id) {
        log.info("GET /api/v1/shows/{}", id);
        return showMapper.showToShowDto(showService.findByIdWithAvailablePlaces(id));
    }

}


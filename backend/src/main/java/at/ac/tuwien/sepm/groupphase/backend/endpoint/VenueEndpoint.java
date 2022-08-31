package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.VenueMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/venues")
public class VenueEndpoint {

    private final VenueService venueService;
    private final VenueMapper venueMapper;

    @Autowired
    public VenueEndpoint(VenueService venueService, VenueMapper venueMapper) {
        this.venueService = venueService;
        this.venueMapper = venueMapper;
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve all Venue entries in the database
     *
     * @return A List of Venue DTOs containing all saved Venues
     */
    @GetMapping
    @ApiOperation(value = "Get list of venues", authorizations = {@Authorization(value = "apiKey")})
    public List<VenueDto> findAll() {
        log.info("GET /api/v1/venues");
        return venueMapper.venueToVenueDto(venueService.findAll());
    }


    /**
     * Accepts the POST-Request and proceeds to create a new Venue entry
     *
     * @param venueDto of the Venue to be created
     * @return the Venue DTO of the newly created Venue (including ID and createdAt)
     */
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create a new venue", authorizations = {@Authorization(value = "apiKey")})
    public VenueDto create(@Valid @RequestBody VenueDto venueDto) {
        log.info("POST /api/v1/venues body: {}", venueDto);
        return venueMapper.venueToVenueDto(
            venueService.createVenue(venueMapper.venueDtoToVenue(venueDto)));
    }

    /**
     * Accepts the GET-Request and proceeds to search for the provided name substring in the saved Event entries
     *
     * @param name substring to be searched for in the Event database
     * @return A List of Event DTOs whose name property match the substring
     */
    @GetMapping("search")
    public List<VenueDto> search(@RequestParam(value = "name", required = false) String name) {
        return venueMapper.venueToVenueDto(venueService.search(name));
    }

}

package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/artists")
@RequiredArgsConstructor
public class ArtistEndpoint {

    private final ArtistService artistService;
    private final ArtistMapper artistMapper;

    /**
     * Accepts the POST-Request and proceeds to create a new Artist entry
     *
     * @param artistDto of the Artist to be created
     * @return the Artist DTO of the newly created Artist (including ID and createdAt)
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistDto createArtist(@Valid @RequestBody ArtistDto artistDto) {
        log.info("POST /api/v1/artists body: {}", artistDto);
        return artistMapper.artistToArtistDto(artistService.createArtist(
            artistMapper.artistDtoToArtist(artistDto)
        ));
    }


    /**
     * Accepts the GET-Request and proceeds to search for the provided name substring in the saved Artist entries
     *
     * @param name substring to be searched for in the Artist database
     * @return A List of Artist DTOs whose name properties match the substring
     */
    @GetMapping("")
    public List<ArtistDto> search(@RequestParam(value = "name", required = false) String name) {
        return artistMapper.artistToArtistDto(artistService.search(name));
    }

}

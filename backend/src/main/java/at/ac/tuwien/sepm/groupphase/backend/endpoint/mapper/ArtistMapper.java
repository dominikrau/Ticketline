package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Artist;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ArtistMapper {
    /**
     * Maps the given Artist DTO to a Domain representation of the Artist
     *
     * @param artistDto the Artist DTO to be mapped
     * @return the mapped Artist
     */
    @Named("artistDto")
    Artist artistDtoToArtist(ArtistDto artistDto);

    /**
     * Maps the given Artists DTOs in a List to a List of Domain representations of the Artists
     *
     * @param artistDtos the List of Artist DTOs to be mapped
     * @return the mapped Artist List
     */
    @IterableMapping(qualifiedByName = "artist")
    List<Artist> artistDtoToArtist(List<ArtistDto> artistDtos);


    /**
     * Maps the given Artist to a Data Transfer Object representation of the Artist
     *
     * @param artist the Artist to be mapped
     * @return the mapped Artist DTO
     */
    @Named("artist")
    ArtistDto artistToArtistDto(Artist artist);

    /**
     * Maps the given Artists in a List to a List of Data Transfer Object representations of the Artists
     *
     * @param artists the List of Artists to be mapped
     * @return the mapped Artist DTO List
     */
    @IterableMapping(qualifiedByName = "artist")
    List<ArtistDto> artistToArtistDto(List<Artist> artists);
}

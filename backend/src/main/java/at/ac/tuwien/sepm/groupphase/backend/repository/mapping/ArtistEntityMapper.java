package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ArtistEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ArtistEntityMapper {

    /**
     * Maps the given Artist Entity to a Domain representation of the Artist
     *
     * @param artistEntity the Artist Entity to be mapped
     * @return the mapped Artist
     */
    @Named("artist")
    Artist toDomain(ArtistEntity artistEntity);

    /**
     * Maps the given Artists Entities in a List to a List of Domain representations of the Artists
     *
     * @param artistEntities the List of Artist Entities to be mapped
     * @return the mapped Artist List
     */
    @IterableMapping(qualifiedByName = "artists")
    List<Artist> toDomain(List<ArtistEntity> artistEntities);

    /**
     * Maps the given Artist to a Repository Entity representation of the Artist
     *
     * @param artist the Artist to be mapped
     * @return the mapped Artist Entity
     */
    @Named("artist")
    ArtistEntity toEntity(Artist artist);

    /**
     * Maps the given Artists in a List to a List of Repository Entity representations of the Artists
     *
     * @param artists the List of Artists to be mapped
     * @return the mapped Artist Entity List
     */
    @IterableMapping(qualifiedByName = "artists")
    List<ArtistEntity> toEntity(List<Artist> artists);
}

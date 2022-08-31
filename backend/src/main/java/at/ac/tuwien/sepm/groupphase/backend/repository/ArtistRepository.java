package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.ArtistEntityMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.specification.ArtistSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
@RequiredArgsConstructor
public class ArtistRepository {

    private final ArtistJpaRepository artistJpaRepository;
    private final ArtistEntityMapper mapper;

    /**
     * Search for artists matching the search substring in the database.
     *
     * @param search substring to be searched for in the database
     * @return a List of Artists whose first name, last name or pseudonym matches the substring
     */
    public List<Artist> search(String search) {
        return artistJpaRepository.findAll(ArtistSpecification.of(search))
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    /**
     * saves a single artist entry in the database
     *
     * @param artist to save
     * @return saved artist entry
     */
    public Artist save(Artist artist) {
        return mapper.toDomain(artistJpaRepository.save(
            mapper.toEntity(artist)
        ));
    }

    /**
     * Find all artist entries ordered by created at date (descending).
     *
     * @return ordered list of all artist entries
     */
    public List<Artist> findAll() {
        return artistJpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

}

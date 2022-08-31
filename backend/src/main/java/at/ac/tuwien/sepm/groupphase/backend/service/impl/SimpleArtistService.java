package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleArtistService implements ArtistService {

    private final ArtistRepository repository;

    @Override
    public Artist createArtist(Artist artist) {
        log.debug("Create artist {}", artist);
        return repository.save(artist);
    }

    @Override
    public List<Artist> search(String search) {
        return repository.search(search);
    }
}

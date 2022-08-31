package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Artist;

import java.util.List;

public interface ArtistService {

    /**
     * Create single artist entry.
     *
     * @param artist to save
     * @return saved artist entry
     */
    Artist createArtist(Artist artist);

    /**
     * Search for artists matching the search substring.
     *
     * @param search substring to be searched for in the database
     * @return a List of Artists whose first name, last name or pseudonym matches the substring
     */
    List<Artist> search(String search);

}

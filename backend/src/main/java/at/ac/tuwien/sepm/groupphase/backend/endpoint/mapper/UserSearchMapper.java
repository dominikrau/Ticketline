package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.UserSearch;

public class UserSearchMapper {

    private UserSearchMapper() {
    }

    /**
     * Creates a domain representation of an User Search with the specified attributes
     *
     * @param firstName of the User to be searched for
     * @param lastName of the User to be searched for
     * @param blocked status of the User to be searched for
     * @return the mapped User Search object
     */
    public static UserSearch toUserSearch(
        String firstName,
        String lastName,
        boolean blocked
    ) {
        return UserSearch.builder()
            .firstName(firstName)
            .lastName(lastName)
            .blocked(blocked)
            .build();
    }
}

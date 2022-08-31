package at.ac.tuwien.sepm.groupphase.backend.repository.specification;

import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ArtistEntity;
import org.springframework.data.jpa.domain.Specification;

import static at.ac.tuwien.sepm.groupphase.backend.repository.specification.SpecificationUtils.ignoreCaseCriteria;

public class ArtistSpecification {

    private ArtistSpecification() {
    }

    /**
     * Returns a Specification to search for an Artist with the given name where it checks
     * if either of the artist names matches the input
     *
     * @param search The name of the artist to search for
     * @return The Specification to search for
     */
    public static Specification<ArtistEntity> of(final String search) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.or(
                ignoreCaseCriteria(criteriaBuilder, root.get("firstName"), search),
                ignoreCaseCriteria(criteriaBuilder, root.get("lastName"), search),
                ignoreCaseCriteria(criteriaBuilder, root.get("pseudonym"), search)
            );
        };
    }

}

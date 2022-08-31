package at.ac.tuwien.sepm.groupphase.backend.repository.specification;

import at.ac.tuwien.sepm.groupphase.backend.repository.entity.VenueEntity;
import org.springframework.data.jpa.domain.Specification;

import static at.ac.tuwien.sepm.groupphase.backend.repository.specification.SpecificationUtils.ignoreCaseCriteria;

public class VenueSpecification {

    private VenueSpecification() {
    }

    /**
     * Returns a specification to search for a venue with the given name
     * @param search the name of the venue to search for
     * @return the specification to search for
     */
    public static Specification<VenueEntity> of(String search) {
        return (root, query, criteriaBuilder) -> ignoreCaseCriteria(criteriaBuilder, root.get("name"), search);
    }

}

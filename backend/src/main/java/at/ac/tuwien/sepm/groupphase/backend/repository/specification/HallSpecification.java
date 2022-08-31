package at.ac.tuwien.sepm.groupphase.backend.repository.specification;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.HallSearch;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.HallEntity;
import org.springframework.data.jpa.domain.Specification;

import static at.ac.tuwien.sepm.groupphase.backend.repository.specification.SpecificationUtils.ignoreCaseCriteria;

public class HallSpecification {

    private HallSpecification() {
    }

    /**
     * Creates a Specification to search for the given Search params
     * @param search the params to search for
     * @return a Specification for the given Params
     */
    public static Specification<HallEntity> of(final HallSearch search) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
            criteriaBuilder.equal(root.get("venue"), search.getVenueId()),
            ignoreCaseCriteria(criteriaBuilder, root.get("name"), search.getName())
        );
    }

}

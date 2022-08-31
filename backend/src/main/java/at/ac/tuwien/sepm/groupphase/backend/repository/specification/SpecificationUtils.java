package at.ac.tuwien.sepm.groupphase.backend.repository.specification;

import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public class SpecificationUtils {

    private SpecificationUtils() {
    }

    private static String wildcard(String value) {
        return "%" + value + "%";
    }

    /**
     * returns a criteria where the expression is checked if it is like the search regardless of case
     * @param criteriaBuilder criteriaBuilder to create Predicate
     * @param expression the expression to test
     * @param search Param to search for
     * @return a Predicate containing the criteria
     */
    public static Predicate ignoreCaseCriteria(CriteriaBuilder criteriaBuilder, Expression<String> expression, String search) {
        return !StringUtils.isEmpty(search) ?
            criteriaBuilder.like(criteriaBuilder.lower(expression), wildcard(search.toLowerCase())) :
            null;
    }

}

package at.ac.tuwien.sepm.groupphase.backend.repository.specification;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.function.BasicFunctionExpression;

/**
 * represents a Sql unit function to use in Specifications
 * @param <X> The type of the function
 */
public class UnitFunctionExpression<X> extends BasicFunctionExpression<X> {
    public UnitFunctionExpression(CriteriaBuilderImpl criteriaBuilder, Class<X> javaType, String functionName) {
        super(criteriaBuilder, javaType, functionName);
    }

    @Override
    public String render(RenderingContext renderingContext) {
        return getFunctionName();
    }
}

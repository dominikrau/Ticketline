package at.ac.tuwien.sepm.groupphase.backend.repository.specification;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.EventType;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.ArtistSearch;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.EventSearch;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.ShowSearch;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static at.ac.tuwien.sepm.groupphase.backend.repository.specification.SpecificationUtils.ignoreCaseCriteria;

public class EventSpecification {

    private EventSpecification() {
    }

    /**
     * Creates a Specification for the given Search with which then can be searched in JPA
     * @param search the Parameters to search for
     * @return a specification with the for the given param
     */
    public static Specification<EventEntity> of(final EventSearch search) {
        return combine(
            name(search.getName()),
            description(search.getDescription()),
            eventType(search.getEventType()),
            artist(search.getArtistSearch()),
            show(search.getShowSearch())
        );
    }

    private static Specification<EventEntity> name(final String name) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return ignoreCaseCriteria(
                criteriaBuilder,
                root.get("name"),
                name
            );
        };
    }

    private static Specification<EventEntity> description(final String description) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return ignoreCaseCriteria(
                criteriaBuilder,
                root.get("description"),
                description
            );
        };
    }

    private static Specification<EventEntity> eventType(final EventType type) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return type != null ?
                criteriaBuilder.equal(root.get("eventType"), type.getCode()) :
                null;
        };
    }

    private static Specification<EventEntity> show(final ShowSearch showSearch) {
        return (root, query, criteriaBuilder) -> {
            if (showSearch == null || showSearch.isEmpty()) {
                return null;
            } else {
                query.distinct(true);
                Join<EventEntity, ShowEntity> eventShowJoin = root.joinList("shows");
                final Predicate[] criteria = Stream.of(
                    afterTime(criteriaBuilder, eventShowJoin.get("startTime"), showSearch.getStartTime()),
                    beforeTime(criteriaBuilder, eventShowJoin.get("endTime"), showSearch.getEndTime()),
                    duration(criteriaBuilder, eventShowJoin.get("startTime"), eventShowJoin.get("endTime"), showSearch.getDuration()),
                    locationHall(eventShowJoin, criteriaBuilder, showSearch),
                    price(eventShowJoin, criteriaBuilder, showSearch)
                )
                    .filter(Objects::nonNull)
                    .toArray(Predicate[]::new);
                return criteriaBuilder.and(criteria);
            }
        };
    }

    private static Specification<EventEntity> artist(final ArtistSearch artistSearch) {
        return (root, query, criteriaBuilder) -> {
            if (artistSearch == null || artistSearch.isEmpty()) {
                return null;
            } else {
                query.distinct(true);
                Join<EventEntity, ArtistEntity> eventArtistJoin = root.joinList("artists");
                final Predicate[] criteria = Stream.of(
                    ignoreCaseCriteria(criteriaBuilder, eventArtistJoin.get("firstName"), artistSearch.getFirstName()),
                    ignoreCaseCriteria(criteriaBuilder, eventArtistJoin.get("lastName"), artistSearch.getLastName()),
                    ignoreCaseCriteria(criteriaBuilder, eventArtistJoin.get("pseudonym"), artistSearch.getPseudonym())
                )
                    .filter(Objects::nonNull)
                    .toArray(Predicate[]::new);
                return criteriaBuilder.and(criteria);
            }
        };
    }

    @SafeVarargs
    private static Specification<EventEntity> combine(final Specification<EventEntity>... specifications) {
        if (specifications == null || specifications.length == 0) {
            return (a, b, c) -> null;
        }
        var spec = specifications[0];
        for (int i = 1; i < specifications.length; i++) {
            if (spec != null) {
                spec = spec.and(specifications[i]);
            }
        }
        return spec;
    }

    private static Predicate afterTime(CriteriaBuilder criteriaBuilder, Expression<Timestamp> expression, LocalTime search) {

        Expression<Time> timeInTable = expression.as(Time.class);
        return search != null ?
            criteriaBuilder.greaterThanOrEqualTo(timeInTable, Time.valueOf(search)) :
            null;
    }

    private static Predicate beforeTime(CriteriaBuilder criteriaBuilder, Expression<Timestamp> expression, LocalTime search) {

        Expression<Time> timeInTable = expression.as(Time.class);
        return search != null ?
            criteriaBuilder.lessThanOrEqualTo(timeInTable, Time.valueOf(search)) :
            null;
    }

    private static Predicate duration(CriteriaBuilder criteriaBuilder, Expression<Timestamp> start, Expression<Timestamp> end, Duration search) {
        if (search == null || search.isZero()) {
            return null;
        }
        Expression<String> seconds = new UnitFunctionExpression<>(null, String.class, "second");
        Expression<Long> difference = criteriaBuilder.function("timestampdiff", Long.class, seconds, start, end);

        return criteriaBuilder.between(
            difference,
            search.minusMinutes(30).toSeconds(),
            search.plusMinutes(30).toSeconds()
        );
    }

    private static Predicate price(Join<EventEntity, ShowEntity> path, CriteriaBuilder criteriaBuilder, ShowSearch search) {
        if (search.getMinPrice() == null && search.getMaxPrice() == null) {
            return null;
        }
        Join<EventEntity, PricingEntity> pricings = path.joinList("pricings");
        List<Predicate> predicates = new ArrayList<>();
        if (search.getMinPrice() != null) {
            predicates.add(criteriaBuilder.ge(pricings.get("price"), search.getMinPrice()));
        }
        if (search.getMaxPrice() != null) {
            predicates.add(criteriaBuilder.le(pricings.get("price"), search.getMaxPrice()));
        }
        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }

    private static Predicate locationHall(Join<EventEntity, ShowEntity> path, CriteriaBuilder criteriaBuilder, ShowSearch search) {
        if (search.getLocation() == null && search.getHall() == null) {
            return null;
        }
        Join<EventEntity, VenueEntity> join = path.join("venue");
        var locationPredicate = ignoreCaseCriteria(criteriaBuilder, join.get("name"), search.getLocation());
        Predicate hallPredicate = null;
        if (!StringUtils.isEmpty(search.getHall())) {
            Join<EventEntity, HallEntity> hallJoin = path.join("seatingChart").join("hall");
            hallPredicate = ignoreCaseCriteria(criteriaBuilder, hallJoin.get("name"), search.getHall());
        }
        final Predicate[] criteria = Stream.of(
            locationPredicate,
            hallPredicate
        )
            .filter(Objects::nonNull)
            .toArray(Predicate[]::new);
        return criteriaBuilder.and(criteria);
    }

}

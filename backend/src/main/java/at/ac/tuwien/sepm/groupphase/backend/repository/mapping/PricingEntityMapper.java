package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Pricing;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.PricingEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ShowEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PricingEntityMapper {

    private final SectorEntityMapper sectorEntityMapper;
    private final SeatingChartEntityMapper seatingChartEntityMapper;

    /**
     * Maps the given Pricing Entity to a Domain representation of the Pricing
     *
     * @param pricingEntity the Pricing Entity to be mapped
     * @return the mapped Pricing
     */
    public Pricing toDomain(PricingEntity pricingEntity) {
        return Pricing.builder()
            .id(pricingEntity.getId())
            .price(pricingEntity.getPrice())
            .sector(sectorEntityMapper.toDomain(pricingEntity.getSector()))
            .build();
    }

    /**
     * Maps the given Pricing Entities in a List to a List of Domain representations of the Pricings
     *
     * @param pricingEntities the List of Pricing Entities to be mapped
     * @return the mapped Pricing List
     */
    public List<Pricing> toDomain(List<PricingEntity> pricingEntities) {
        if ( pricingEntities == null ) {
            return Collections.emptyList();
        }

        List<Pricing> list = new ArrayList<>();

        for ( PricingEntity pricingEntity : pricingEntities ) {
            list.add( toDomain(pricingEntity) );
        }

        return list;
    }


    /**
     * Maps the given Pricing and Show to a Repository Entity representation of the Pricing
     *
     * @param pricing the Pricing to be mapped
     * @param show the Show to be mapped
     * @return the mapped Pricing Entity
     */
    public PricingEntity toEntity(Pricing pricing, ShowEntity show) {

        return PricingEntity.builder()
            .id(pricing.getId())
            .price(pricing.getPrice())
            .sector(sectorEntityMapper.toEntity(pricing.getSector(),show.getSeatingChart()))
            .show(show)
            .build();
    }

}

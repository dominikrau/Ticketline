package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Pricing;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.PricingEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.PricingEntityMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.ShowEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PricingRepository {

    private final PricingJpaRepository repository;
    private final PricingEntityMapper mapper;
    private final ShowEntityMapper showEntityMapper;

    /**
     * Save a single Pricing entry in the database
     *
     * @param pricing to save
     * @param show to which the pricing shall be mapped
     * @return saved Pricing entry
     */
    public Pricing savePricing(Pricing pricing, Show show) {
        PricingEntity saved = repository.save(mapper.toEntity(pricing, showEntityMapper.toEntity(show)));
        return mapper.toDomain(saved);
    }

    /**
     * Tries to retrieve a pricing for a specified Show and Sector
     * @param sectorId sector of the show
     * @param showId show whose sector price shall be retrieved
     * @return A Pricing if it exists
     */
    public Optional<Pricing> findPricingBySectorAndShow(Long sectorId, Long showId) {
        return repository.findPricingEntityBySector_IdAndShow_ShowId(sectorId, showId)
            .map(mapper::toDomain);
    }
}

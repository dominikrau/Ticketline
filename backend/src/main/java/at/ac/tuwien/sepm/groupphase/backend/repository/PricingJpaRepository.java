package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.repository.entity.PricingEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ShowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PricingJpaRepository extends JpaRepository<PricingEntity, Long> {

    /**
     * Gets all Pricings for the specified Show
     * @param showEntity Show whose pricings shall be retrieved
     * @return List of the show's pricings
     */
    List<PricingEntity> findAllByShow(ShowEntity showEntity);

    /**
     * Tries to retrieve a pricing for a specified Show and Sector
     * @param sectorId sector of the show
     * @param showId show whose sector price shall be retrieved
     * @return A Pricing if it exists
     */
    Optional<PricingEntity> findPricingEntityBySector_IdAndShow_ShowId(Long sectorId, Long showId);
}

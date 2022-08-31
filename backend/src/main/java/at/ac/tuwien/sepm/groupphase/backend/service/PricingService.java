package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Pricing;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Show;

import java.util.List;

public interface PricingService {

    /**
     * Publish a single Pricing entry
     *
     * @param pricing to publish
     * @param show that pricing is for
     * @return published pricing entry
     */
    Pricing createPricing(Pricing pricing, Show show);

    /**
     * Publish a single Pricing entry
     *
     * @param pricings to publish
     * @param show that pricings are for
     * @return published pricing entries
     */
    List<Pricing> createPricing(List<Pricing> pricings, Show show);
}

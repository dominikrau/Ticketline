package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Pricing;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.PricingRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimplePricingService implements PricingService {

    private final PricingRepository pricingRepository;


    @Override
    public Pricing createPricing(Pricing pricing, Show show) {
        log.debug("Create Pricing, {} for showId {}", pricing, show.getShowId());
        return pricingRepository.savePricing(pricing, show);
    }

    @Override
    public List<Pricing> createPricing(List<Pricing> pricings, Show show) {
        if(pricings == null) {
            return null;
        }

        List<Pricing> list = new ArrayList<>();

        for (Pricing pricing: pricings
             ) {
            list.add(createPricing(pricing,show));
        }

        return list;
    }
}

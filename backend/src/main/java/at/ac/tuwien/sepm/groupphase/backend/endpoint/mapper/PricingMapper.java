package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Pricing;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PricingDto;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PricingMapper {
    private final SectorMapper sectorMapper;

    /**
     * Maps the given Pricing DTO to a Domain representation of the Pricing
     *
     * @param pricingDto the Pricing DTO to be mapped
     * @return the mapped Pricing
     */
    public  Pricing toDomain(PricingDto pricingDto) {
        return Pricing.builder()
            .id(pricingDto.getId())
            .sector(sectorMapper.toDomain(pricingDto.getSector()))
            .price(pricingDto.getPrice())
            .build();
    }

    /**
     * Maps the given Pricings DTOs in a List to a List of Domain representations of the Pricings
     *
     * @param pricingDtos the List of Pricing DTOs to be mapped
     * @return the mapped Pricing List
     */
    @Named("pricings")
    List<Pricing> toDomain(List<PricingDto> pricingDtos) {
        if (pricingDtos == null) {
            return Collections.emptyList();
        }

        List<Pricing> list = new ArrayList<>();

        for (PricingDto pricingDto : pricingDtos ) {
            list.add(toDomain(pricingDto));
        }

        return list;
    }


    /**
     * Maps the given Pricing to a Data Transfer Object representation of the Pricing
     *
     * @param pricing the Pricing to be mapped
     * @return the mapped Pricing DTO
     */
    public PricingDto toDto(Pricing pricing) {
        return PricingDto.builder()
            .id(pricing.getId())
            .sector(sectorMapper.toDto(pricing.getSector()))
            .price(pricing.getPrice())
            .build();
    }

    /**
     * Maps the given Pricings in a List to a List of Data Transfer Object representations of the Pricings
     *
     * @param pricings the List of Pricings to be mapped
     * @return the mapped Pricing DTO List
     */
    List<PricingDto> toDto(List<Pricing> pricings) {
        if (pricings == null) {
            return Collections.emptyList();
        }

        List<PricingDto> list = new ArrayList<>();

        for (Pricing pricing : pricings) {
            list.add(toDto(pricing));
        }

        return list;
    }

}

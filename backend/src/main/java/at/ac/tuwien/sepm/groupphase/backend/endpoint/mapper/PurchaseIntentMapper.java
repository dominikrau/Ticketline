package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.PurchaseIntent;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.StandingPurchaseIntent;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PurchaseIntentDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StandingPurchaseIntentDto;
import org.mapstruct.Mapper;

@Mapper(uses = PaymentMapper.class)
public interface PurchaseIntentMapper {

    /**
     * Maps the given Purchase Intent DTO to a Domain representation of the Purchase Intent
     *
     * @param purchaseIntentDto the Purchase Intent DTO to be mapped
     * @return the mapped Purchase Intent
     */
    PurchaseIntent toDomain(PurchaseIntentDto purchaseIntentDto);

    /**
     * Maps the given Standing Purchase Intent DTO to a Domain representation of the Standing Purchase Intent
     *
     * @param standingPurchaseIntentDto the Standing Purchase Intent DTO to be mapped
     * @return the mapped Standing Purchase Intent
     */
    StandingPurchaseIntent toDomain(StandingPurchaseIntentDto standingPurchaseIntentDto);

}

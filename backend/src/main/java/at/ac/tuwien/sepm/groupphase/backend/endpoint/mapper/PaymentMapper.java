package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Payment;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.PaymentMethod;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = PaymentMethod.class)
public interface PaymentMapper {

    /**
     * Maps the given Payment DTO to a Domain representation of the Payment
     *
     * @param paymentDto the Payment DTO to be mapped
     * @return the mapped Payment
     */
    @Mapping(target = "method", source = "method", qualifiedByName = "fromCode")
    Payment toDomain(PaymentDto paymentDto);

}

package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class PricingDto {

    Long id;
    @NotNull
    @Min(value = 0)
    Double price;
    @NotNull
    @Valid
    SectorDto sector;

    @JsonCreator
    public PricingDto(
        @JsonProperty("id") Long id,
        @JsonProperty("price") @NotNull Double price,
        @JsonProperty("sector") @Valid @NotNull SectorDto sector
    ){
        this.id = id;
        this.price = price;
        this.sector = sector;
    }
}

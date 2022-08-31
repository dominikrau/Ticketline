package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class AddressDto {

    @NotBlank
    @Size(max = 255)
    String country;
    @NotBlank
    @Size(max = 255)
    String city;
    @NotBlank
    @Size(max = 255)
    String postalCode;
    @NotBlank
    @Size(max = 255)
    String street;
    @Size(max = 255)
    String additional;

    @JsonCreator
    public AddressDto(
        @NotBlank @JsonProperty("country") String country,
        @NotBlank @JsonProperty("city") String city,
        @NotBlank @JsonProperty("postalCode") String postalCode,
        @NotBlank @JsonProperty("street") String street,
        @JsonProperty("additional") String additional
    ) {
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.additional = additional;
    }
}

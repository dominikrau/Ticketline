package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class Address {

    String country;
    String city;
    String postalCode;
    String street;
    String additional;

}

package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;

import java.util.Arrays;

@RequiredArgsConstructor
public enum PaymentMethod {

    CREDIT_CARD("creditcard"),
    PAY_PAL("paypal"),
    BANK_TRANSFER("banktransfer"),
    RESERVATION("reservation");

    private final String code;

    /**
     * converts the String Code into the Payment Method from the enumeration
     * @param code the string describing the Payment Method
     * @return the ENUM describing the Payment Method
     */
    @Named("fromCode")
    public static PaymentMethod fromCode(String code) {
        return Arrays.stream(values())
            .filter(a -> a.code.equals(code))
            .findFirst()
            .orElse(null);
    }

}

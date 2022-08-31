package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TicketStatus {

    RESERVED("RESERVED"),
    BOUGHT("BOUGHT"),
    CANCELLED("CANCELLED");

    private final String code;

    /**
     * converts the String Code into the Ticket Status from the enumeration
     * @param code the string describing the Ticket Status
     * @return the ENUM describing the Ticket Status
     */
    public static TicketStatus fromCode(String code) {
        return Arrays.stream(values())
            .filter(ticketStatus -> code.equals(ticketStatus.getCode()))
            .findFirst()
            .orElse(null);
    }

    /**
     * Checks if the Ticket Status is Reserved or Bought, depending on the Payment Method
     * If the Payment Method is "Reservation", the Ticket Status is Reserved. Otherwise the Ticket Status is Bought.
     *
     * @param paymentMethod the provided Method of Payment
     * @return The Ticket Status. Either Reserved or Bought
     */
    public static TicketStatus fromPaymentMethod(final PaymentMethod paymentMethod) {
        return paymentMethod == PaymentMethod.RESERVATION ? TicketStatus.RESERVED : TicketStatus.BOUGHT;
    }
}

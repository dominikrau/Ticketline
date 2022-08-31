package at.ac.tuwien.sepm.groupphase.backend.domain;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import com.itextpdf.text.pdf.PdfPTable;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Pdf Creator for a Cancellation Invoice of a specific Ticket
 */
public class CancellationPdfCreator extends BasicReceiptPdfCreator {

    private final Ticket ticket;

    @SneakyThrows
    public CancellationPdfCreator(Ticket ticket) {
        super();
        if (ticket.getStatus() != TicketStatus.CANCELLED) {
            throw new ValidationException("Trying to get Cancellation invoice for a Ticket that wasn't cancelled");
        }
        this.ticket = ticket;
    }


    @Override
    protected int orderId() {
        return ticket.getTicketId().intValue();
    }

    @Override
    protected ApplicationUser getUser() {
        return ticket.getUser();
    }

    @Override
    protected void orderSummary(PdfPTable table) {
        table.addCell(ticket.getShow().getEvent().getName() + ": " + System.lineSeparator() + "Sector " + ticket.getSector().getName());
        table.addCell("1");
        table.addCell("€ -" + BigDecimal.valueOf(ticket.getPrice()).setScale(2, RoundingMode.HALF_UP).toString());
        table.addCell("20 %");
    }

    @Override
    protected BigDecimal totalCost() {
        return BigDecimal.valueOf(ticket.getPrice() * -1);
    }

    @Override
    protected LocalDate orderDate() {
        return LocalDate.now();
    }

    @Override
    protected String title() {
        return "Cancellation";
    }
}

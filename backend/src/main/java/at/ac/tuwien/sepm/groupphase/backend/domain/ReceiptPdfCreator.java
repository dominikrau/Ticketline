package at.ac.tuwien.sepm.groupphase.backend.domain;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Order;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import com.itextpdf.text.pdf.PdfPTable;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * PdfCreator for a Receipt for a given Order and its Tickets
 */
public class ReceiptPdfCreator extends BasicReceiptPdfCreator {

    private final Order order;
    private final List<Ticket> tickets;

    @SneakyThrows
    public ReceiptPdfCreator(Order order, List<Ticket> tickets) {
        super();
        if (tickets.stream().anyMatch(ticket -> !ticket.getTicketOrder().equals(order))) {
            throw new ValidationException("Trying to print Tickets from different Order");
        }
        this.order = order;
        this.tickets = tickets;
    }

    @Override
    protected int orderId() {
        return order.getOrderId().intValue();
    }

    @Override
    protected ApplicationUser getUser() {
        return order.getUser();
    }

    @Override
    protected void orderSummary(PdfPTable table) {
        for (Ticket ticket : tickets) {
            table.addCell(ticket.getShow().getEvent().getName() + ": " + System.lineSeparator() + "Sector " + ticket.getSector().getName());
            table.addCell("1");
            table.addCell("€" + BigDecimal.valueOf(ticket.getPrice()).setScale(2, RoundingMode.HALF_UP).toString());
            table.addCell("20 %");
        }
    }

    @Override
    protected BigDecimal totalCost() {
        return tickets.stream()
            .map(Ticket::getPrice)
            .map(BigDecimal::valueOf)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    protected LocalDate orderDate() {
        return LocalDate.ofInstant(order.getOrderDate(), ZoneId.systemDefault());
    }

    @Override
    protected String title() {
        return "Order";
    }
}

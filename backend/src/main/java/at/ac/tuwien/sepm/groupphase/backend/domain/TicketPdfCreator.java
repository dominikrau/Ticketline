package at.ac.tuwien.sepm.groupphase.backend.domain;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.itextpdf.text.Font.FontFamily.HELVETICA;

/**
 * PdfCreator for creating Tickets as a Pdf
 */
public class TicketPdfCreator extends PdfCreator {

    private final BaseFont baseFont;
    private final List<Ticket> tickets;

    @SneakyThrows
    public TicketPdfCreator(List<Ticket> tickets) {
        if (tickets.isEmpty()) {
            throw new ValidationException("Trying to create Pdf for Tickets but no Tickets there.");
        }
        this.tickets = tickets;
        baseFont = BaseFont.createFont();
    }


    @Override
    protected List<Element> elements() {
        return tickets.stream()
            .filter(ticket -> ticket.getStatus() == TicketStatus.BOUGHT)
            .map(this::ticketPage)
            .collect(Collectors.toList());
    }

    @SneakyThrows
    private Element ticketPage(final Ticket ticket) {
        Section sector = new Chapter(ticket.getTicketId().intValue());
        sector.add(qrCode(ticket));
        sector.add(createShowField(ticket.getShow()));
        sector.add(createSectorField(ticket.getSector()));
        if (ticket.getSeat() != null) {
            Paragraph paragraph = new Paragraph();
            paragraph.add(new Chunk("Row: " + ticket.getSeat().getY()));
            paragraph.add(new Chunk(", "));
            paragraph.add(new Chunk("Seat: " + ticket.getSeat().getX()));
            sector.add(paragraph);
        }
        sector.add(new Chunk("Price: €" + BigDecimal.valueOf(ticket.getPrice()).setScale(2, RoundingMode.HALF_UP).toString(), new Font(HELVETICA, 14, Font.BOLD)));
        return sector;
    }


    private Element createShowField(final Show show) {
        Paragraph showParagraph = new Paragraph(show.getEvent().getName() + System.lineSeparator(), new Font(HELVETICA, 22, Font.BOLD));
        showParagraph.add(new Chunk(show.getVenue().getName(), new Font(baseFont)));
        showParagraph.add(new Chunk(" - ", new Font(baseFont)));
        showParagraph.add(new Chunk(show.getSeatingChart().getHall().getName(), new Font(baseFont)));
        showParagraph.add(createAddressField(show.getVenue().getAddress()));
        showParagraph.add(new Chunk(format(show.getStartTime()) + " - " + format(show.getEndTime())));
        return showParagraph;
    }

    private Element createAddressField(Address address) {
        Paragraph addressParagraph = new Paragraph("Address: " + System.lineSeparator());
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add(address.getStreet() + " " + (address.getAdditional() != null ? address.getAdditional() : ""));
        joiner.add(address.getCountry());
        joiner.add(address.getPostalCode() + " " + address.getCity());
        addressParagraph.add(new Chunk(joiner.toString()));
        return addressParagraph;
    }

    private Element createSectorField(Sector sector) {
        Paragraph sectorParagraph = new Paragraph();
        sectorParagraph.add(new Chunk(System.lineSeparator()));
        sectorParagraph.add(new Chunk(sector.getName(), new Font(baseFont)));
        sectorParagraph.add(new Chunk(System.lineSeparator()));
        sectorParagraph.add(new Chunk(sector.getTypeDescription(), new Font(HELVETICA, 14, Font.ITALIC)));
        return sectorParagraph;
    }

    private String format(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    @SneakyThrows
    private Image qrCode(final Ticket ticket) {
        StringJoiner builder = new StringJoiner(";");
        builder.add(ticket.getTicketId().toString());
        builder.add(ticket.getShow().getEvent().getName());
        builder.add(ticket.getShow().getVenue().getName());
        builder.add(ticket.getPrice().toString());
        ByteArrayOutputStream qrImageStream = new ByteArrayOutputStream();
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(builder.toString(), BarcodeFormat.QR_CODE, 200, 200);
        MatrixToImageWriter.writeToStream(bitMatrix, "png", qrImageStream);
        return Image.getInstance(qrImageStream.toByteArray());
    }

}

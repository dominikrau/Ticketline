package at.ac.tuwien.sepm.groupphase.backend.domain;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Address;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class BasicReceiptPdfCreator extends PdfCreator {

    private static final ApplicationUser COMPANY_USER = ApplicationUser.builder()
        .firstName("Tour of")
        .lastName("Heros")
        .address(Address.builder()
            .street("Karlsplatz 3")
            .postalCode("1010")
            .city("Vienna")
            .country("Austria")
            .build())
        .build();
    protected final BaseFont baseFont;

    @SneakyThrows
    public BasicReceiptPdfCreator() {
        baseFont = BaseFont.createFont();
    }

    @Override
    protected List<Element> elements() {
        Section orderPage = new Chapter(orderId());
        orderPage.add(new Paragraph(title(), new Font(baseFont, 20, Font.BOLD)));
        orderPage.add(new Paragraph(System.lineSeparator()));
        orderPage.add(user(COMPANY_USER));
        orderPage.add(new Paragraph(System.lineSeparator()));
        orderPage.add(user(getUser()));
        orderPage.add(new Paragraph(System.lineSeparator()));
        orderPage.add(new Paragraph("Order number: " + orderId()));
        orderPage.add(new Paragraph("Order date: " + orderDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        orderPage.add(orderSummary());
        orderPage.add(new Paragraph(System.lineSeparator()));
        orderPage.add(cost());
        return List.of(orderPage);
    }

    protected abstract int orderId();

    protected abstract ApplicationUser getUser();

    protected abstract void orderSummary(PdfPTable table);

    protected abstract BigDecimal totalCost();

    protected abstract LocalDate orderDate();

    protected abstract String title();

    private Element orderSummary() {
        final Paragraph paragraph = new Paragraph("Order Summary:", new Font(baseFont, 20));
        PdfPTable table = new PdfPTable(4);
        paragraph.add(table);
        table.addCell("Articles");
        table.addCell("Amount");
        table.addCell("Price");
        table.addCell("Tax %");
        orderSummary(table);
        return paragraph;
    }

    private Element user(ApplicationUser user) {
        Paragraph paragraph = new Paragraph();
        paragraph.add(user.getFirstName() + " " + user.getLastName());
        paragraph.add(address(user.getAddress()));
        return paragraph;
    }

    private Element address(Address address) {
        Paragraph addressParagraph = new Paragraph();
        addressParagraph.add(new Paragraph(address.getStreet() + " " + (address.getAdditional() != null ? address.getAdditional() : "")));
        addressParagraph.add(new Paragraph(address.getPostalCode() + " " + address.getCity() + ", " + address.getCountry()));
        return addressParagraph;
    }


    private Element cost() {
        BigDecimal totalCost = totalCost();
        BigDecimal amountBeforeTax = totalCost.divide(BigDecimal.valueOf(1.2), RoundingMode.HALF_UP);
        BigDecimal tax = totalCost.subtract(amountBeforeTax);
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph("Before Tax: €" + amountBeforeTax.setScale(2, RoundingMode.HALF_UP).toString(), new Font(baseFont, 14)));
        paragraph.add(new Paragraph("Tax: €" + tax.setScale(2, RoundingMode.HALF_UP).toString(), new Font(baseFont, 14)));
        paragraph.add(new Paragraph("Total Cost: €" + totalCost.setScale(2, RoundingMode.HALF_UP).toString(), new Font(baseFont, 14)));
        return paragraph;
    }

}

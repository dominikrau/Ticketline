package at.ac.tuwien.sepm.groupphase.backend.domain;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;

public abstract class PdfCreator {

    /**
     * Creates a Pdf as a byte[]. The Content of the Pdf is determined by
     * the Actual Implementation of this Abstract class
     * @return A Pdf as a byte[]
     * @throws DocumentException if there is a Problem during creation of the Pdf
     */
    public byte[] createPdf() throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        for (Element element : elements()) {
            document.add(element);
        }

        document.close();
        return outputStream.toByteArray();
    }

    /**
     * Returns the Content of the Pdf that should be written
     * @return the Content as a List of Itext Elements
     */
    protected abstract List<Element> elements();

}

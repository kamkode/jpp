import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class IDCardGenerator {
    private static final int CARD_WIDTH = 300; // Width of the ID card in points
    private static final int CARD_HEIGHT = 200; // Height of the ID card in points
    private static final float MARGIN = 50; // Margin between ID cards in points
    private static final float FONT_SIZE = 12; // Font size for text fields

    public static void main(String[] args) {
        String csvFilePath = "path/to/your/csv/file.csv";
        String outputPdfFilePath = "path/to/your/output/file.pdf";

        try {
            List<String[]> data = CSVUtils.readCSV(csvFilePath);

            PDDocument document = new PDDocument();

            for (String[] row : data) {
                PDPage page = new PDPage(new PDRectangle(CARD_WIDTH, CARD_HEIGHT));
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE);

                // Adjust the starting point for text positioning
                float textX = MARGIN;
                float textY = CARD_HEIGHT - MARGIN;

                // Draw the fields on the ID card
                contentStream.beginText();
                contentStream.newLineAtOffset(textX, textY);
                contentStream.showText("Phone Number: " + row[0]);
                contentStream.newLineAtOffset(0, -FONT_SIZE);
                contentStream.showText("Email ID: " + row[1]);
                contentStream.newLineAtOffset(0, -FONT_SIZE);
                contentStream.showText("Position: " + row[2]);
                contentStream.endText();

                // Add the image to the ID card
                File imageFile = new File(row[3]);
                if (imageFile.exists()) {
                    float imageX = MARGIN;
                    float imageY = MARGIN;
                    PDPageContentStream imageContentStream = new PDPageContentStream(document, page,
                            PDPageContentStream.AppendMode.APPEND, true, true);
                    imageContentStream.drawImage(PDImageXObject.createFromFileByExtension(imageFile, document),
                            imageX, imageY);
                    imageContentStream.close();
                }

                contentStream.close();
            }

            document.save(outputPdfFilePath);
            document.close();

            System.out.println("ID cards generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

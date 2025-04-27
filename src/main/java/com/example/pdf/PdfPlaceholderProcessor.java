package com.example.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PdfPlaceholderProcessor {

    public static void main(String[] args) {
        try {
            // Load the DOCX file from resources folder
            InputStream inputStream = PdfPlaceholderProcessor.class.getClassLoader().getResourceAsStream("file/notes.docx");
            if (inputStream == null) {
                throw new FileNotFoundException("Template file not found in resources!");
            }
            XWPFDocument doc = new XWPFDocument(inputStream);

            // Create placeholder map
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("[name]", "John Doe");
            placeholders.put("[age]", "38");
            placeholders.put("[location]", "New York");
            placeholders.put("[signature]", "John's Signature");

            // Prepare PDF document
            PDDocument pdfDocument = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            pdfDocument.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setLeading(18f); // line height
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 750); // Starting position

            float yPosition = 750; // track Y for signature placement

            for (IBodyElement bodyElement : doc.getBodyElements()) {
                if (bodyElement instanceof XWPFParagraph) {
                    XWPFParagraph paragraph = (XWPFParagraph) bodyElement;
                    String paragraphText = getProcessedText(paragraph, placeholders);

                    if (paragraph.getStyle() != null && paragraph.getStyle().equals("ListParagraph")) {
                        // For bullet points
                        contentStream.showText("\u2022 " + paragraphText);
                    } else {
                        contentStream.showText(paragraphText);
                    }
                    contentStream.newLine();
                    yPosition -= 18f; // moving down
                } else if (bodyElement instanceof XWPFTable) {
                    // You can extend this block if you want table support too
                }
            }

            contentStream.endText();

            // Now draw the signature aligned to the right
            String signature = placeholders.get("[signature]");
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            float pageWidth = page.getMediaBox().getWidth();
            float textWidth = PDType1Font.HELVETICA.getStringWidth(signature) / 1000 * 12;
            float startX = pageWidth - textWidth - 50;
            float signatureY = yPosition - 30;

            contentStream.newLineAtOffset(startX, signatureY);
            contentStream.showText(signature);
            contentStream.endText();

            contentStream.close();
            inputStream.close();

            // Save the PDF
            pdfDocument.save("output.pdf");
            pdfDocument.close();

            System.out.println("PDF generated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getProcessedText(XWPFParagraph paragraph, Map<String, String> placeholders) {
        StringBuilder fullText = new StringBuilder();
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                    text = text.replace(entry.getKey(), entry.getValue());
                }
                fullText.append(text);
            }
        }
        return fullText.toString();
    }
}

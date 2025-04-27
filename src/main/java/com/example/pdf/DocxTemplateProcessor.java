package com.example.pdf;

import org.apache.poi.xwpf.usermodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DocxTemplateProcessor {

    public static void main(String[] args) {
        try {
            // Load the template.docx from resources using ClassLoader
            InputStream inputStream = DocxTemplateProcessor.class.getClassLoader().getResourceAsStream("file/notes.docx");
            if (inputStream == null) {
                throw new FileNotFoundException("Template file not found in resources!");
            }

            XWPFDocument doc = new XWPFDocument(inputStream);

            // Create a map with placeholder values
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("[name]", "John Doe");
            placeholders.put("[age]", "2025-04-27");
            placeholders.put("[location]", "$1500");
            placeholders.put("[signature]","ak");

            // Replace placeholders in paragraphs
            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                replacePlaceholders(paragraph, placeholders);
            }

            // Replace placeholders in tables
            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            replacePlaceholders(paragraph, placeholders);
                        }
                    }
                }
            }

            // Save the document with replaced values
            String outputFilePath = "output.docx"; // /tmp is writable in AWS Lambda
            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)) {
                doc.write(fileOutputStream);
            }

            inputStream.close(); // Always close streams

            System.out.println("Document processed successfully and saved to ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to replace placeholders in a paragraph
    private static void replacePlaceholders(XWPFParagraph paragraph, Map<String, String> placeholders) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                    if (text.contains(entry.getKey())) {
                        text = text.replace(entry.getKey(), entry.getValue());
                        run.setText(text, 0);
                    }
                }
            }
        }
    }
}

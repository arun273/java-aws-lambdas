package com.example.html.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class HtmlToPdfStatic {

    public static void main(String[] args) throws IOException {
        // Path to the HTML file in resources folder
        String htmlFilePath = "/file/Java 8 Concepts.html"; // Adjust the file name if needed

        // Load HTML from resources folder
        String htmlContent = loadHtmlFromResources(htmlFilePath);

        // Specify output PDF file path
        String outputPdfPath = "output2.pdf";

        // Convert HTML to PDF
        convertHtmlToPdf(htmlContent, outputPdfPath);

        System.out.println("PDF created successfully at: " + outputPdfPath);
    }

    /**
     * Loads the HTML content from the resources folder.
     * @param htmlFilePath the path to the HTML file inside resources folder
     * @return the content of the HTML file as a string
     * @throws IOException if there is an issue reading the file
     */
    private static String loadHtmlFromResources(String htmlFilePath) throws IOException {
        InputStream inputStream = HtmlToPdfStatic.class.getResourceAsStream(htmlFilePath);

        if (inputStream == null) {
            throw new FileNotFoundException("HTML file not found in resources: " + htmlFilePath);
        }

        // Convert the InputStream to a String
        StringBuilder htmlContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line).append("\n");
            }
        }
        return htmlContent.toString();
    }

    /**
     * Converts HTML string content into a PDF and saves it to the specified path.
     * @param htmlContent the HTML content as a string
     * @param outputPdfPath the path to save the output PDF
     */
    private static void convertHtmlToPdf(String htmlContent, String outputPdfPath) {
        try (OutputStream os = new FileOutputStream(outputPdfPath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null); // No base URI used here
            builder.toStream(os);
            builder.run();
        } catch (IOException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }
}

package com.example.html.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HtmlToPdfService {

    private HtmlToPdfService() {
    }

    public static void generatePdf(String templatePath, Map<String, String> placeholders, OutputStream outputStream) throws IOException {
        String html = loadHtmlTemplate(templatePath);
        String finalHtml = replacePlaceholders(html, placeholders);
        renderHtmlToPdf(finalHtml, outputStream);
    }

    private static String loadHtmlTemplate(String resourcePath) throws IOException {
        InputStream is = HtmlToPdfService.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) throw new FileNotFoundException("Template not found: " + resourcePath);
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private static String replacePlaceholders(String html, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            html = html.replace(entry.getKey(), entry.getValue());
        }
        return html;
    }

    private static void renderHtmlToPdf(String html, OutputStream outputStream) throws IOException {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();
    }
}

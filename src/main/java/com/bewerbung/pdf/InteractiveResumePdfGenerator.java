package com.bewerbung.pdf;

import com.bewerbung.model.ResumeData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InteractiveResumePdfGenerator {

    private static final PDFont TITLE_FONT = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    private static final PDFont HEADER_FONT = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    private static final PDFont NORMAL_FONT = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    
    private static final float TITLE_FONT_SIZE = 22f;
    private static final float HEADER_FONT_SIZE = 14f;
    private static final float NORMAL_FONT_SIZE = 11f;
    private static final float MARGIN = 60f;
    private static final float LINE_HEIGHT = 15f;
    
    public static void create(ResumeData data, String aiText) throws IOException {
        Files.createDirectories(Paths.get("output"));
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Set up the page dimensions
                float pageWidth = page.getMediaBox().getWidth();
                float pageHeight = page.getMediaBox().getHeight();
                float yPosition = pageHeight - MARGIN;
                
                // Add title
                yPosition = addText(contentStream, data.name(), TITLE_FONT, TITLE_FONT_SIZE, 
                                  MARGIN, yPosition, pageWidth - 2 * MARGIN);
                
                // Add contact information
                yPosition = addSection(contentStream, "Contact Information", 
                                     data.email() + " | " + data.telefon(), 
                                     MARGIN, yPosition - 20, pageWidth - 2 * MARGIN);
                
                // Add skills
                yPosition = addSection(contentStream, "Skills", 
                                     String.join(", ", data.skills()), 
                                     MARGIN, yPosition - 20, pageWidth - 2 * MARGIN);
                
                // Add AI-generated content
                addSection(contentStream, "Professional Summary", aiText, 
                         MARGIN, yPosition - 20, pageWidth - 2 * MARGIN);
            }
            
            // Save the document
            String filename = "output/resume_" + data.name().replaceAll("\\s+", "_") + ".pdf";
            document.save(filename);
            System.out.println("Resume saved as: " + filename);
        }
    }
    
    private static float addSection(PDPageContentStream contentStream, 
                                  String title, String content,
                                  float x, float y, float width) throws IOException {
        // Add section title
        float yPosition = addText(contentStream, title, HEADER_FONT, HEADER_FONT_SIZE, x, y, width);
        
        // Add section content
        return addText(contentStream, content, NORMAL_FONT, NORMAL_FONT_SIZE, 
                      x, yPosition - 10, width);
    }
    
    private static float addText(PDPageContentStream contentStream, String text,
                               PDFont font, float fontSize,
                               float x, float y, float width) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float lineHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize * 1.05f;
        float currentY = y;
        
        for (String word : words) {
            String newLine = line.length() > 0 ? line + " " + word : word;
            float lineWidth = font.getStringWidth(newLine) / 1000 * fontSize;
            
            if (lineWidth > width && line.length() > 0) {
                contentStream.showText(line.toString());
                contentStream.newLineAtOffset(0, -lineHeight);
                currentY -= lineHeight;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(newLine);
            }
        }
        
        // Add the last line if not empty
        if (line.length() > 0) {
            contentStream.showText(line.toString());
        }
        
        contentStream.endText();
        return currentY - lineHeight;
    }
}

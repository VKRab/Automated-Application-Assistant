package com.bewerbung.io;

import com.bewerbung.model.ResumeData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConfigWriter {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);
    
    private static final String OUTPUT_DIR = "output";
    
    public static void save(ResumeData data, String aiText) throws IOException {
        // Create output directory if it doesn't exist
        Files.createDirectories(Paths.get(OUTPUT_DIR));
        
        // Generate timestamp for filename
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        
        // Create a configuration object to hold both data and AI text
        var config = new ConfigData(
                data,
                aiText,
                LocalDateTime.now()
        );
        
        // Write to JSON file
        String filename = String.format("%s/resume_%s_%s.json", 
                OUTPUT_DIR, 
                data.name().replaceAll("\\s+", "_"),
                timestamp);
        
        objectMapper.writeValue(Paths.get(filename).toFile(), config);
        System.out.println("Configuration saved to: " + filename);
        
        // Also save the AI text as a separate text file
        String textFilename = String.format("%s/resume_%s_%s.txt",
                OUTPUT_DIR,
                data.name().replaceAll("\\s+", "_"),
                timestamp);
        
        Files.writeString(Paths.get(textFilename), aiText);
        System.out.println("AI text saved to: " + textFilename);
    }
    
    // Inner class to hold both the resume data and AI text
    private record ConfigData(
            ResumeData resumeData,
            String aiText,
            LocalDateTime generatedAt
    ) {}
}

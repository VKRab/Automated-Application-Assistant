package com.bewerbung.ai;

import com.bewerbung.model.ResumeData;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class GeminiService {

    private static final String MODEL = "gemini-2.5-flash";
    private static final String API_KEY;
    
    static {
        // Get API key from environment variable
        API_KEY = System.getenv("GEMINI_API_KEY");
        
        // Verify API key
        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            System.err.println("ERROR: GEMINI_API_KEY environment variable is not set.");
            System.err.println("Please set it in your Run/Debug Configurations in IntelliJ:");
            System.err.println("1. Go to Run -> Edit Configurations");
            System.err.println("2. Select your application");
            System.err.println("3. Under 'Environment variables', add: GEMINI_API_KEY=your_api_key_here");
            System.err.println("4. Apply and OK");
            System.exit(1);
        }
        
        System.out.println("Successfully loaded Gemini API key");
    }
    
    private static String getApiUrl() {
        return "https://generativelanguage.googleapis.com/v1beta/models/" + MODEL + ":generateContent?key=" + API_KEY;
    }

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String generateResumeText(ResumeData data) {
        try {
            String prompt = """
                Erstelle einen kurzen, professionellen Lebenslaufabschnitt für eine Ausbildung zum Fachinformatiker.
                Name: %s, Alter: %d, Skills: %s.
                Zusätzliche Anweisung: %s
                Antworte nur mit dem fertigen Text.
                """.formatted(data.name(), data.alter(), data.skills(), data.customPrompt());

            String json = """
                {
                  "contents":[{"parts":[{"text":"%s"}]}],
                  "generationConfig":{"temperature":0.7,"maxOutputTokens":800}
                }
                """.formatted(prompt.replace("\"", "\\\""));

            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
            Request req = new Request.Builder().url(getApiUrl()).post(body).build();

            try (Response resp = CLIENT.newCall(req).execute()) {
                String raw = resp.body().string();
                return MAPPER.readTree(raw)
                        .at("/candidates/0/content/parts/0/text")
                        .asText();
            }
        } catch (Exception e) {
            throw new RuntimeException("Gemini fehler: " + e.getMessage(), e);
        }
    }
}
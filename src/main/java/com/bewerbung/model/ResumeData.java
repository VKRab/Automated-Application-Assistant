package com.bewerbung.model;

import java.util.List;

public record ResumeData(
    String name,
    int alter,
    String email,
    String telefon,
    List<String> skills,
    String customPrompt,
    ResumeStyle style
) {
    public String skillsAsString() {
        return String.join(", ", skills);
    }
}

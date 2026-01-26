package com.bewerbung;

import com.bewerbung.model.ResumeStyle;
import java.util.List;

public record ResumeData (
    String name,
    int alter,
    String email,
    String telefon,
    List<String> skills,
    String customPrompt,
    ResumeStyle style
){}
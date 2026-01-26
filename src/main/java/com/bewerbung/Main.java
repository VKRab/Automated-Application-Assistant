/*
com.bewerbung
├── Main.java                    // ← точка входа, выбор режима
├── core
│   ├── JobAnschreibenWizard.java   // старый flow (URL → PDF)
│   └── InteractiveResumeWizard.java // новый flow (custom prompt → styled PDF)
├── model
│   ├── ResumeData.java          // POJO с данными
│   └── ResumeStyle.java         // enum стилей
├── pdf
│   ├── AnschreibenPdfGenerator.java  // старый генератор
│   └── InteractiveResumePdfGenerator.java // новый
├── ai
│   └── GeminiService.java       // общий клиент Gemini
└── io
    └── ConfigWriter.java        // сохраняет .conf файл
это новыя струкртура файла HR2.0
*/
package com.bewerbung;

import com.bewerbung.core.InteractiveResumeWizard;
import com.bewerbung.core.JobAnschreibenWizard;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("   BEWERBUNG HELFER v2.0      ");
        System.out.println("==============================");
        System.out.println("1 – Klassischer Modus (URL → Anschreiben)");
        System.out.println("2 – Interaktiver Lebenslauf-Builder");
        System.out.print("> ");

        switch (new Scanner(System.in).nextLine().trim()) {
            case "1" -> JobAnschreibenWizard.start();
            case "2" -> InteractiveResumeWizard.start();
            default -> System.out.println("❌ Ungültig.");
        }
    }
}
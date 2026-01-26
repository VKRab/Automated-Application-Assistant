//core
package com.bewerbung.core;

import com.bewerbung.ai.GeminiService;
import com.bewerbung.io.ConfigWriter;
import com.bewerbung.model.ResumeData;
import com.bewerbung.model.ResumeStyle;
import com.bewerbung.pdf.InteractiveResumePdfGenerator;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class InteractiveResumeWizard {

    public static void start() {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Alter: ");
            int alter = Integer.parseInt(sc.nextLine().trim());

            System.out.print("E-Mail: ");
            String email = sc.nextLine().trim();

            System.out.print("Telefon: ");
            String telefon = sc.nextLine().trim();

            System.out.print("Skills (komma-getrennt): ");
            List<String> skills = Arrays.stream(sc.nextLine().split(","))
                    .map(String::trim)
                    .filter(skill -> !skill.isEmpty())
                    .toList();

            System.out.print("Eigenes Prompt (Leer für Default): ");
            String prompt = sc.nextLine().trim();
            if (prompt.isEmpty()) {
                prompt = "Erstelle einen kurzen, professionellen Lebenslaufabschnitt.";
            }

            System.out.println("Stil:");
            for (ResumeStyle s : ResumeStyle.values()) {
                System.out.println((s.ordinal() + 1) + " – " + s);
            }
            ResumeStyle style = ResumeStyle.values()[Integer.parseInt(sc.nextLine()) - 1];

            ResumeData data = new ResumeData(name, alter, email, telefon, skills, prompt, style);

            try {
                String aiText = GeminiService.generateResumeText(data);
                ConfigWriter.save(data, aiText);
                InteractiveResumePdfGenerator.create(data, aiText);
                System.out.println("✅ Fertig! Siehe output/");
            } catch (IOException e) {
                System.err.println("Fehler beim Erstellen des Lebenslaufs: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            System.err.println("Ungültige Eingabe für Alter oder Stil. Bitte geben Sie eine Zahl ein.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Ungültige Stil-Auswahl. Bitte wählen Sie eine der angegebenen Nummern.");
        } catch (Exception e) {
            System.err.println("Ein unerwarteter Fehler ist aufgetreten: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
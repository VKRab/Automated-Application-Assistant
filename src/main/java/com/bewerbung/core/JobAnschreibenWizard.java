package com.bewerbung.core;

import com.bewerbung.ai.GeminiService;
import com.bewerbung.model.ResumeData;
import com.bewerbung.model.ResumeStyle;

import java.util.Scanner;
import java.util.List;
import java.util.Arrays;

public class JobAnschreibenWizard {
    public static void start() {
        System.out.println("Job Application Letter Wizard");
        System.out.println("----------------------------");
        
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Job Title: ");
        String jobTitle = sc.nextLine().trim();
        
        System.out.print("Company Name: ");
        String company = sc.nextLine().trim();
        
        System.out.print("Your Name: ");
        String name = sc.nextLine().trim();
        
        System.out.print("Your Skills (comma separated): ");
        String skills = sc.nextLine().trim();
        
        // Convert comma-separated skills to List<String>
        List<String> skillsList = Arrays.stream(skills.split(","))
            .map(String::trim)
            .filter(skill -> !skill.isEmpty())
            .toList();
        
        System.out.print("Additional Information: ");
        String additionalInfo = sc.nextLine().trim();
        
        // Create a basic ResumeData object with the collected information
        ResumeData data = new ResumeData(
            name, 
            0, // age not needed for job application
            "", // email not needed
            "", // phone not needed
            skillsList,
            String.format("Write a professional job application letter for the position of %s at %s. %s", 
                         jobTitle, company, additionalInfo),
            ResumeStyle.PROFESSIONAL
        );
        
        try {
            System.out.println("\nGenerating your job application letter...");
            String letter = GeminiService.generateResumeText(data);
            System.out.println("\n=== JOB APPLICATION LETTER ===\n");
            System.out.println(letter);
            System.out.println("\n==============================");
        } catch (Exception e) {
            System.err.println("Error generating job application letter: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

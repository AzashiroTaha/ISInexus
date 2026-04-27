package com.isi.nexus.clipack;

import com.isi.nexus.model.Enseignant;
import com.isi.nexus.service.EnseignantService;

import java.util.List;
import java.util.Scanner;

public class EnseignantCLI {
    public static void printHeader() {
        System.out.println("\n------------------------------- ENSEIGNANTS ------------------------------");
        System.out.printf("%-8s %-18s %-18s %-14s %-22s%n", "ID", "NOM", "PRENOM", "GRADE", "SPECIALITE");
        System.out.println("-------------------------------------------------------------------------");
    }

    public static void printEnseignant(Enseignant en) {
        if (en == null) {
            System.out.println("Aucun enseignant.");
            return;
        }
        System.out.printf(
                "%-8d %-18s %-18s %-14s %-22s%n",
                en.getEn_id(),
                cut(en.getEn_nom(), 18),
                cut(en.getEn_pr(), 18),
                cut(en.getGrade(), 14),
                cut(en.getSpeciality(), 22)
        );
    }

    public static void printEnseignants(List<Enseignant> enseignants) {
        printHeader();
        if (enseignants == null || enseignants.isEmpty()) {
            System.out.println("Aucun enseignant trouve.");
            return;
        }
        for (Enseignant en : enseignants) {
            printEnseignant(en);
        }
    }

    public static void create(Scanner scanner) throws Exception {
        String nom = readRequired(scanner, "Nom: ");
        String prenom = readRequired(scanner, "Prenom: ");
        String grade = readRequired(scanner, "Grade: ");
        String specialite = readRequired(scanner, "Specialite: ");

        EnseignantService.createEnseignantService(nom, prenom, grade, specialite);
        System.out.println("Enseignant cree avec succes.");
    }

    public static void list() throws Exception {
        printEnseignants(EnseignantService.getAllEnseignantService());
    }

    public static void read(Scanner scanner) throws Exception {
        int id = readInt(scanner, "ID enseignant: ");
        Enseignant en = EnseignantService.getEnseignantByIDService(id);
        if (en == null) {
            System.out.println("Aucun enseignant trouve.");
            return;
        }
        printEnseignant(en);
    }

    public static void update(Scanner scanner) throws Exception {
        int id = readInt(scanner, "ID enseignant a modifier: ");
        String newNom = readOptional(scanner, "Nouveau nom (Entrer pour conserver): ");
        String newPrenom = readOptional(scanner, "Nouveau prenom (Entrer pour conserver): ");
        String newGrade = readOptional(scanner, "Nouveau grade (Entrer pour conserver): ");
        String newSpecialite = readOptional(scanner, "Nouvelle specialite (Entrer pour conserver): ");

        EnseignantService.updateEnseignant(
                id,
                emptyToNull(newNom),
                emptyToNull(newPrenom),
                emptyToNull(newGrade),
                emptyToNull(newSpecialite)
        );

        System.out.println("Enseignant modifie avec succes.");
    }

    public static void delete(Scanner scanner) throws Exception {
        int id = readInt(scanner, "ID enseignant a supprimer: ");
        EnseignantService.delEnseignant(id);
        System.out.println("Enseignant supprime avec succes.");
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (Exception e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
    }

    private static String readRequired(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (line != null && !line.trim().isEmpty()) {
                return line.trim();
            }
            System.out.println("Champ obligatoire.");
        }
    }

    private static String readOptional(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine();
        return line == null ? "" : line.trim();
    }

    private static String emptyToNull(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        return s.trim();
    }

    private static String cut(String value, int max) {
        if (value == null) {
            return "-";
        }
        if (value.length() <= max) {
            return value;
        }
        return value.substring(0, max - 3) + "...";
    }
}

package com.isi.nexus.clipack;

import com.isi.nexus.model.Etudiant;
import com.isi.nexus.service.EtudiantService;
import com.isi.nexus.util.funcs;

import java.util.List;
import java.util.Scanner;

public class EtudiantCLI {
    public static void printHeader() {
        System.out.println("\n------------------------------- ETUDIANTS -------------------------------");
        System.out.printf("%-6s %-20s %-16s %-16s %-30s%n", "ID", "MATRICULE", "NOM", "PRENOM", "THEME");
        System.out.println("-------------------------------------------------------------------------");
    }

    public static void printEtudiant(Etudiant etu) {
        if (etu == null) {
            System.out.println("Aucun etudiant.");
            return;
        }
        System.out.printf(
                "%-6d %-20s %-16s %-16s %-30s%n",
                etu.getIdEtudiant(),
                cut(etu.getMatricule(), 20),
                cut(etu.getEtuName(), 16),
                cut(etu.getEtuNicknm(), 16),
                cut(etu.getTheme(), 30)
        );
    }

    public static void printEtudiants(List<Etudiant> etudiants) {
        printHeader();
        if (etudiants == null || etudiants.isEmpty()) {
            System.out.println("Aucun etudiant trouve.");
            return;
        }
        for (Etudiant etu : etudiants) {
            printEtudiant(etu);
        }
    }

    public static void create(Scanner scanner) throws Exception {
        String matricule = generateUniqueMatricule();
        System.out.println("Matricule genere automatiquement: " + matricule);

        String prenom = readRequired(scanner, "Prenom: ");
        String nom = readRequired(scanner, "Nom: ");
        String theme = readRequired(scanner, "Theme: ");

        EtudiantService.createEtudiantService(matricule, prenom, nom, theme);
        System.out.println("Etudiant cree avec succes.");
    }

    public static void list() throws Exception {
        printEtudiants(EtudiantService.getAllEtuService());
    }

    public static void read(Scanner scanner) throws Exception {
        String matricule = readRequired(scanner, "Matricule: ");
        Etudiant etu = EtudiantService.getEtudiantService(matricule);
        if (etu == null) {
            System.out.println("Aucun etudiant trouve.");
            return;
        }
        printEtudiant(etu);
    }

    public static void update(Scanner scanner) throws Exception {
        String matricule = readRequired(scanner, "Matricule de l'etudiant a modifier: ");
        String newMat = readOptional(scanner, "Nouvelle matricule (Entrer pour conserver): ");
        String newNom = readOptional(scanner, "Nouveau nom (Entrer pour conserver): ");
        String newPrenom = readOptional(scanner, "Nouveau prenom (Entrer pour conserver): ");
        String newTheme = readOptional(scanner, "Nouveau theme (Entrer pour conserver): ");

        EtudiantService.updateEtudiantService(
                matricule,
                emptyToNull(newMat),
                emptyToNull(newNom),
                emptyToNull(newPrenom),
                emptyToNull(newTheme)
        );

        System.out.println("Etudiant modifie avec succes.");
    }

    public static void delete(Scanner scanner) throws Exception {
        String matricule = readRequired(scanner, "Matricule de l'etudiant a supprimer: ");
        EtudiantService.delEtuByMatService(matricule);
        System.out.println("Etudiant supprime avec succes.");
    }

    private static String generateUniqueMatricule() throws Exception {
        String matricule = funcs.etu_mat();
        while (EtudiantService.getEtudiantService(matricule) != null) {
            matricule = funcs.etu_mat();
        }
        return matricule;
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

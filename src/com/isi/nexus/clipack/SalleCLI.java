package com.isi.nexus.clipack;

import com.isi.nexus.model.Salle;
import com.isi.nexus.service.SalleService;

import java.util.List;
import java.util.Scanner;

public class SalleCLI {
    public static void printHeader() {
        System.out.println("\n--------------------------------- SALLES ---------------------------------");
        System.out.printf("%-8s %-24s %-12s %-16s%n", "ID", "NOM", "CAPACITE", "VIDEOPROJECTEUR");
        System.out.println("-------------------------------------------------------------------------");
    }

    public static void printSalle(Salle salle) {
        if (salle == null) {
            System.out.println("Aucune salle.");
            return;
        }
        System.out.printf(
                "%-8d %-24s %-12d %-16s%n",
                salle.getClasse_id(),
                cut(salle.getClasse_name(), 24),
                salle.getCapacity(),
                salle.getVProjector() ? "oui" : "non"
        );
    }

    public static void printSalles(List<Salle> salles) {
        printHeader();
        if (salles == null || salles.isEmpty()) {
            System.out.println("Aucune salle trouvee.");
            return;
        }
        for (Salle salle : salles) {
            printSalle(salle);
        }
    }

    public static void create(Scanner scanner) throws Exception {
        String nom = readRequired(scanner, "Nom de salle: ");
        int capacite = readInt(scanner, "Capacite: ");
        String vproj = readRequired(scanner, "Video projecteur (oui/non): ");

        SalleService.createSalle(nom, capacite, vproj);
        System.out.println("Salle creee avec succes.");
    }

    public static void list() throws Exception {
        printSalles(SalleService.getAllSalle());
    }

    public static void read(Scanner scanner) throws Exception {
        String nom = readRequired(scanner, "Nom de salle: ");
        printSalle(SalleService.getSalle(nom));
    }

    public static void update(Scanner scanner) throws Exception {
        String nom = readRequired(scanner, "Nom de la salle a modifier: ");
        String newNom = readOptional(scanner, "Nouveau nom (Entrer pour conserver): ");
        Integer newCapacity = readOptionalInt(scanner, "Nouvelle capacite (Entrer pour conserver): ");
        String newVproj = readOptional(scanner, "Video projecteur oui/non (Entrer pour conserver): ");

        SalleService.updateSalleService(
                nom,
                emptyToNull(newNom),
                newCapacity,
                emptyToNull(newVproj)
        );

        System.out.println("Salle modifiee avec succes.");
    }

    public static void delete(Scanner scanner) throws Exception {
        String nom = readRequired(scanner, "Nom de la salle a supprimer: ");
        SalleService.delSalleService(nom);
        System.out.println("Salle supprimee avec succes.");
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

    private static Integer readOptionalInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (line == null || line.trim().isEmpty()) {
                return null;
            }
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

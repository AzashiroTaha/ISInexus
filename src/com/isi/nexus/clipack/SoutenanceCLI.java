package com.isi.nexus.clipack;

import com.isi.nexus.model.Enseignant;
import com.isi.nexus.model.Etudiant;
import com.isi.nexus.model.Salle;
import com.isi.nexus.model.Soutenance;
import com.isi.nexus.service.EnseignantService;
import com.isi.nexus.service.EtudiantService;
import com.isi.nexus.service.SalleService;
import com.isi.nexus.service.SoutenanceService;
import com.isi.nexus.util.funcs;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class SoutenanceCLI {
    public static void printHeader() {
        System.out.println("\n-------------------------------- SOUTENANCES -------------------------------");
        System.out.printf(
                "%-5s %-12s %-11s %-12s %-14s %-14s %-14s %-14s%n",
                "ID", "DATE", "HEURE", "SALLE", "ETUDIANT", "PRESIDENT", "EXAMINATEUR", "ENCADREUR"
        );
        System.out.println("----------------------------------------------------------------------------");
    }

    public static void printSoutenance(Soutenance sout) {
        if (sout == null) {
            System.out.println("Aucune soutenance.");
            return;
        }
        System.out.printf(
                "%-5d %-12s %-11s %-12s %-14s %-14s %-14s %-14s%n",
                sout.getSoutenance_id(),
                sout.getSoutenance_date(),
                cut(sout.getHeure_start() + "-" + sout.getHeure_end(), 11),
                cut(sout.getSoutenance_salle() == null ? "-" : sout.getSoutenance_salle().getClasse_name(), 12),
                cut(sout.getSoutenance_etudiant() == null ? "-" : sout.getSoutenance_etudiant().getMatricule(), 14),
                cut(sout.getSoutenance_president() == null ? "-" : sout.getSoutenance_president().getEn_nom(), 14),
                cut(sout.getsoutenance_examinateur() == null ? "-" : sout.getsoutenance_examinateur().getEn_nom(), 14),
                cut(sout.getSoutenance_encadreur() == null ? "-" : sout.getSoutenance_encadreur().getEn_nom(), 14)
        );
    }

    public static void printSoutenances(List<Soutenance> soutenances) {
        printHeader();
        if (soutenances == null || soutenances.isEmpty()) {
            System.out.println("Aucune soutenance trouvee.");
            return;
        }
        for (Soutenance sout : soutenances) {
            printSoutenance(sout);
        }
    }

    public static void create(Scanner scanner) throws Exception {
        LocalDate date = readDateRequired(scanner, "Date (dd/MM/yyyy): ");
        LocalTime heureStart = readTimeRequired(scanner, "Heure debut (HH:mm): ");
        LocalTime heureEnd = readTimeRequired(scanner, "Heure fin (HH:mm): ");

        Salle salle = chooseSalle(scanner, true);
        Etudiant etudiant = chooseEtudiant(scanner, true);
        Enseignant president = chooseEnseignant(scanner, "president", true);
        Enseignant examinateur = chooseEnseignant(scanner, "examinateur", true);
        Enseignant encadreur = chooseEnseignant(scanner, "encadreur", true);

        SoutenanceService.createSoutenanceService(
                date,
                salle,
                etudiant,
                president,
                examinateur,
                encadreur,
                heureStart,
                heureEnd
        );

        System.out.println("Soutenance creee avec succes.");
    }

    public static void list() throws Exception {
        printSoutenances(SoutenanceService.getAllSoutenanceService());
    }

    public static void read(Scanner scanner) throws Exception {
        int id = readInt(scanner, "ID soutenance: ");
        printSoutenance(SoutenanceService.getSoutenance(id));
    }

    public static void update(Scanner scanner) throws Exception {
        list();
        int id = readInt(scanner, "ID soutenance a modifier: ");

        LocalDate date = readDateOptional(scanner, "Nouvelle date (dd/MM/yyyy, Entrer pour conserver): ");
        LocalTime heureStart = readTimeOptional(scanner, "Nouvelle heure debut (HH:mm, Entrer pour conserver): ");
        LocalTime heureEnd = readTimeOptional(scanner, "Nouvelle heure fin (HH:mm, Entrer pour conserver): ");

        Salle salle = askChangeSalle(scanner);
        Etudiant etudiant = askChangeEtudiant(scanner);
        Enseignant president = askChangeEnseignant(scanner, "president");
        Enseignant examinateur = askChangeEnseignant(scanner, "examinateur");
        Enseignant encadreur = askChangeEnseignant(scanner, "encadreur");

        SoutenanceService.updateSoutenanceService(
                id,
                date,
                salle,
                etudiant,
                president,
                examinateur,
                encadreur,
                heureStart,
                heureEnd
        );

        System.out.println("Soutenance modifiee avec succes.");
    }

    public static void delete(Scanner scanner) throws Exception {
        list();
        int id = readInt(scanner, "ID soutenance a supprimer: ");
        SoutenanceService.delSoutenance(id);
        System.out.println("Soutenance supprimee avec succes.");
    }

    private static Salle askChangeSalle(Scanner scanner) throws Exception {
        String choix = readOptional(scanner, "Changer la salle ? (oui/non): ");
        if (!"oui".equalsIgnoreCase(choix)) {
            return null;
        }
        return chooseSalle(scanner, true);
    }

    private static Etudiant askChangeEtudiant(Scanner scanner) throws Exception {
        String choix = readOptional(scanner, "Changer l'etudiant ? (oui/non): ");
        if (!"oui".equalsIgnoreCase(choix)) {
            return null;
        }
        return chooseEtudiant(scanner, true);
    }

    private static Enseignant askChangeEnseignant(Scanner scanner, String role) throws Exception {
        String choix = readOptional(scanner, "Changer " + role + " ? (oui/non): ");
        if (!"oui".equalsIgnoreCase(choix)) {
            return null;
        }
        return chooseEnseignant(scanner, role, true);
    }

    private static Salle chooseSalle(Scanner scanner, boolean required) throws Exception {
        List<Salle> salles = SalleService.getAllSalle();
        SalleCLI.printSalles(salles);

        while (true) {
            Integer id = readOptionalInt(scanner, "Choisir ID salle" + (required ? "" : " (Entrer pour ignorer)") + ": ");
            if (id == null && !required) {
                return null;
            }
            if (id == null) {
                System.out.println("ID obligatoire.");
                continue;
            }

            for (Salle salle : salles) {
                if (salle.getClasse_id() == id) {
                    return salle;
                }
            }
            System.out.println("ID salle invalide.");
        }
    }

    private static Etudiant chooseEtudiant(Scanner scanner, boolean required) throws Exception {
        List<Etudiant> etudiants = EtudiantService.getAllEtuService();
        EtudiantCLI.printEtudiants(etudiants);

        while (true) {
            Integer id = readOptionalInt(scanner, "Choisir ID etudiant" + (required ? "" : " (Entrer pour ignorer)") + ": ");
            if (id == null && !required) {
                return null;
            }
            if (id == null) {
                System.out.println("ID obligatoire.");
                continue;
            }

            for (Etudiant etu : etudiants) {
                if (etu.getIdEtudiant() == id) {
                    return etu;
                }
            }
            System.out.println("ID etudiant invalide.");
        }
    }

    private static Enseignant chooseEnseignant(Scanner scanner, String role, boolean required) throws Exception {
        List<Enseignant> enseignants = EnseignantService.getAllEnseignantService();
        EnseignantCLI.printEnseignants(enseignants);

        while (true) {
            Integer id = readOptionalInt(scanner, "Choisir ID " + role + (required ? "" : " (Entrer pour ignorer)") + ": ");
            if (id == null && !required) {
                return null;
            }
            if (id == null) {
                System.out.println("ID obligatoire.");
                continue;
            }

            for (Enseignant en : enseignants) {
                if (en.getEn_id() == id) {
                    return en;
                }
            }
            System.out.println("ID enseignant invalide.");
        }
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

    private static String readOptional(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine();
        return line == null ? "" : line.trim();
    }

    private static LocalDate readDateRequired(Scanner scanner, String prompt) {
        while (true) {
            String line = readRequired(scanner, prompt);
            try {
                return funcs.dateFR(line);
            } catch (Exception e) {
                System.out.println("Date invalide. Format attendu: dd/MM/yyyy");
            }
        }
    }

    private static LocalDate readDateOptional(Scanner scanner, String prompt) {
        while (true) {
            String line = readOptional(scanner, prompt);
            if (line.isEmpty()) {
                return null;
            }
            try {
                return funcs.dateFR(line);
            } catch (Exception e) {
                System.out.println("Date invalide. Format attendu: dd/MM/yyyy");
            }
        }
    }

    private static LocalTime readTimeRequired(Scanner scanner, String prompt) {
        while (true) {
            String line = readRequired(scanner, prompt);
            try {
                return funcs.heureFR(line);
            } catch (Exception e) {
                System.out.println("Heure invalide. Format attendu: HH:mm");
            }
        }
    }

    private static LocalTime readTimeOptional(Scanner scanner, String prompt) {
        while (true) {
            String line = readOptional(scanner, prompt);
            if (line.isEmpty()) {
                return null;
            }
            try {
                return funcs.heureFR(line);
            } catch (Exception e) {
                System.out.println("Heure invalide. Format attendu: HH:mm");
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

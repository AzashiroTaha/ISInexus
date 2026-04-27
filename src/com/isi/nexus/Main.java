package com.isi.nexus;

import com.isi.nexus.clipack.EnseignantCLI;
import com.isi.nexus.clipack.EtudiantCLI;
import com.isi.nexus.clipack.SalleCLI;
import com.isi.nexus.clipack.SoutenanceCLI;

import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String LINE = "============================================================";

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printTitle("ISI NEXUS CLI");
            System.out.println("[1] Etudiants");
            System.out.println("[2] Enseignants");
            System.out.println("[3] Salles");
            System.out.println("[4] Soutenances");
            System.out.println("[0] Quitter");

            int choix = readInt("\nVotre choix: ");

            switch (choix) {
                case 1:
                    menuEtudiant();
                    break;
                case 2:
                    menuEnseignant();
                    break;
                case 3:
                    menuSalle();
                    break;
                case 4:
                    menuSoutenance();
                    break;
                case 0:
                    running = false;
                    System.out.println("\nMerci d'avoir utilise ISI NEXUS CLI.");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private static void menuEtudiant() {
        boolean back = false;

        while (!back) {
            printSection("Etudiant");
            System.out.println("[1] Creer etudiant");
            System.out.println("[2] Lister etudiants");
            System.out.println("[3] Rechercher etudiant (matricule)");
            System.out.println("[4] Modifier etudiant");
            System.out.println("[5] Supprimer etudiant (matricule)");
            System.out.println("[0] Retour");

            int choix = readInt("\nVotre choix: ");

            try {
                switch (choix) {
                    case 1:
                        EtudiantCLI.create(SCANNER);
                        break;
                    case 2:
                        EtudiantCLI.list();
                        break;
                    case 3:
                        EtudiantCLI.read(SCANNER);
                        break;
                    case 4:
                        EtudiantCLI.update(SCANNER);
                        break;
                    case 5:
                        EtudiantCLI.delete(SCANNER);
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    private static void menuEnseignant() {
        boolean back = false;

        while (!back) {
            printSection("Enseignant");
            System.out.println("[1] Creer enseignant");
            System.out.println("[2] Lister enseignants");
            System.out.println("[3] Rechercher enseignant (ID)");
            System.out.println("[4] Modifier enseignant");
            System.out.println("[5] Supprimer enseignant");
            System.out.println("[0] Retour");

            int choix = readInt("\nVotre choix: ");

            try {
                switch (choix) {
                    case 1:
                        EnseignantCLI.create(SCANNER);
                        break;
                    case 2:
                        EnseignantCLI.list();
                        break;
                    case 3:
                        EnseignantCLI.read(SCANNER);
                        break;
                    case 4:
                        EnseignantCLI.update(SCANNER);
                        break;
                    case 5:
                        EnseignantCLI.delete(SCANNER);
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    private static void menuSalle() {
        boolean back = false;

        while (!back) {
            printSection("Salle");
            System.out.println("[1] Creer salle");
            System.out.println("[2] Lister salles");
            System.out.println("[3] Rechercher salle (nom)");
            System.out.println("[4] Modifier salle");
            System.out.println("[5] Supprimer salle");
            System.out.println("[0] Retour");

            int choix = readInt("\nVotre choix: ");

            try {
                switch (choix) {
                    case 1:
                        SalleCLI.create(SCANNER);
                        break;
                    case 2:
                        SalleCLI.list();
                        break;
                    case 3:
                        SalleCLI.read(SCANNER);
                        break;
                    case 4:
                        SalleCLI.update(SCANNER);
                        break;
                    case 5:
                        SalleCLI.delete(SCANNER);
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    private static void menuSoutenance() {
        boolean back = false;

        while (!back) {
            printSection("Soutenance");
            System.out.println("[1] Creer soutenance");
            System.out.println("[2] Lister soutenances");
            System.out.println("[3] Rechercher soutenance (ID)");
            System.out.println("[4] Modifier soutenance");
            System.out.println("[5] Supprimer soutenance");
            System.out.println("[0] Retour");

            int choix = readInt("\nVotre choix: ");

            try {
                switch (choix) {
                    case 1:
                        SoutenanceCLI.create(SCANNER);
                        break;
                    case 2:
                        SoutenanceCLI.list();
                        break;
                    case 3:
                        SoutenanceCLI.read(SCANNER);
                        break;
                    case 4:
                        SoutenanceCLI.update(SCANNER);
                        break;
                    case 5:
                        SoutenanceCLI.delete(SCANNER);
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
    }

    private static void printTitle(String title) {
        System.out.println("\n" + LINE);
        System.out.printf("  %s%n", title);
        System.out.println(LINE);
    }

    private static void printSection(String title) {
        System.out.println("\n------------------------------------------------------------");
        System.out.printf("  %s%n", title);
        System.out.println("------------------------------------------------------------");
    }
}

package com.isi.nexus.service;

import com.isi.nexus.dbpack.*;
import com.isi.nexus.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SoutenanceService {
    private final static Soutenance_db soutenance_db = new Soutenance_db();

    public static void createSoutenanceService(LocalDate soutenance_date, Salle soutenance_salle, Etudiant soutenance_etudiant, Enseignant soutenance_president, Enseignant soutenance_examinateur, Enseignant soutenance_encadreur, LocalTime heure_start, LocalTime heure_end) throws Exception{

        if (soutenance_date == null) throw new Exception("La date est obligatoire.");
        if (heure_start == null) throw new Exception("Preciser l'heure de debut");
        if (heure_end == null) throw new Exception("Preciser l'heure de fin");

        if (soutenance_date.isBefore(LocalDate.now())) {
            throw new Exception("La date est incorrecte.");
        }

        if (!heure_start.isBefore(heure_end)) {
            throw new Exception("L'heure de debut doit etre avant l'heure de fin.");
        }

        if (soutenance_salle == null){
            throw new Exception("Aucune salle choisie.");
        }

        if (soutenance_etudiant == null){
            throw new Exception("Aucun etudiant choisie.");
        }

        if (soutenance_president == null){
            throw new Exception("Il faut choisir une president.");
        }

        if (soutenance_examinateur == null){
            throw new Exception("Il faut choisir une examinateur.");
        }

        if (soutenance_encadreur == null){
            throw new Exception("Il faut choisir une encadreur.");
        }

        if(Soutenance_db.checkSalleTaken(soutenance_salle.getClasse_id(), soutenance_date, heure_start,heure_end)){
            throw new Exception("Salle deja prise.");
        }

        if (Soutenance_db.checkEtudiantInSoutenance(soutenance_etudiant.getIdEtudiant())){
            throw new Exception("Cet Etudiant a deja une soutenance.");
        }

        if (Soutenance_db.checkEnseignantTaken(soutenance_president.getEn_id(),  soutenance_date, heure_start,heure_end)){
            throw new Exception("President deja a une soutenance.");
        }

        if (Soutenance_db.checkEnseignantTaken(soutenance_examinateur.getEn_id(),  soutenance_date, heure_start,heure_end)){
            throw new Exception("Examinateur deja associe a une soutenance.");
        }

        if (Soutenance_db.checkEnseignantTaken(soutenance_encadreur.getEn_id(),  soutenance_date, heure_start,heure_end)){
            throw new Exception("Encadreur deja associe a une soutenance.");
        }

        if (soutenance_examinateur.getEn_id() == soutenance_president.getEn_id() || soutenance_examinateur.getEn_id() == soutenance_encadreur.getEn_id() || soutenance_president.getEn_id() == soutenance_encadreur.getEn_id()){
            throw new Exception("Enseignat avec plusieur fonction pour une seule soutenance.");
        }

        Soutenance_db.createSoutenance(soutenance_date, soutenance_salle, soutenance_etudiant, soutenance_president, soutenance_examinateur, soutenance_encadreur, heure_start, heure_end);
    }

    //updating "Soutenance"
    public static void updateSoutenanceService(int soutenance_id, LocalDate soutenance_date,Salle soutenance_salle, Etudiant soutenance_etudiant, Enseignant soutenance_president, Enseignant soutenance_examinateur, Enseignant soutenance_encadreur, LocalTime heure_start, LocalTime heure_end) throws Exception{
        if (soutenance_id <= 0) throw new Exception("ID invalide.");
        Soutenance oldSoutenance = soutenance_db.getSoutenanceByID(soutenance_id);
        if (oldSoutenance == null) throw new Exception("Pas de soutenance avec cet ID.");

        if (soutenance_date == null) soutenance_date = oldSoutenance.getSoutenance_date();
        if (soutenance_salle == null) soutenance_salle = oldSoutenance.getSoutenance_salle();
        if (soutenance_etudiant == null) soutenance_etudiant = oldSoutenance.getSoutenance_etudiant();
        if (soutenance_president == null) soutenance_president = oldSoutenance.getSoutenance_president();
        if (soutenance_examinateur == null) soutenance_examinateur = oldSoutenance.getsoutenance_examinateur();
        if (soutenance_encadreur == null) soutenance_encadreur = oldSoutenance.getSoutenance_encadreur();
        if (heure_start == null) heure_start = oldSoutenance.getHeure_start();
        if (heure_end == null) heure_end = oldSoutenance.getHeure_end();

        if (soutenance_date.isBefore(LocalDate.now())) {
            throw new Exception("La date est incorrecte.");
        }

        if (!heure_start.isBefore(heure_end)) {
            throw new Exception("L'heure de debut doit etre avant l'heure de fin.");
        }

        if (soutenance_examinateur.getEn_id() == soutenance_president.getEn_id() || soutenance_examinateur.getEn_id() == soutenance_encadreur.getEn_id() || soutenance_president.getEn_id() == soutenance_encadreur.getEn_id()){
            throw new Exception("Enseignat avec plusieur fonction pour une seule soutenance.");
        }

        List<Soutenance> allSoutenances = soutenance_db.getAllSoutenances();
        for (Soutenance sout : allSoutenances) {
            if (sout.getSoutenance_id() == soutenance_id) {
                continue;
            }

            boolean sameDate = soutenance_date.equals(sout.getSoutenance_date());
            boolean overlap = heure_start.isBefore(sout.getHeure_end()) && heure_end.isAfter(sout.getHeure_start());
            if (!sameDate || !overlap) {
                continue;
            }

            if (sout.getSoutenance_salle() != null && sout.getSoutenance_salle().getClasse_id() == soutenance_salle.getClasse_id()) {
                throw new Exception("Salle deja prise.");
            }

            if (sout.getSoutenance_etudiant() != null && sout.getSoutenance_etudiant().getIdEtudiant() == soutenance_etudiant.getIdEtudiant()) {
                throw new Exception("Cet Etudiant a deja une soutenance.");
            }

            int p = soutenance_president.getEn_id();
            int x = soutenance_examinateur.getEn_id();
            int e = soutenance_encadreur.getEn_id();
            int ep = sout.getSoutenance_president() == null ? -1 : sout.getSoutenance_president().getEn_id();
            int ex = sout.getsoutenance_examinateur() == null ? -1 : sout.getsoutenance_examinateur().getEn_id();
            int ee = sout.getSoutenance_encadreur() == null ? -1 : sout.getSoutenance_encadreur().getEn_id();

            if (p == ep || p == ex || p == ee) {
                throw new Exception("President deja a une soutenance.");
            }

            if (x == ep || x == ex || x == ee) {
                throw new Exception("Examinateur deja associe a une soutenance.");
            }

            if (e == ep || e == ex || e == ee) {
                throw new Exception("Encadreur deja associe a une soutenance.");
            }
        }

        soutenance_db.updateSoutenance(soutenance_id, soutenance_date, soutenance_salle, soutenance_etudiant, soutenance_president,soutenance_examinateur, soutenance_encadreur, heure_start, heure_end);
    }

    //getting "Soutenance"
    public static Soutenance getSoutenance(int soutenance_id) throws Exception{
        if (soutenance_id <= 0){
            throw new Exception("ID invalide.");
        }

        if (soutenance_db.getSoutenanceByID(soutenance_id) == null) throw new Exception("Pas de soutenance avec cet ID.");

        return soutenance_db.getSoutenanceByID(soutenance_id);
    }

    //getting all "Soutenance"
    public static List<Soutenance> getAllSoutenanceService() throws Exception{
        List<Soutenance> allSoutenance = soutenance_db.getAllSoutenances();

        if (allSoutenance == null || allSoutenance.isEmpty()){
            throw new Exception("Pas de soutenance dans la base de donnee.");
        }

        return allSoutenance;
    }


    //deleting "Soutenance"
    public static void delSoutenance(int soutenance_id) throws Exception{
        if (soutenance_id <= 0){
            throw new Exception("ID invalide.");
        }

        if (soutenance_db.getSoutenanceByID(soutenance_id) == null) throw new Exception("Pas de soutenance avec cet ID.");

        soutenance_db.delSoutenance(soutenance_id);
    }
}

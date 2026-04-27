package com.isi.nexus.service;

import com.isi.nexus.dbpack.Salle_db;
import com.isi.nexus.model.Salle;

import java.util.*;

public class SalleService {
    private final static Salle_db salleDB = new Salle_db();

    //Creating "Salle"
    public static boolean createSalle(String salleName, int capacity, String check) throws Exception{
        boolean hasVideoProjector;

        if (salleName == null || salleName.trim().isEmpty()){
            throw new Exception("le nom de la salle ne doit pas etre vide.");
        }

        if (salleDB.getSalleByName(salleName) != null){
            throw new Exception("Il existe deja une salle avec cet nom.");
        }

        if (capacity <= 5){
            throw new Exception("Capacite insuffisante.");
        }

//        if (check == null || check.trim().isEmpty() || (!check.equalsIgnoreCase("oui") && !check.equalsIgnoreCase("non"))){
//            throw new Exception("Pas d'information sur la presence de video projecteur");
//        }

        if (check == null || check.trim().isEmpty()) {
            throw new Exception("Information manquante sur le vidéoprojecteur");
        }

        if (!check.equalsIgnoreCase("oui") && !check.equalsIgnoreCase("non")) {
            throw new Exception("Valeur invalide : oui ou non attendu");
        }

        hasVideoProjector = check.equalsIgnoreCase("oui");

        Salle_db.createClasse(salleName,capacity,hasVideoProjector);
        return true;
    }


    //searchin "Salle"
    public static Salle getSalle(String salleName) throws Exception{
        if (salleName == null || salleName.trim().isEmpty()){
            throw new Exception("Nom de la salle vide.");
        }

        Salle sal = salleDB.getSalleByName(salleName);
        if (sal == null){
            throw new Exception("Il n'existe pas de salle avec ce nom.");
        }

        return sal;
    }

    //getting all "Salle"
    public static List<Salle> getAllSalle() throws Exception{
        List<Salle> allSalle = salleDB.getAllSalles();
        if (allSalle == null) throw new Exception("Pas de Salle existante.");

        return allSalle;
    }

    //update "Salle"
    public static void updateSalleService(String  salleName, String newSalleName, Integer newCapacity, String newCheck) throws Exception{
        boolean newHasVideoProjector;

        if (salleName == null || salleName.trim().isEmpty()){
            throw new Exception("Nom de la salle vide.");
        }

        Salle sal = salleDB.getSalleByName(salleName);

        if (sal == null){
            throw new Exception("Pas de salle avec ce nom.");
        }

        if (newCapacity == null){
            newCapacity = sal.getCapacity();
        }else if (newCapacity <= 5){
            throw new Exception("Capacite insuffisante.");
        }

        if (newSalleName == null || newSalleName.trim().isEmpty()){
            newSalleName = sal.getClasse_name();
        }else if (salleDB.getSalleByName(newSalleName) != null && !salleName.equalsIgnoreCase(newSalleName)) throw new Exception("Il existe deja une salle avec ce nom.");

        if (newCheck == null || newCheck.trim().isEmpty()){
            newHasVideoProjector = sal.getVProjector();
        } else {
            if (!newCheck.equalsIgnoreCase("oui") && !newCheck.equalsIgnoreCase("non")){
                throw new Exception("Valeur invalide : oui ou non attendu");
            }
            newHasVideoProjector = newCheck.equalsIgnoreCase("oui");
        }

        salleDB.updateSalle(sal.getClasse_id(), newSalleName, newCapacity, newHasVideoProjector);
    }

    //deleting "Salle"
    public static void delSalleService(String salleName) throws Exception{
        if (salleName == null || salleName.trim().isEmpty()){
            throw new Exception("Nom de la salle vide.");
        }else if (salleDB.getSalleByName(salleName) == null){
            throw new Exception("Pas de salle avec ce nom.");
        }

        salleDB.delSalle(salleName);
    }

}

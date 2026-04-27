package com.isi.nexus.service;

import com.isi.nexus.dbpack.Enseignant_db;
import com.isi.nexus.model.Enseignant;

import java.util.*;

public class EnseignantService {
    private final static Enseignant_db enseignantDB = new Enseignant_db();

    //create "Enseignant"
    public static boolean createEnseignantService(String en_nom, String en_pr, String grade, String speciality) throws  Exception{
        if (en_nom == null || en_nom.trim().isEmpty()){
            throw new Exception("Le nom ne doit pas etre vide.");
        }

        if (en_pr == null || en_pr.trim().isEmpty()){
            throw new Exception("Le prenom ne doit pas etre vide.");
        }

        if (grade == null || grade.trim().isEmpty()){
            throw new Exception("Le grade ne doit pas etre vide.");
        }

        if (speciality == null || speciality.trim().isEmpty()){
            throw new Exception("La specialite ne doit pas etre vide.");
        }

        Enseignant_db.createEnseignant(en_nom,en_pr,grade,speciality);
        return true;
    }

    //search "Enseignant" by ID
    public static Enseignant getEnseignantByIDService(int id)throws Exception{
        if (id < 10000){
            throw new Exception("ID enseignant invalide.");
        }
        return enseignantDB.getEnseignantByID(id);
    }

    //getting all
    public static List<Enseignant> getAllEnseignantService() throws Exception{
        List<Enseignant> allEnseignants = new Enseignant_db().gatAllEnseignant();
        if (allEnseignants == null){
            throw new Exception("Il n'y a pas d'Enseignants.");
        }
        return allEnseignants;
    }

    //updating "Enseignant
    public static boolean updateEnseignant(int en_id, String newEnNom, String newEnPr, String newGrade, String newSpeciality) throws Exception{
        if (en_id < 10000){
            throw new Exception("ID invalide.");
        }

        Enseignant en = enseignantDB.getEnseignantByID(en_id);

        if (en == null){
            throw new Exception("Aucun Enseignant trouve.");
        }

        if (newEnNom == null || newEnNom.trim().isEmpty()){
            newEnNom = en.getEn_nom();
        }

        if (newEnPr == null || newEnPr.trim().isEmpty()){
            newEnPr = en.getEn_pr();
        }

        if (newGrade == null || newGrade.trim().isEmpty()){
            newGrade = en.getGrade();
        }

        if (newSpeciality == null || newSpeciality.trim().isEmpty()){
            newSpeciality = en.getSpeciality();
        }

        enseignantDB.editEnseignant(en_id,newEnNom,newEnPr,newGrade,newSpeciality);
        return true;
    }

    //deleting "Enseignant"
    public static boolean delEnseignant(int en_id) throws Exception{
        if (en_id < 10000){
            throw new Exception("ID invalide.");
        }

        if (enseignantDB.getEnseignantByID(en_id) == null){
            throw new Exception("Aucun enseignant trouve avec cet ID.");
        }

        enseignantDB.delEnseignant(en_id);
        return true;
    }
}

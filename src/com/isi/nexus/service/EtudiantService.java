package com.isi.nexus.service;

import com.isi.nexus.dbpack.Etudiant_db;
import com.isi.nexus.model.Etudiant;

import java.util.List;

public class EtudiantService {
    private final static Etudiant_db etuDB = new Etudiant_db();

    public static boolean createEtudiantService(String mat, String etu_pr, String etu_nom,  String theme) throws Exception{

        if (mat == null || mat.trim().isEmpty()){
            throw new Exception("La matricule ne doit pas etre vide.");
        }

        if (etu_nom == null || etu_nom.trim().isEmpty()){
            throw new Exception("Le nom de l'etudiant ne doit pas etre vide");
        }

        if (etu_pr == null || etu_pr.trim().isEmpty()){
            throw new Exception("La prenom de l'etudiant ne doit pas etre vide.");
        }

        if (theme == null || theme.trim().isEmpty()){
            throw new Exception("Le theme ne doit pas etre vide.");
        }else if (theme.length() < 10){
            throw new Exception("Theme trop court pour une soutenance.");
        }

        if (etuDB.getEtu_matricule(mat) != null){
            throw new Exception("Il existe deja un etudiant avec ce matricule");
        }

        Etudiant_db.createEtudiant(mat,etu_pr,etu_nom,theme);
        return true;

    }

    //search "Etudiant" by mat
    public static Etudiant getEtudiantService(String mat) throws Exception{
        if (mat == null || mat.trim().isEmpty()){
            throw new Exception("Ajouter une matricule.");
        }

        return new Etudiant_db().getEtu_matricule(mat);
    }



    //getting all "Etudiant"
    public static List<Etudiant> getAllEtuService() throws Exception{
        List<Etudiant> allEtu = new Etudiant_db().allEtudiant();

        if (allEtu == null){
            throw new Exception("Pas d'etudiant dans la base de donnee");
        }

        return allEtu;
    }

    //delete "Etudiant

    /**
     * 1e function delete "Etudiant" by id (clic via UI)
     * 2e function delete "Etudiant" by matricule (searche by mat)
     */

    //1st del func
    public static boolean delEtuByIdService(int etu_id) throws  Exception{
        if (etu_id < 0){
            throw new Exception("ID invalide.");
        }

        if (etuDB.getEtuId(etu_id) == null){
            throw new Exception("Aucun etudiant trouve avec cet ID.");
        }

        etuDB.delEtudiant(etu_id);
        return true;
    }

    //2nd del func
    public static boolean delEtuByMatService(String mat) throws  Exception{
        if (mat == null || mat.trim().isEmpty()){
            throw new Exception("La matricule ne doit pas etre vide.");
        }

        Etudiant etu = etuDB.getEtu_matricule(mat);
        if (etu == null){
            throw new Exception("Aucun etudiant trouve avec cette matricule.");
        }

        etuDB.delEtudiant(etu.getIdEtudiant());
        return true;
    }

    //Updating "Etudiant"
    public static boolean updateEtudiantService(String mat, String newMat, String newEtu_nom, String newEtu_pr, String newTheme) throws Exception{
        if (mat == null || mat.trim().isEmpty()){
            throw new Exception("La matricule ne doit pas etre vide.");
        }

        Etudiant etu = etuDB.getEtu_matricule(mat);

        if (etu == null){
            throw new Exception("Aucun Etudiant avec cette matricule.");
        }

        if (newMat == null || newMat.trim().isEmpty()){
            newMat = etu.getMatricule();
        }

        if (newEtu_nom == null || newEtu_nom.trim().isEmpty()){
            newEtu_nom = etu.getEtuName();
        }

        if (newEtu_pr == null || newEtu_pr.trim().isEmpty()){
            newEtu_pr = etu.getEtuNicknm();
        }

        if (newTheme == null || newTheme.trim().isEmpty()){
            newTheme =  etu.getTheme();
        }

        etuDB.updateEtudiant(etu.getMatricule(),newMat,newEtu_nom,newEtu_pr,newTheme);
        return true;
    }

}

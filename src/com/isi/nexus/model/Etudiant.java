package com.isi.nexus.model;

public class Etudiant {

    private int idEtudiant;
    private String name;
    private String EtuNicknm;
    private String matricule;
    private String theme;


    //Constructor
    public Etudiant(){

    }

    public Etudiant(String matricule ,String name, String Nickname, String theme){
        this.name = name;
        this.EtuNicknm = EtuNicknm;
        this.theme = theme;
        this.matricule = matricule;
    }

    public Etudiant(int idEtudiant, String matricule ,String name, String Nickname, String theme){
        this.idEtudiant = idEtudiant;
        this.name = name;
        this.EtuNicknm = Nickname;
        this.theme = theme;
        this.matricule = matricule;
    }

    //Getter and Setter


    public int getIdEtudiant() {
        return idEtudiant;
    }

    public String getEtuName() {
        return name;
    }

    public String getEtuNicknm() {
        return EtuNicknm;
    }

    public String getTheme() {
        return theme;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public void setEtuName(String name) {
        this.name = name;
    }

    public void setEtuNicknm(String etuNicknm) {
        EtuNicknm = etuNicknm;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
}



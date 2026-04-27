package com.isi.nexus.model;

public class Enseignant {
    private int en_id;
    private String en_nom;
    private String en_pr;
    private String grade;
    private String speciality;

    public Enseignant(){

    }

    public Enseignant(int en_id, String en_nom, String en_pr, String grade, String speciality){
        this.en_id = en_id;
        this.en_nom = en_nom;
        this.en_pr = en_pr;
        this.grade = grade;
        this.speciality = speciality;
    }

    public Enseignant(String en_nom, String en_pr, String grade, String speciality){
        this.en_nom = en_nom;
        this.en_pr = en_pr;
        this.grade = grade;
        this.speciality = speciality;
    }

    public int getEn_id() {
        return en_id;
    }

    public String getEn_nom() {
        return en_nom;
    }

    public String getEn_pr() {
        return en_pr;
    }

    public String getGrade() {
        return grade;
    }

    public String getSpeciality() {
        return speciality;
    }

    //Setters

    public void setEn_id(int en_id) {
        this.en_id = en_id;
    }

    public void setEn_nom(String en_nom) {
        this.en_nom = en_nom;
    }

    public void setEn_pr(String en_pr) {
        this.en_pr = en_pr;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}

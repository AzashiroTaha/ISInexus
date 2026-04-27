package com.isi.nexus.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Soutenance {
    private int soutenance_id;
    private LocalDate soutenance_date;
    private Salle soutenance_salle;
    private Etudiant soutenance_etudiant;
    private Enseignant soutenance_president;
    private Enseignant soutenance_examinateur;
    private Enseignant soutenance_encadreur;
    private LocalTime heure_start;
    private LocalTime heure_end;


    public Soutenance(){

    }

    public Soutenance(LocalDate soutenance_date, Salle soutenance_salle, Etudiant soutenance_etudiant, Enseignant soutenance_president, Enseignant soutenance_examinateur, Enseignant soutenance_encadreur, LocalTime heure_start, LocalTime heure_end) {
        this.soutenance_date = soutenance_date;
        this.soutenance_salle = soutenance_salle;
        this.soutenance_etudiant = soutenance_etudiant;
        this.soutenance_president = soutenance_president;
        this.soutenance_examinateur = soutenance_examinateur;
        this.soutenance_encadreur = soutenance_encadreur;
        this.heure_start = heure_start;
        this.heure_end = heure_end;
    }

    public Soutenance(int soutenance_id, LocalDate soutenance_date, Salle soutenance_salle, Etudiant soutenance_etudiant,
                      Enseignant soutenance_president, Enseignant soutenance_examinateur,
                      Enseignant soutenance_encadreur, LocalTime heure_start, LocalTime heure_end) {
        this.soutenance_id = soutenance_id;
        this.soutenance_date = soutenance_date;
        this.soutenance_salle = soutenance_salle;
        this.soutenance_etudiant = soutenance_etudiant;
        this.soutenance_president = soutenance_president;
        this.soutenance_examinateur = soutenance_examinateur;
        this.soutenance_encadreur = soutenance_encadreur;
        this.heure_start = heure_start;
        this.heure_end = heure_end;
    }

    public int getSoutenance_id() {
        return soutenance_id;
    }

    public LocalDate getSoutenance_date() {
        return soutenance_date;
    }

    public Salle getSoutenance_salle() {
        return soutenance_salle;
    }

    public Etudiant getSoutenance_etudiant() {
        return soutenance_etudiant;
    }

    public Enseignant getSoutenance_president() {
        return soutenance_president;
    }

    public Enseignant getsoutenance_examinateur() {
        return soutenance_examinateur;
    }

    public Enseignant getSoutenance_encadreur() {
        return soutenance_encadreur;
    }

    public LocalTime getHeure_start() {
        return heure_start;
    }

    public LocalTime getHeure_end() {
        return heure_end;
    }

    public void setSoutenance_id(int soutenance_id) {
        this.soutenance_id = soutenance_id;
    }

    public void setSoutenance_date(LocalDate soutenance_date) {
        this.soutenance_date = soutenance_date;
    }

    public void setSoutenance_salle(Salle soutenance_salle) {
        this.soutenance_salle = soutenance_salle;
    }

    public void setSoutenance_etudiant(Etudiant soutenance_etudiant) {
        this.soutenance_etudiant = soutenance_etudiant;
    }

    public void setSoutenance_president(Enseignant soutenance_president) {
        this.soutenance_president = soutenance_president;
    }

    public void setSoutenance_examinateur(Enseignant soutenance_examinateur) {
        this.soutenance_examinateur = soutenance_examinateur;
    }

    public void setSoutenance_encadreur(Enseignant soutenance_encadreur) {
        this.soutenance_encadreur = soutenance_encadreur;
    }

    public void setHeure_start(LocalTime heure_start) {
        this.heure_start = heure_start;
    }

    public void setHeure_end(LocalTime heure_end) {
        this.heure_end = heure_end;
    }

}

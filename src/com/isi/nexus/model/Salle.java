package com.isi.nexus.model;

public class Salle {
    private int classe_id;
    private int capacity;
    private String classe_name;
    private boolean hasVideoProjector;

    public Salle(){

    }

    public Salle(String classe_name, int capacity, boolean isVProjector){
        this.classe_name = classe_name;
        this.capacity = capacity;
        this.hasVideoProjector = isVProjector;
    }

    public Salle(int classe_id, String classe_name, int capacity , boolean isVProjector){
        this.classe_id = classe_id;
        this.classe_name = classe_name;
        this.capacity = capacity;
        this.hasVideoProjector = isVProjector;
    }

    public int getClasse_id() {
        return classe_id;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getClasse_name() {
        return classe_name;
    }

    public Boolean getVProjector() {
        return hasVideoProjector;
    }

    public void setClasse_id(int classe_id) {
        this.classe_id = classe_id;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setClasse_name(String classe_name) {
        this.classe_name = classe_name;
    }

    public void setVProjector(Boolean VProjector) {
        hasVideoProjector = VProjector;
    }
}

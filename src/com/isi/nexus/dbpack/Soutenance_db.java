package com.isi.nexus.dbpack;

import com.isi.nexus.model.*;
import com.isi.nexus.util.DBConnection;
import jdk.nashorn.internal.runtime.StoredScript;
import  com.isi.nexus.dbpack.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Soutenance_db {
    //declaring models
    private final Etudiant_db etudiant_db = new Etudiant_db();
    private final Salle_db salle_db = new Salle_db();
    private final Enseignant_db enseignant_db = new Enseignant_db();


    public static void createSoutenance(
            LocalDate soutenance_date,
            Salle soutenance_salle,
            Etudiant soutenance_etudiant,
            Enseignant soutenance_president,
            Enseignant soutenance_examinateur,
            Enseignant soutenance_encadreur,
            LocalTime heure_start,
            LocalTime heure_end
    ){
        String sql = "INSERT INTO soutenance(soutenance_date, soutenance_salle, soutenance_etudiant, soutenance_president, soutenance_examinateur, soutenance_encadreur, heure_start, heure_end) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setDate(1, Date.valueOf(soutenance_date));
            ps.setInt(2, soutenance_salle.getClasse_id());
            ps.setInt(3, soutenance_etudiant.getIdEtudiant());
            ps.setInt(4, soutenance_president.getEn_id());
            ps.setInt(5, soutenance_examinateur.getEn_id());
            ps.setInt(6, soutenance_encadreur.getEn_id());
            ps.setTime(7, Time.valueOf(heure_start));
            ps.setTime(8, Time.valueOf(heure_end));

            ps.executeUpdate();

        }catch(SQLException e){
            System.out.println("Erreur lors de la creation d'un etudiant");
            e.printStackTrace();
        }

    }

    public static boolean checkEtudiantInSoutenance(int etu_id){
        String sql = "SELECT 1 FROM soutenance WHERE soutenance_etudiant = ? LIMIT 1";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, etu_id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) return true;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

//Check if a "Salle" is taken.
    public static boolean checkSalleTaken(
            int salleId,
            LocalDate soutenanceDate,
            LocalTime newStart,
            LocalTime newEnd
    ) {
        String sql = "SELECT 1 FROM soutenance WHERE soutenance_salle = ? AND soutenance_date = ? AND heure_start < ? AND heure_end > ? LIMIT 1";

        try (Connection conn = DBConnection.getCon();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, salleId);
            ps.setDate(2, Date.valueOf(soutenanceDate));
            ps.setTime(3, Time.valueOf(newEnd));
            ps.setTime(4, Time.valueOf(newStart));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Check if an "Enseignant" is taken
    public static boolean checkEnseignantTaken(
            int enseignantId,
            LocalDate soutenanceDate,
            LocalTime newStart,
            LocalTime newEnd
    ) {
        String sql = "SELECT 1 FROM soutenance WHERE soutenance_date = ? AND heure_start < ? AND heure_end > ? AND (soutenance_president = ? OR soutenance_examinateur = ? OR soutenance_encadreur = ?) LIMIT 1";

        try (Connection conn = DBConnection.getCon();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(soutenanceDate));
            ps.setTime(2, Time.valueOf(newEnd));
            ps.setTime(3, Time.valueOf(newStart));
            ps.setInt(4, enseignantId);
            ps.setInt(5, enseignantId);
            ps.setInt(6, enseignantId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    //searching "Soutenance"
    public Soutenance getSoutenanceByID(int soutenance_id){
        String sql = "SELECT * FROM soutenance WHERE soutenance_id = ?";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, soutenance_id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Soutenance sout = new Soutenance();
                sout.setSoutenance_id(rs.getInt("soutenance_id"));
                sout.setSoutenance_date(rs.getDate("soutenance_date").toLocalDate());
                sout.setSoutenance_salle(salle_db.getSalleByID(rs.getInt("soutenance_salle")));
                sout.setSoutenance_etudiant(etudiant_db.getEtuId(rs.getInt("soutenance_etudiant")));
                sout.setSoutenance_president(enseignant_db.getEnseignantByID(rs.getInt("soutenance_president")));
                sout.setSoutenance_examinateur(enseignant_db.getEnseignantByID(rs.getInt("soutenance_examinateur")));
                sout.setSoutenance_encadreur(enseignant_db.getEnseignantByID(rs.getInt("soutenance_encadreur")));
                sout.setHeure_start(rs.getTime("heure_start").toLocalTime());
                sout.setHeure_end(rs.getTime("heure_end").toLocalTime());
                return sout;
            }

        }catch(SQLException e){
            System.out.println("Erreur lors de la recherche d'une soutenance");
            e.printStackTrace();
        }
        return null;
    }

    //getting all soutenance
    public List<Soutenance> getAllSoutenances(){
        List<Soutenance> soutenances = new ArrayList<>();
        String sql = "SELECT * FROM soutenance";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Soutenance sout = new Soutenance(
                        rs.getInt("soutenance_id"),
                        rs.getDate("soutenance_date").toLocalDate(),
                        salle_db.getSalleByID(rs.getInt("soutenance_salle")),
                        etudiant_db.getEtuId(rs.getInt("soutenance_etudiant")),
                        enseignant_db.getEnseignantByID(rs.getInt("soutenance_president")),
                        enseignant_db.getEnseignantByID(rs.getInt("soutenance_examinateur")),
                        enseignant_db.getEnseignantByID(rs.getInt("soutenance_encadreur")),
                        rs.getTime("heure_start").toLocalTime(),
                        rs.getTime("heure_end").toLocalTime()
                );

                soutenances.add(sout);
            }

        }catch (SQLException e){
            System.out.println("Erreur en listant les soutenances");
            e.printStackTrace();
        }
        return soutenances;
    }

    //updating "Soutenance"
    public void updateSoutenance(
            int soutenanceId,
            LocalDate soutenanceDate,
            Salle soutenanceSalle,
            Etudiant soutenanceEtudiant,
            Enseignant soutenancePresident,
            Enseignant soutenanceExaminateur,
            Enseignant soutenanceEncadreur,
            LocalTime heureStart,
            LocalTime heureEnd
    ){
        String sql = "UPDATE soutenance SET soutenance_date = ?, soutenance_salle = ?, soutenance_etudiant = ?, soutenance_president = ?, soutenance_examinateur = ?, soutenance_encadreur = ?, heure_start = ?, heure_end = ? WHERE soutenance_id = ?";

        try(Connection conn = DBConnection.getCon();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setDate(1, Date.valueOf(soutenanceDate));
            ps.setInt(2, soutenanceSalle.getClasse_id());
            ps.setInt(3, soutenanceEtudiant.getIdEtudiant());
            ps.setInt(4, soutenancePresident.getEn_id());
            ps.setInt(5, soutenanceExaminateur.getEn_id());
            ps.setInt(6, soutenanceEncadreur.getEn_id());
            ps.setTime(7, Time.valueOf(heureStart));
            ps.setTime(8, Time.valueOf(heureEnd));
            ps.setInt(9, soutenanceId);

            ps.executeUpdate();

        }catch(SQLException e){
            System.out.println("Erreur lors de la mise a jour de la soutenance");
            e.printStackTrace();
        }
    }


    //deleting a "Soutenance"
    public void delSoutenance(int soutenance_id){
        String sql = "DELETE FROM soutenance WHERE soutenance_id = ?";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, soutenance_id);
            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println("Erreur en supprimer soutenance");
            e.printStackTrace();
        }
    }
}

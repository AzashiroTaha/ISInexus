package com.isi.nexus.dbpack;

import com.isi.nexus.model.Enseignant;
import com.isi.nexus.util.DBConnection;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class Enseignant_db {
    //Creating "Enseignant"
    public static void createEnseignant(String nm, String pr, String grade, String speciality){
        String sql = "INSERT INTO enseignant(en_nom, en_pr, grade, speciality) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, nm);
            ps.setString(2, pr);
            ps.setString(3, grade);
            ps.setString(4, speciality);

            ps.executeUpdate();
        }catch (SQLException E){
            System.out.println("ERREUR lors de la creation enseignant");
        }
    }

    public Enseignant getEnseignantByID(int id){
        String sql = "SELECT * FROM enseignant WHERE en_id = ?";
        Enseignant en = new Enseignant();

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);

            ResultSet rs =  ps.executeQuery();
            if (rs.next()){
                en.setEn_id(rs.getInt("en_id"));
                en.setEn_nom(rs.getString("en_nom"));
                en.setEn_pr(rs.getString("en_pr"));
                en.setGrade(rs.getString("grade"));
                en.setSpeciality(rs.getString("speciality"));
            }else return null;
        }catch (SQLException e){
            System.out.println("Erreur");
        }
        return en;
    }

    //getting all "Enseignant"
    public List<Enseignant> gatAllEnseignant(){
        String sql = "SELECT * FROM enseignant";
        List<Enseignant> enseignants = new ArrayList<>();

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Enseignant en = new Enseignant(
                        rs.getInt("en_id"),
                        rs.getString("en_nom"),
                        rs.getString("en_pr"),
                        rs.getString("grade"),
                        rs.getString("speciality")
                );
                enseignants.add(en);
            }
        }catch (SQLException e){
            System.out.println("ERREUR");
        }
        return enseignants;
    }

    //update grade || speciality
    public boolean editEnseignant(int id, String newEn_nm, String newEn_pr, String newGrade, String newSpeciality){
        String sql = "UPDATE enseignant SET en_nom = ?, en_pr = ?, grade = ?, speciality = ? WHERE en_id = ?";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, newEn_nm);
            ps.setString(2, newEn_pr);
            ps.setString(3, newGrade);
            ps.setString(4, newSpeciality);
            ps.setInt(5, id);

            int editedLine = ps.executeUpdate();

            if(editedLine != 0) {
                return true;
            }
        }catch (SQLException e){
            System.out.println("ERREUR");
        }
        return false;
    }

    //deleting "Enseignant"
    public boolean delEnseignant(int id){
        String sql = "DELETE FROM enseignant WHERE en_id = ?";

        try(Connection conn = DBConnection.getCon();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            int editedline = ps.executeUpdate();
            if (editedline != 0){
                return true;
            }

        }catch (SQLException e){
            System.out.println("ERREUR!");
        }
        return false;
    }

}

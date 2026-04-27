package com.isi.nexus.dbpack;

import com.isi.nexus.model.Admin;
import com.isi.nexus.util.DBConnection;
import com.isi.nexus.model.Etudiant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Etudiant_db {

    //Creating "Etudiant"
    public static void createEtudiant(String matricule, String pr, String nm, String theme){
        String sql = "INSERT INTO etudiant(mat, etu_nom, etu_pr, theme) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, matricule);
            ps.setString(2, nm);
            ps.setString(3, pr);
            ps.setString(4, theme);

            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println("Erreur lors de la creation d'un etudiant");
            e.printStackTrace();
        }
    }


    public Etudiant getEtu_matricule(String matricule){
        String sql = "SELECT * FROM etudiant WHERE mat = ?";

        try(Connection conn = DBConnection.getCon();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1,matricule);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                Etudiant etu = new Etudiant();
                etu.setIdEtudiant(rs.getInt("etu_id"));
                etu.setMatricule(rs.getString("mat"));
                etu.setEtuName(rs.getString("etu_nom"));
                etu.setEtuNicknm(rs.getString("etu_pr"));
                etu.setTheme(rs.getString("theme"));
                return etu;
            }
        } catch (SQLException e) {
            System.out.println("ERREUR");
        }
        return null;
    }

    //getting "Etudiant" by id (for UI)
    public Etudiant getEtuId(int etu_id){
        String sql = "SELECT * FROM etudiant WHERE etu_id = ?";

        try(Connection conn = DBConnection.getCon();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1,etu_id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                Etudiant etu = new Etudiant();
                etu.setIdEtudiant(rs.getInt("etu_id"));
                etu.setMatricule(rs.getString("mat"));
                etu.setEtuName(rs.getString("etu_nom"));
                etu.setEtuNicknm(rs.getString("etu_pr"));
                etu.setTheme(rs.getString("theme"));
                return etu;
            }
        } catch (SQLException e) {
            System.out.println("ERREUR1111");
        }
        return null;
    }

    public List<Etudiant> allEtudiant(){
        List<Etudiant> etudiants =  new ArrayList<>();
        String sql = "SELECT * FROM etudiant";

        try(Connection conn = DBConnection.getCon();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Etudiant etudiant = new Etudiant(
                        rs.getInt("etu_id"),
                        rs.getString("mat"),
                        rs.getString("etu_nom"),
                        rs.getString("etu_pr"),
                        rs.getString("theme")
                );
                etudiants.add(etudiant);
            }

        }catch (SQLException e){
            System.out.println("ERREUR lors de l'excution de la requete.");
        }
        return  etudiants;
    }


    //Delete etudiant
    public boolean delEtudiant(int id){
        String sql = "DELETE FROM etudiant WHERE etu_id = ?";

        try(Connection conn = DBConnection.getCon();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            int editedline = ps.executeUpdate();
            if (editedline != 0){
                return true;
            }

        }catch (SQLException e){
            System.out.println("ERREUR lors de la suppression de cette utilisateur");
        }
        return false;
    }

    //change theme
    public boolean updateEtudiant(String matricule ,String newMatricule,String newNom, String newPr, String newTheme){
        String sql = "UPDATE etudiant SET mat = ?, etu_nom = ?, etu_pr = ?, theme = ? WHERE mat = ?";

        try(Connection conn =  DBConnection.getCon();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1,newMatricule);
            ps.setString(2,newNom);
            ps.setString(3,newPr);
            ps.setString(4,newTheme);
            ps.setString(5,matricule);

            int editedline = ps.executeUpdate();

            if (editedline != 0){
                return true;
            }

        }catch (SQLException e){
            System.out.println("ERREUR lors de la modification.");
        }
        return false;
    }
}

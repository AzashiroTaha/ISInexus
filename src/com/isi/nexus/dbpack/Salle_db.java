package com.isi.nexus.dbpack;

import com.isi.nexus.model.Soutenance;
import com.isi.nexus.util.DBConnection;
import com.isi.nexus.model.Salle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Salle_db {
    //Creating salle
    public static void createClasse(String classe_name, int capacity, boolean hasVideoProjector){
        String sql = "INSERT INTO classe(classe_name, capacity, hasVideoProjector) VALUES (?, ?, ?)";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, classe_name);
            ps.setInt(2, capacity);
            ps.setBoolean(3, hasVideoProjector);

            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println("ERREUR!");
        }
    }

    //Read "class"
    public Salle getSalleByName(String classe_name){
        String sql = "SELECT * FROM classe WHERE classe_name = ?";
       Salle sal = new Salle();

       try(Connection conn = DBConnection.getCon();
       PreparedStatement ps = conn.prepareStatement(sql)){
           ps.setString(1,classe_name);
           ResultSet rs = ps.executeQuery();
           if (rs.next()){
               sal.setClasse_id(rs.getInt("classe_id"));
               sal.setClasse_name(rs.getString("classe_name"));
               sal.setCapacity(rs.getInt("capacity"));
               sal.setVProjector(rs.getBoolean("hasVideoProjector"));
               return sal;
           }

       }catch (SQLException e){
           System.out.println("Erreur!");
       }
        return null;
    }

    public Salle getSalleByID(int classe_id){
        String sql = "SELECT * FROM classe WHERE classe_id = ?";
       Salle sal = new Salle();

       try(Connection conn = DBConnection.getCon();
       PreparedStatement ps = conn.prepareStatement(sql)){
           ps.setInt(1,classe_id);
           ResultSet rs = ps.executeQuery();
           if (rs.next()){
               sal.setClasse_id(rs.getInt("classe_id"));
               sal.setClasse_name(rs.getString("classe_name"));
               sal.setCapacity(rs.getInt("capacity"));
               sal.setVProjector(rs.getBoolean("hasVideoProjector"));
               return sal;
           }

       }catch (SQLException e){
           System.out.println("Erreur!");
       }
        return null;
    }

    public List<Salle> getAllSalles(){
        String sql = "SELECT * FROM classe";
        List<Salle> salles = new ArrayList<>();

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){
           ResultSet rs = ps.executeQuery();
           while (rs.next()){
               Salle sal = new Salle(
                       rs.getInt("classe_id"),
                       rs.getString("classe_name"),
                       rs.getInt("capacity"),
                       rs.getBoolean("hasVideoProjector")
               );
               salles.add(sal);
           }
        }catch (SQLException e){
            System.out.println("ERREUR!");
        }
        return salles;
    }

    //Update "salle"
    public boolean updateSalle(int id, String newClasse_name, int newCapacity, Boolean newHasVideoProjector){
        String sql = "UPDATE classe SET classe_name = ?, capacity = ?, hasVideoProjector = ? WHERE classe_id = ?";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1,newClasse_name);
            ps.setInt(2,newCapacity);
            ps.setBoolean(3,newHasVideoProjector);
            ps.setInt(4,id);

            int editedLine = ps.executeUpdate();
            if (editedLine != 0){
                return true;
            }
        }catch (SQLException e ){
            System.out.println("ERREUR!");
        }

        return false;
    }

    //deleting "Salle"
    public void delSalle(String salleName){
        String sql = "DELETE FROM classe WHERE classe_name = ?";
        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1,salleName);

            ps.executeUpdate();

        }catch (SQLException e){
            System.out.println("ERREUR");
            e.printStackTrace();
        }
    }
}

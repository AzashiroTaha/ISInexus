package com.isi.nexus.dbpack;

import com.isi.nexus.model.Admin;
import com.isi.nexus.dbpack.Admin_db;
import com.isi.nexus.util.DBConnection;

import javax.swing.*;
import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;


public class Admin_db {

//    static String username = "taha";
//    static String password = "bankai123";
//
//    public static void main(String[] args){
//        String sql = "INSERT INTO admins(username, password) VALUES (?, ?)";
//        String sql1 = "DELETE FROM admins WHERE admin_qid = ?";
//
//
//        try(Connection con = DBConnection.getCon();
//            PreparedStatement pstmt = con.prepareStatement(sql1) ){
//
////            pstmt.setString(1, username);
////            pstmt.setString(2, password);
//            pstmt.setInt(1,1);
//
//            int lignemodifier = pstmt.executeUpdate();
//
//            System.out.println(lignemodifier);
//
//        }catch (SQLException e){
//            System.out.println("Problem when trying to connect");
//        }
//
//
//    }


    //Create Admin
    public static void createAdmin(String username, String password){
        String sql = "INSERT INTO admin(username, password) VALUES (?, ?)";

        try (Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, username);
            ps.setString(2, password);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERREUR : creation admin impossible");
            e.printStackTrace();
        }
    }

    //Search admin
    public Admin getAdmin_username(String username){
        Admin admin = new Admin();
        String sql = "SELECT * FROM admin WHERE username = ?";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery(sql);

            if (rs.next()){
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println("ERREUR");
        }

        return admin;
    }

    //get all admin
    public List<Admin> allAdmin(){
        List<Admin> admins =  new ArrayList<>();
        String sql = "SELECT * FROM admin";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Admin admin = new Admin(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password")
                );
                admins.add(admin);
            }

        }catch (SQLException e){
            System.out.println("ERREUR lors de l'excution de la requete.");
        }
        return  admins;
    }

    //Delete admin

    public boolean delAdmin(String username){
        String sql = "DELETE FROM admin WHERE username = ?";

        try(Connection conn = DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, "username");

            int editedline = ps.executeUpdate();
            if (editedline != 0){
                return true;
            }

        }catch (SQLException e){
            System.out.println("ERREUR lors de la suppression de cette utilisateur");
        }
        return false;
    }


    //update admin
    public boolean adminChusername(String username, String newUsername, String newPassword){
        String sql = "UPDATE admin SET username = ?, password = ? WHERE username = ?";

        try(Connection conn =  DBConnection.getCon();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1,newUsername);
            ps.setString(2,newPassword);
            ps.setString(3,username);

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

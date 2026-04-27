package com.isi.nexus.util;

import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/isinexus_db";
    private static final String USER = "nexus";
    private static final String PASS = System.getenv("DB_PASS");

    private static Connection con;

    public static Connection getCon() throws SQLException {
        if(con == null || con.isClosed()){
            try {
                con = DriverManager.getConnection(URL,USER,PASS);
                System.out.println("Connection reussie!");
            }catch (SQLException e){
                System.out.println("Erreur SQL : impossible d'etablir une connection");
            }
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

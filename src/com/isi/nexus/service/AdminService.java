package com.isi.nexus.service;

import com.isi.nexus.dbpack.Admin_db;
import com.isi.nexus.model.Admin;
import com.sun.org.apache.bcel.internal.generic.ARETURN;

public class AdminService {


    public static boolean Service_createAdmin(String username, String password){
        try{
            if (username.isEmpty() || password.isEmpty()){
                System.out.println("Mot de pass ou nom d'utilisateur vide.");
                return false;
            }

            Admin_db.createAdmin(username, password);
            System.out.println("Admin Created.");
            return true;

        }catch (Exception e){
            System.out.println("SERVICE ERROR : impossible de creer un utilisateur");
            return false;
        }
    }


}

package com.isi.nexus.util;

import javax.swing.text.DateFormatter;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class funcs {
        private static Random rand = new Random();
        private static  List<String> classes = new ArrayList<>();
        static {
            classes.add("L1CS");
            classes.add("L1RI");
            classes.add("L1RT");
            classes.add("L1GL");
            classes.add("L1IAGE");
            classes.add("L1SEMI");
            classes.add("L1DS");
            //need to add other classes later!!!
        }

    public static String etu_mat(){
        String rand_mot;
        int index = rand.nextInt(classes.size());
        rand_mot = classes.get(index);

        return ("ISI-" + rand_mot + rand.nextInt(301) + "-" + rand.nextInt(4001));
    }

//    public static LocalDate dateFR(String heureStr){
//            return LocalDate.parse(heureStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//    }

    public static LocalDate dateFR(String dateStr) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, fmt);
//        return date.format(fmt);  // ← Retourne String "27/12/2024"
    }

    public static LocalTime heureFR(String heureStr) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(heureStr, fmt);
    }


    public static void main(String[] args){
            String date = "27/12/2024";
        System.out.println(dateFR(date));
    }
}

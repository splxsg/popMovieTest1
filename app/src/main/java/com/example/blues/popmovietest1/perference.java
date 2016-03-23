package com.example.blues.popmovietest1;

/**
 * Created by Blues on 22/03/2016.
 */
public class perference {
       private static String Currentsort = "";

    public static String getCurrentsort(){
        return Currentsort;
    }

    public static void setCurrentsort(String s){
        perference.Currentsort=s;
    }
}

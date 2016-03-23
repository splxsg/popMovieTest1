package com.example.blues.popmovietest1;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Blues on 22/03/2016.
 */
public class perference {
       private static String Currentsort = "";
    private static Boolean TrailerFlag = false;
    private static int moviePerPage;

    public static String getCurrentsort(){
        return Currentsort;
    }

    public static void setCurrentsort(String s){
        perference.Currentsort=s;
    }

    public static void setTrailerFlag(Boolean f)
    {TrailerFlag = f;}

    public static Boolean getTrailerFlag()
    {
        return TrailerFlag;
    }

    public static String getMovieInfoFromJSON(String JSONSTR,String info_name)
            throws JSONException {
        JSONObject ReviewJson = new JSONObject(JSONSTR);
        return ReviewJson.getString(info_name);
    }

    public static void setMoviePerPage(int page)
    {moviePerPage = page;}
    public static int getMoviePerPage()
    {return moviePerPage;}

}
